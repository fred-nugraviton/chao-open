package com.nugraviton.chao.spi.event;

import java.time.LocalDateTime;
import java.util.UUID;

public interface TaskEventPayload {

	String getJobName();

	String getTaskName();
	
	UUID getSessionId();

	UUID getExecutionId();

	LocalDateTime getStartTime();

	LocalDateTime getEndTime();

	boolean isStartSession();

	boolean isFailed();

	Throwable getThrowable();

}