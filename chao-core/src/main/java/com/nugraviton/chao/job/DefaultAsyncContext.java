package com.nugraviton.chao.job;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;


/**
 * Simulates async call. Uses executionId to remember the execution.
 * 
 * @author fred.wang@nuGraviton.com
 *
 */
public class DefaultAsyncContext implements AsyncContext, Serializable {

	private static final long serialVersionUID = 9176625077191972691L;
	
	private LocalDateTime startTime = LocalDateTime.now();
	private LocalDateTime endTime;
	
	private final String jobName;
	private final String taskName;
	private final UUID sessionId;
	private final UUID exectionId;

	private Throwable throwable;

	private boolean failed;
	
	/**
	 * Every task execution is associated with an {@link AsyncContext}.
	 * 
	 * @param jobName -- job name  
	 * @param taskName -- task name
	 * @param sessionId -- session id 
	 * @param exectionId -- the id of this execution of this task.
	 */
	public DefaultAsyncContext(String jobName, String taskName, UUID sessionId, UUID exectionId) {
		this.jobName = jobName;
		this.taskName = taskName;
		this.sessionId = sessionId;
		this.exectionId = exectionId;
	}
	
	@Override
	public String getJobName() {
		return this.jobName;
	}
	
	@Override
	public String getTaskName() {
		return this.taskName;
	}
	
	public UUID getSessionId(){
		return sessionId;
	}

	@Override
	public UUID getExecutionId() {
		return this.exectionId;
	}

	@Override
	public LocalDateTime getStartTime() {
		return this.startTime;
	}

	@Override
	public LocalDateTime getEndTime() {
		return endTime;
	}
	
	@Override
	public Long getTimeConsumed() {
		if(endTime == null){
			return null;
		}else{
			return ChronoUnit.MILLIS.between(this.startTime, endTime);
		}
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}
	
	public void setThrowable(Throwable t) {
		this.throwable = t;
		this.failed = true;
	}
	
	@Override
	public Throwable getThrowable(){
		return this.throwable;
	}
	
	@Override
	public boolean isFailed(){
		return this.failed;
	}

	@Override
	public String toString() {
		return "AsyncContextImpl [sessionId=" + sessionId + ", exectionId="
				+ exectionId + ", startTime=" + startTime + ", endTime=" + endTime + ", failed=" + failed
				+ ", throwable=" + throwable + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((exectionId == null) ? 0 : exectionId.hashCode());
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
		DefaultAsyncContext other = (DefaultAsyncContext) obj;
		if (exectionId == null) {
			if (other.exectionId != null)
				return false;
		} else if (!exectionId.equals(other.exectionId))
			return false;
		return true;
	}

}
