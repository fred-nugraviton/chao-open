package com.nugraviton.chao;

import java.rmi.AccessException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nugraviton.chao.event.ChaoStartedEvent;
import com.nugraviton.chao.event.DeploymentEvent;
import com.nugraviton.chao.event.EventBus;
import com.nugraviton.chao.event.JobSDeployedEvent;
import com.nugraviton.chao.job.AsyncContext;
import com.nugraviton.chao.job.JobContainer;
import com.nugraviton.chao.job.JobStreamHandler;
import com.nugraviton.chao.job.Workflow;
import com.nugraviton.chao.job.rmi.RmiCallback;
import com.nugraviton.chao.spi.event.TaskEventPayload;

/**
 * 
 * The life cycle of this container used by:
 * 1. spring context call backs.
 * 2. Deployer to reload jobs.
 * 3. user manually stop/start/reload.
 * 
 * @author fred.wang@nuGraviton.com
 *
 */
@SuppressWarnings("unused")
public class Embedded implements Chao, TaskExecutable{
	
	private static Logger log = LoggerFactory.getLogger(Embedded.class);
	
	private static AtomicBoolean instantiated = new AtomicBoolean(false);
	
	private AtomicBoolean started = new AtomicBoolean(false);
	private AtomicBoolean shutdown= new AtomicBoolean(false);
	private AtomicBoolean terminated = new AtomicBoolean(false);
	
	private ReentrantLock lock = new ReentrantLock();
    private final Condition termination = lock.newCondition();
	
	//Map<jobName, jobContainer>
	private Map<String, JobContainer> jobs = new ConcurrentHashMap<>();
	
	private Registry rmiRegistry;
	private EventBus eventBus;
	
	private int rmiPort = 1099;
	
	private TaskScheduler taskScheduler;
	private RmiCallback rmiCallback;

	private JobStreamHandler jobStreamHandler;

	private DeploymentEvent deployment;
	/**
	 * Construct a core. All classes are scanned to load 
	 * jobs and tasks. it's convenient if the number of classes in your system is relatively small 
	 * but this will defeat on-demand class loading.
	 * 
	 * Once an instance is created, you cannot create another one until the instance is terminated. 
	 * Once the instance is terminated by calling {@code #shutdown()} or {@code #shutdownNow()},  
	 * you can create another one. 
	 * 
	 * 
	 */
	public Embedded(){
		try {
			if(instantiated.get()){
				throw new ChaoException("error creating core.");
			}
			instantiated.set(true);
			this.rmiRegistry = LocateRegistry.createRegistry(rmiPort);
			this.rmiCallback = new RmiCallbackImpl(this);
			this.rmiRegistry.rebind(RmiCallback.RMI_CALL_BACK, UnicastRemoteObject.exportObject(rmiCallback, 0));
			this.taskScheduler = new ThreadPoolTaskScheduler();
			this.eventBus = new EventBus(taskScheduler, this);
			this.jobStreamHandler = new DefaultJobStreamHandler();
			this.deployment = new DeploymentEvent(); 
			
		} catch (Throwable e) {
			throw new ChaoException("error creating core.", e);
		}
	}
	

	/**
	 * Preferred way to construct a core. All the given annotated packages are scanned 
	 * to load jobs. This is more efficient if you have a large number of classes.
	 * 
	 * @param packages -- packages to scan.
	 */
	public Embedded(String... packages){ 
		this();
		this.deployment = new DeploymentEvent(packages); 
	}
	
	public void publishExceptionEvent(Throwable t){
		this.eventBus.publishExceptionEvent(t);
	}

	
	
