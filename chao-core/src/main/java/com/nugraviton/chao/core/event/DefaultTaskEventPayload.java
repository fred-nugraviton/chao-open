package com.nugraviton.chao.core.event;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import com.nugraviton.chao.job.AsyncContext;
import com.nugraviton.chao.spi.event.TaskEventPayload;

/**
 * Event pay load for session related events.
 * 
 * @author fred.wang@nuGraviton.com
 *
 */
public final class DefaultTaskEventPayload implements TaskEventPayload{

	private final String jobName;
	private final String taskName;
	
	private UUID sessionId;
	private UUID executionId;
	
	private boolean failed;
	private Throwable throwable;
	
	private final LocalDateTime  startTime = LocalDateTime.now();
	private LocalDateTime  endTime;
	private boolean startSession;
	
	public DefaultTaskEventPayload(String jobName, String taskName, boolean startSession) {
		this.jobName = jobName;
		this.taskName = taskName;
		this.startSession = startSession;
		if(startSession){
			this.sessionId = UUID.randomUUID();
		}
	}
	
	public DefaultTaskEventPayload(String jobName, String taskName, UUID sessionId, UUID executionId, boolean startSession) {
		
		this(jobName, taskName, startSession);
		this.sessionId = sessionId;
		this.executionId = executionId;
	}
	

	public DefaultTaskEventPayload(AsyncContext context) {
		
		this(context.getJobName(), context.getTaskName(), context.getSessionId());
		this.executionId = context.getExecutionId();
		this.throwable = context.getThrowable();
	}
	
	public DefaultTaskEventPayload(String jobName, String taskName, UUID sessionId) {
		this(jobName, taskName, false);
		this.sessionId = sessionId;
	}

	/* (non-Javadoc)
	 * @see com.nugraviton.chao.spi.event.TaskEventPayload#getSessionId()
	 */
	@Override
	public UUID getSessionId() {
		return sessionId;
	}

	/* (non-Javadoc)
	 * @see com.nugraviton.chao.spi.event.TaskEventPayload#getJobName()
	 */
	@Override
	public String getJobName() {
		return jobName;
	}	
	
	/* (non-Javadoc)
	 * @see com.nugraviton.chao.spi.event.TaskEventPayload#getTaskName()
	 */
	@Override
	public String getTaskName(){
		return this.taskName;
	}
	
	/* (non-Javadoc)
	 * @see com.nugraviton.chao.spi.event.TaskEventPayload#getExecutionId()
	 */
	@Override
	public UUID getExecutionId(){
		return executionId;
	}

	/* (non-Javadoc)
	 * @see com.nugraviton.chao.spi.event.TaskEventPayload#getStartTime()
	 */
	@Override
	public LocalDateTime getStartTime() {
		return startTime;
	}
	/* (non-Javadoc)
	 * @see com.nugraviton.chao.spi.event.TaskEventPayload#getEndTime()
	 */
	@Override
	public LocalDateTime getEndTime(){
		return endTime;
	}
	

	/* (non-Javadoc)
	 * @see com.nugraviton.chao.spi.event.TaskEventPayload#isStartSession()
	 */
	@Override
	public boolean isStartSession() {
		return startSession;
	}
	
	/**
	 * Gets time consumed to execute the task.
	 * @return Long -- milliseconds, null if task not completed.
	 */
	Long getTimeConsumed(){
		if(endTime == null){
			return null;
		}else{
			return ChronoUnit.MILLIS.between(this.startTime, endTime);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.nugraviton.chao.spi.event.TaskEventPayload#isFailed()
	 */
	@Override
	public boolean isFailed(){
		return this.failed;
	}
	
	/* (non-Javadoc)
	 * @see com.nugraviton.chao.spi.event.TaskEventPayload#getThrowable()
	 */
	@Override
	public Throwable getThrowable(){
		return this.throwable;
	}

	/* (non-Javadoc)
	 * @see com.nugraviton.chao.spi.event.TaskEventPayload#toString()
	 */
	@Override
	public String toString() {
		return "TaskEventPayload [jobName=" + jobName + ", taskName=" + taskName
				+ ", sessionId=" + sessionId + ", executionId=" + executionId + ", failed=" + failed + ", throwable="
				+ throwable + ", startTime=" + startTime + ", endTime=" + endTime + ", startSession=" + startSession
				+ "]";
	}

}
