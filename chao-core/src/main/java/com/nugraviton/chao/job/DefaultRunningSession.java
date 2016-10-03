package com.nugraviton.chao.job;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.nugraviton.chao.job.rmi.TaskDef;
import com.nugraviton.chao.spi.event.TaskEventPayload;

/**
 * Represents a session. Associated to a {@code JobHandle}, 
 * takes snapshots of session executions.
 * Keeps the task execution history.
 * 
 * @author fred.wang@nuGraviton.com
 *
 */
class DefaultRunningSession implements RunningSession{

	private JobHandle jobHandle;
	
	private final Map<String, TaskDef> taskDefs;
	
	private final UUID sessionId;
	
	private final LocalDateTime startTime = LocalDateTime.now();
	private LocalDateTime endTime;
	
	private boolean failed;
	
	private boolean completed;
	
	private Throwable throwable;
	
	//Map<taskId, AsyncContext>
	private final Map<UUID, AsyncContext> currentTaskStatus = new ConcurrentHashMap<>();

	private AtomicInteger taskCompleted = new AtomicInteger(0);
	
	DefaultRunningSession(JobHandle jobHandle, UUID sessionId, Map<String, TaskDef> taskDefs){
		this.jobHandle = jobHandle;
		this.sessionId = sessionId;
		this.taskDefs = taskDefs;
	}

	@Override
	public UUID getSessionId() {
		return this.sessionId;
	}
	
	public JobHandle getJobHandle(){
		return this.jobHandle;
	}
	
	@Override
	public Map<UUID, AsyncContext> getTaskExecutions(){
		return this.currentTaskStatus;
	}
	
	@Override
	public AsyncContext taskStart(TaskEventPayload payload) {
		RmiTask rmiTask = this.taskDefs.get(payload.getTaskName()).getRmiTaskDef(sessionId);
		AsyncContext context = this.jobHandle.taskStart(rmiTask);
		currentTaskStatus.put(context.getExecutionId(), context);
		return context;
	}

	@Override
	public void taskComplete(AsyncContext asyncContext) {
		currentTaskStatus.put(asyncContext.getExecutionId(), asyncContext);
		
		if (taskCompleted.incrementAndGet() == taskDefs.size()){
			close();
		}
	}
	
	@Override
	public void taskFail(AsyncContext asyncContext, Throwable throwable) {
		currentTaskStatus.put(asyncContext.getExecutionId(), asyncContext);		
	}
	
	@Override
	public LocalDateTime getStartTime() {
		return startTime;
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
			return ChronoUnit.MILLIS.between(startTime, endTime);
		}
	}

	@Override
	public boolean isFailed() {
		return this.failed;
	}

	@Override
	public Throwable getThrowable() {
		return this.throwable;
	}
	
	@Override
	public boolean isCompleted(){
		return completed;
	}
	
	void setThrowable(Throwable throwable){
		this.failed = true;
		this.throwable = throwable;
	}

	@Override
	public void close() {
		this.jobHandle.close();
		this.jobHandle = null;
		this.completed = true;
		this.endTime = LocalDateTime.now();
	}

	@Override
	public String toString() {
		return "RunningSessionImpl [sessionId=" + sessionId + ", startTime=" + startTime + ", endTime=" + endTime
				+ ", failed=" + failed + ", completed=" + completed + ", throwable=" + throwable
				+ ", currentTaskStatus=" + currentTaskStatus + ", taskCompleted=" + taskCompleted + "]";
	}
	
}
