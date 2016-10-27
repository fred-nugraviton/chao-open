package com.nugraviton.chao.job;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import com.nugraviton.chao.spi.event.TaskEventPayload;

/**
 * A session is a serials of task executions in a job.
 * There are server side and client side sessions,
 * this one is for server side. 
 * 
 * @author fred.wang@nuGraviton.com
 *
 */
public interface RunningSession {
	
	UUID getSessionId();
	
	LocalDateTime getStartTime();
	
	LocalDateTime getEndTime();
	
	Long getTimeConsumed();
	
	boolean isFailed();
	
	Throwable getThrowable();

	Map<UUID, AsyncContext> getTaskExecutions();
	
	AsyncContext taskStart(TaskEventPayload payload);

	void taskComplete(AsyncContext asyncContext);
	
	void taskFail(AsyncContext asyncContext, Throwable throwable);
	
	boolean isCompleted();

	void close();
	
}
