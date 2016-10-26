package com.nugraviton.chao.job;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nugraviton.chao.core.TaskRegistrar;
import com.nugraviton.chao.core.TaskScheduler;
import com.nugraviton.chao.core.event.JobStartedEvent;
import com.nugraviton.chao.core.event.JobStoppedEvent;
import com.nugraviton.chao.core.event.JobTerminatedEvent;
import com.nugraviton.chao.core.event.EventBus;
import com.nugraviton.chao.core.event.DefaultTaskEventPayload;
import com.nugraviton.chao.core.event.TaskFailedEvent;
import com.nugraviton.chao.job.rmi.RmiTaskExecutor;
import com.nugraviton.chao.job.rmi.TaskDef;
import com.nugraviton.chao.spi.event.TaskEventPayload;

/**
 * Job containers are isolated from each other by their class loaders. 
 * Each one manages its own job executions.
 * A crashed container won't affect others. All get methods return a copy of something,
 * this is intended to prevent JobContainers from being leaked to outside. 
 * Chao does not expose any JobContainer. 
 *  
 * @author fred.wang@nuGraviton.com
 *
 */
public class DefaultJobContainer implements JobContainer{
	
	private static Logger log = LoggerFactory.getLogger(DefaultJobContainer.class);
	
	private ReentrantLock lock = new ReentrantLock();
	
	private AtomicBoolean active= new AtomicBoolean(false);
	private AtomicBoolean shutdown = new AtomicBoolean(false); 
	private AtomicBoolean terminated = new AtomicBoolean(false);
	
	private Workflow workflow;

	private Registry registry;
	private JobStreamHandler jobStreamHandler;
	private EventBus eventBus;
	private SessionManager sm;

	private JobDef jobDef;

	private TaskRegistrar taskRegistrar;
	
	@Override
	public String getJobName(){
		return this.jobDef.getJobName();
	}
	
	/**
	 * return a clone of JobConfInfo.
	 */
	@Override
	public JobDef getJobConfInfo(){
		return this.jobDef.clone();
	}
	
	/**
	 * Get snapshot of RmiTaskDef
	 * @return a map of taskDef with their names.
	 */
	@Override
	public Map<String, TaskDef> getTaskDefs(){
		return workflow.deepCopy().getTaskDefs();
	}
	
	/**
	 * Creates a {@code JobHandle} for this {@code JobContainer}.
	 * An JVM process is created under the hood. Intent 
	 * only for container use.
	 * @param UUID -- the sessionId this JobHandle belongs to.
	 * 
	 * @return JobHandle -- wraps a new JVM process.
	 */
	JobHandle newJobHandle(UUID sessionId){
			
		lock.lock();
		
		Process process = null;
		
		try {
			CommandBuilder commandBuilder = new CommandBuilder();
			commandBuilder.withClassPath(jobDef.getClassPath());
			//commandBuilder.withJdwp(52260, false);
			commandBuilder.withJobWorkDir(Paths.get(jobDef.getJobWorkDir()));
			commandBuilder.setJobHandleId(sessionId);
			
			List<String> commandStrng = commandBuilder.build();
			if(log.isDebugEnabled()){
				log.debug("command={}", commandStrng);
			}
			
			ProcessBuilder pb = new ProcessBuilder(commandStrng);
			pb.directory(Paths.get(jobDef.getJobWorkDir()).toFile());
			
			pb.redirectErrorStream(true);
			process = pb.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			//wait for the signal, otherwise may get NotBoundException.
			while(true){
				String singnal = reader.readLine();
				if(log.isDebugEnabled()){
					log.debug("[{}: {}]", jobDef.getJobName(), singnal);
				}
				if(singnal == null || RmiTaskExecutor.JOB_RUNNER_ERROR_SIGNAL.equals(singnal)){
					throw new ContainerException(String.format("error starting process for %s because", jobDef.getJobName()));
				}
				if(RmiTaskExecutor.JOB_EXECUTOR_BINDED_SIGNAL.equals(singnal)){
					break;
				}
			}
			if(!process.isAlive()){
				throw new ContainerException(String.format("error starting process for %s", jobDef.getJobName()));
			}
			
			RmiTaskExecutor jobExecutor = (RmiTaskExecutor)registry.lookup(sessionId.toString());
			
			JobHandle jobSession = new DefaultJobHandle(sessionId, process, jobExecutor);
			
			jobStreamHandler.registerStream(new DefaultProcessInputReader(jobDef.getJobName(), sessionId, reader));
			return jobSession;
			
		} catch (Throwable e) {
			
			log.error("error creating JobHandle", e);
			if(process != null && process.isAlive()){
				process.destroyForcibly();
			}
			
			if(registry != null){
				try {
					registry.unbind(sessionId.toString());
				} catch (Exception e1) {
					// just ignore.
				} 
			}
			throw new ContainerException(String.format("error starting process for %s", jobDef.getJobName()), e);
		} finally{
			lock.unlock();
		}
	}

