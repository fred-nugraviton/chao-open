package com.nugraviton.chao.core.event;

import com.nugraviton.chao.spi.event.TaskEventPayload;

public class TaskCompletedEvent {

	private final TaskEventPayload payload;

	public TaskCompletedEvent(TaskEventPayload payload) {
		this.payload = payload;
	}

	public TaskEventPayload getPayload() {
		return payload;
	}

	@Override
	public String toString() {
		return "TaskCompletedEvent [payload=" + payload + "]";
	}
	
}
