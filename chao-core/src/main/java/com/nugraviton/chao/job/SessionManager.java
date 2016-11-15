package com.nugraviton.chao.job;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.nugraviton.chao.TaskExecutable;
import com.nugraviton.chao.event.DefaultTaskEventPayload;
import com.nugraviton.chao.event.EventBus;
import com.nugraviton.chao.event.SessionCompletedEvent;
import com.nugraviton.chao.event.SessionStartedEvent;
import com.nugraviton.chao.event.SessionTerminatedEvent;
import com.nugraviton.chao.event.TaskCompletedEvent;
import com.nugraviton.chao.event.TaskStartEvent;
import com.nugraviton.chao.event.TaskStartedEvent;
import com.nugraviton.chao.job.rmi.TaskDef;
import com.nugraviton.chao.spi.event.SessionEventPayload;
import com.nugraviton.chao.spi.event.TaskEventPayload;

class SessionManager implements TaskExecutable{

	//private static Logger log = LoggerFactory.getLogger(SessionManager.class);
	
	private EventBus eventBus;
	private final DefaultJobContainer jobContainer;
	private Map<String, TaskDef> taskDefs;
	private final DefaultJobDef jobConf;
	
	private Map<UUID, RunningSession> runningSessions = new ConcurrentHashMap<>();
	
	SessionManager(DefaultJobContainer jobContainer) {
		this.jobContainer = jobContainer;
		this.jobConf = (DefaultJobDef)jobContainer.getJobConfInfo();
		this.taskDefs = jobContainer.getTaskDefs();
	}

	void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}
	
	boolean hasRunningSession(){
		return !runningSessions.isEmpty();
	}
	
	/**
	 * To start a task. 
	 * Creates a new session if it is a start task, otherwise executes in its session. 
	 */
	public void taskStart(TaskEventPayload payload){
		
		UUID sessionId = payload.getSessionId();
		AsyncContext asyncContext = null;
		RunningSession session = null;
		
		if(payload.isStartSession()){
			
			if(runningSessions.size() >= jobConf.getMaxSession()){
				
				throw new MaxJobSessionException(
						String.format("maxSession [%s] is reached for [%s].", jobConf.getMaxSession(), jobConf.getJobName()));
			}
			
			// starts new session.
			session = startSession(sessionId);
			
		}else {
			session = this.runningSessions.get(sessionId);
		}
		
		asyncContext = session.taskStart(payload);
		String jobName = asyncContext.getJobName();
		String taskName = asyncContext.getTaskName();
		UUID executionId = asyncContext.getExecutionId();
		
		TaskEventPayload eventPayload = new DefaultTaskEventPayload(jobName, taskName, sessionId, executionId, false);
		eventBus.publish(new TaskStartedEvent(eventPayload));
	}

	private TaskEventPayload newTaskEventPayload(AsyncContext asyncContext) {
		String jobName = asyncContext.getJobName();
		String taskName = asyncContext.getTaskName();
		UUID sessionId = asyncContext.getSessionId();
		UUID executionId = asyncContext.getExecutionId();
		
		TaskEventPayload payload = new DefaultTaskEventPayload(jobName, taskName, sessionId, executionId, false);
		return payload;
	}
	
	@Override
	public void taskCompleted(AsyncContext asyncContext) {
		
		RunningSession session = runningSessions.get(asyncContext.getSessionId());
		session.taskComplete(asyncContext);
		
		TaskEventPayload payload = newTaskEventPayload(asyncContext);
		
		eventBus.publish(new TaskCompletedEvent(payload));
		
		if(session.isCompleted()) {
			
			runningSessions.remove(session.getSessionId());
			eventBus.publish(new SessionCompletedEvent(session));
			
			if(runningSessions.isEmpty()){
				jobContainer.tryNotifyTermination();
			}

			jobContainer.tryRescheduleFixedDelayJob();
			
		}else{
			
			TaskDef task =taskDefs.get(asyncContext.getTaskName()); 
			
			for(TaskDef nextTask : task.getNextTasks()){
				String jobName = nextTask.getJobName();
				String taskName = nextTask.getTaskName();
				payload = new DefaultTaskEventPayload(jobName, taskName, asyncContext.getSessionId());
				eventBus.publish(new TaskStartEvent(payload));
			}
		}
	}

	@Override
	public void taskFailed(AsyncContext asyncContext) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void terminateSession(TaskEventPayload payload) {
		
		RunningSession session = runningSessions.remove(payload.getSessionId());
		session.close();
		
		SessionEventPayload sessionEventPayload = new SessionEventPayload(jobConf.clone(), session);
		eventBus.publish(new SessionTerminatedEvent(sessionEventPayload));
	}


	private RunningSession startSession(UUID sessionId) {
		
		try{
			JobHandle jobHandle = jobContainer.newJobHandle(sessionId);
			RunningSession runningSession = new DefaultRunningSession(jobHandle, sessionId, taskDefs);
			this.runningSessions.put(sessionId, runningSession);
			
			SessionEventPayload sessionEventPayload = new SessionEventPayload(jobConf.clone(), runningSession);
			eventBus.publish(new SessionStartedEvent(sessionEventPayload));
			
			return runningSession;
			
		}catch(Throwable e){
			throw new JobException("error creating Session", e);
		}
	}

	public void terminate() {
		for (RunningSession session : runningSessions.values()) {
			session.close();
		}
		runningSessions.clear();
	}

}
