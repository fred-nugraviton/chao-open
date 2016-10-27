package com.nugraviton.chao.spi.event;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import com.nugraviton.chao.job.JobDef;
import com.nugraviton.chao.job.RunningSession;

/**
 * Event pay load for session related events. 
 * 
 * @author fred.wang@nuGraviton.com
 *
 */
public final class SessionEventPayload{
	
	private JobDef jobDef;
	private UUID sessionID;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private Throwable throwable;
	private boolean failed;
	
	public SessionEventPayload(JobDef jobConf, RunningSession runningSession) {
		this.jobDef = jobConf;
		this.sessionID = runningSession.getSessionId(); 
		this.startTime = runningSession.getStartTime();
		this.endTime = runningSession.getEndTime();
		this.throwable = runningSession.getThrowable();
		this.failed = runningSession.isFailed();
	}
	
	public JobDef getJobDef(){
		return this.jobDef;
	}
	
	public UUID getSessionId() {
		return sessionID;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}
	
	public LocalDateTime getEndTime(){
		return endTime;
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
	
	public boolean isFailed(){
		return this.failed;
	}
	
	public Throwable getThrowable(){
		return this.throwable;
	}

	@Override
	public String toString() {
		return "SessionEventPayload [sessionID=" + sessionID + ", startTime=" + startTime + ", endTime=" + endTime
				+ ", throwable=" + throwable + ", failed=" + failed + "]";
	}
}