	@Override
	public void start() {
		this.lock.lock();
        try {
        	
			if(shutdown.get()){
				throw new IllegalStateException("cannot start, core has been shut down.");
			}
			
			if(!started.get()){
				taskScheduler.execute(eventBus);
				taskScheduler.scheduleAtFixedRate(jobStreamHandler, 100);
				started.set(true);
				eventBus.publish(new ChaoStartedEvent());
				eventBus.publish(deployment);
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
        		shutdown.set(true);
        		for(JobContainer job : jobs.values()){
        			job.shutdown();
        		}
        		
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
				started.set(false);
				shutdown.set(true);
				terminated.set(true);
				for(JobContainer job : jobs.values()){
					job.shutdownNow();
				}
				dispose();
				this.termination.signalAll();
			}
        
		} catch (Exception e) {
			log.error("error shutdownNow", e);
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

	@Override
	public void awaitTermination() {
        lock.lock();
        try {
            while (!terminated.get()) {
               	termination.await();
            }
        } catch(InterruptedException e){
        	// ignore.
        } finally {
            lock.unlock();
        }
	}
	
	@Override
	public void terminateSession(TaskEventPayload payload) {
		try {
			JobContainer job = jobs.get(payload.getJobName());
			if(job != null){
				job.terminateSession(payload);
			}
		}catch(Throwable e){
			eventBus.publishExceptionEvent(e);
		}
	}
	
	private void tryNotifyTermination() {
		 lock.lock();
	     try {
	    	 if(shutdown.get() && !terminated.get()){
	    		 
				for(JobContainer job : jobs.values()){
					if(!job.isTerminated()){
						return;
					}
				}
				this.terminated.set(true);
				dispose();
				this.termination.signalAll();
	    	 }
        } catch (Exception e) {
			log.error("error notify termination.");
		} finally {
            lock.unlock();
        }
		
	}
	
	/**
	 * Register the Job to container, 
	 * called by {@code DeployedListener} from reflection.
	 * @param app {@code Job}
	 */
	private void deployJobs(Map<String, Workflow> wfs){
		
		this.lock.lock();
		try {
			
			if(!this.jobs.isEmpty()){
				Exception e = new DeploymentException("can not deployment again");
				eventBus.publishExceptionEvent(e);
			}
			
			this.jobs.putAll(new JobBuilder()
					.withWorkflows(wfs)
					.withStreamHandler(jobStreamHandler)
					.withRmiRegistry(rmiRegistry)
					.withEventBus(eventBus)
					.withTaskSchduler(taskScheduler)
					.build());
		
			for(JobContainer job : jobs.values()){
				job.activate();
			}
			eventBus.publish(new JobSDeployedEvent());
			
		}catch(Exception e){
			eventBus.publishExceptionEvent(e);
		} finally {
	         lock.unlock();
	    }
	}
	
	private void deregisterStream(UUID sessionId) {
		jobStreamHandler.deregisterStream(sessionId);
		if(log.isTraceEnabled()){
			log.trace("deregistered stream: {}", sessionId);
		}
	}
	
	@Override	
	public void activateJob(String jobName){

		lock.lock();
        try{
        	JobContainer job = jobs.get(jobName);
        	checkJobExistence(job, jobName);
        	job.activate();
        }catch(Throwable e) {
        	eventBus.publishExceptionEvent(e);
        }finally {
            lock.unlock();
        }
     }
	
	@Override
	public void deactivateJob(String jobName){
		
		lock.lock();
        try{
        	JobContainer job = jobs.get(jobName);
        	checkJobExistence(job, jobName);
        	job.deactivate();
        }catch(Throwable e){
        	eventBus.publishExceptionEvent(e);
        }finally {
            lock.unlock();
        }
	}
	
	public void taskStart(TaskEventPayload payload) {
		try{
			String jobName = payload.getJobName(); 
			JobContainer job = jobs.get(jobName);
			checkJobExistence(job, jobName);
			job.taskStart(payload);
		}catch(Throwable e){
			eventBus.publishExceptionEvent(e);
		}
	}
	
	@Override
	public void taskCompleted(AsyncContext asyncContext) {
		try{
			String jobName = asyncContext.getJobName(); 
			JobContainer job = jobs.get(jobName);
			checkJobExistence(job, jobName);
			job.taskCompleted(asyncContext);
		}catch(Throwable e){
			eventBus.publishExceptionEvent(e);
		}
	}

	@Override
	public void taskFailed(AsyncContext asyncContext) {
		try {
			String jobName = asyncContext.getJobName(); 
			JobContainer job = jobs.get(jobName);
			checkJobExistence(job, jobName);
			job.taskFailed(asyncContext);
		}catch(Throwable e){
			eventBus.publishExceptionEvent(e);
		}
		
	}
	
	private void checkJobExistence(JobContainer job, String jobName) {
		if(job == null){
			throw new IllegalArgumentException("none existent job [" + jobName +"]");
		}
	}
	

	private void dispose() throws Exception {
		this.rmiRegistry.unbind(RmiCallback.RMI_CALL_BACK);
		UnicastRemoteObject.unexportObject(rmiCallback, true);
		this.taskScheduler.shutdownNow();
		instantiated.set(false);
	}
	
}