	@Override
	public void taskStart(TaskEventPayload payload) {
		if(payload.isStartSession() && !active.get()){
			if(log.isDebugEnabled()){
				log.debug("job is not started, ignored {}", payload);
			}
			return;
		}
		
		if(payload.isStartSession() && this.jobDef.isFixedDelay()){
			//cancel the schedule now, will restore after job completion. 
			this.taskRegistrar.cancelTask(payload);
		}
		
		sm.taskStart(payload);
	}

	@Override
	public void taskCompleted(AsyncContext asyncContext) {
		if(!terminated.get()){
			sm.taskCompleted(asyncContext);
			if(!active.get() && !sm.hasRunningSession()){
				eventBus.publish(new JobStoppedEvent(this));
			}
		}
	}
	
	@Override
	public void terminateSession(TaskEventPayload payload) {
		if(!terminated.get()){
			sm.terminateSession(payload);
		}
	}

	@Override
	public void taskFailed(AsyncContext asyncContext) {
		log.error(String.format("task failed {}", asyncContext), asyncContext.getThrowable());
		// swallow the failure if the job has been terminated.
		if(!terminated.get()){
			
			TaskEventPayload payload = new DefaultTaskEventPayload(asyncContext);
			
			TaskFailedEvent taskFailedEvent = new TaskFailedEvent(payload);
			eventBus.publish(taskFailedEvent);
		}
	}
	
	@Override
	public void activate() {
		
	        this.lock.lock();
	        try {
	        	if(shutdown.get()){
	        		throw new IllegalStateException(
	        				String.format("Job [%s] has shutdown, cannot start ", this.getJobName()));
	        	}
	        	
	        	if(!active.get()){
	        		this.active.set(true);
	        		TaskDef task = workflow.getStartTask();
	        		if(!this.sm.hasRunningSession() && !taskRegistrar.isScheduled(task.getTaskName())){
	        			taskRegistrar.scheduleTask(workflow.getStartTask());
	        		}
	        		eventBus.publish(new JobStartedEvent(this));
	        	}
	        	
	        } finally {
	            this.lock.unlock();
	        }
	}
	
	public void tryRescheduleFixedDelayJob() {
		if(active.get() && workflow.getJobDef().isFixedDelay()){
			taskRegistrar.tryRescheduleFixedDelayJob(workflow.getStartTask());
		}
	}

	@Override
	public boolean isActive() {
		return this.active.get();
	}
	
	@Override
	public void deactivate() {
		this.lock.lock();
		try {
			if(active.get()){
				this.active.set(false);
			}
			
	    } finally {
	        this.lock.unlock();
	    }
	}

	@Override
	public void shutdown() {

		this.lock.lock();
        try {
        	if(!shutdown.get()){
        		taskRegistrar.cancelAllTasks();
		        active.set(false);
		        shutdown.set(true);
		        tryNotifyTermination();
        	}
        } finally {
            this.lock.unlock();
		}
	}

	@Override
	public void shutdownNow() {
		this.lock.lock();
        try {
        	if(!terminated.get()){
        		taskRegistrar.cancelAllTasks();
	        	active.set(false);
	        	shutdown.set(true);
	        	sm.terminate();
	        	terminated.set(true);
	        	tryNotifyTermination();
        	}
        } finally {
            this.lock.unlock();
		}
	}

	@Override
	public boolean isShutdown() {
		return shutdown.get();
	}

	@Override
	public boolean isTerminated() {
		return terminated.get();
	}

	
	
	public void tryNotifyTermination() {
		lock.lock();
		try {
			if(shutdown.get() && !terminated.get()){
				if(!sm.hasRunningSession()){
					terminated.set(true);
					eventBus.publish(new JobTerminatedEvent(this));
				}
			}
			
		} finally {
			lock.unlock();
	    }
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((jobDef == null) ? 0 : jobDef.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DefaultJobContainer other = (DefaultJobContainer) obj;
		if (jobDef == null) {
			if (other.jobDef != null)
				return false;
		} else if (!jobDef.equals(other.jobDef))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "Job [jobConfigInfo=" + jobDef + "]";
	}
	

	@SuppressWarnings("unused")
	private void setWorkflow(Workflow workflow){
		this.workflow = workflow;
		this.jobDef = (DefaultJobDef)workflow.getJobDef();
	}

	@SuppressWarnings("unused")
	private void setEventBus(EventBus eventBus){
		this.eventBus = eventBus;
		this.sm = new SessionManager(this);
		this.sm.setEventBus(eventBus);
	}
	
	@SuppressWarnings("unused")
	private void setJobRegistrar(TaskScheduler taskScheduler){
		this.taskRegistrar = new TaskRegistrar(eventBus, taskScheduler);
	}
	
	@SuppressWarnings("unused")
	private void setStreamHandler(JobStreamHandler jobStreamHandler){
		this.jobStreamHandler = jobStreamHandler;
	}
	
	@SuppressWarnings("unused")
	private void setRmiRegistry(Registry registery){
		this.registry = registery;		
	}
}
