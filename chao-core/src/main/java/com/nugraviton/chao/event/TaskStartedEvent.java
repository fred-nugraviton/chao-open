package com.nugraviton.chao.event;

import com.nugraviton.chao.spi.event.TaskEventPayload;

public class TaskStartedEvent {

	private final TaskEventPayload payload;

	public TaskStartedEvent(TaskEventPayload payload) {
		this.payload = payload;
	}

	public TaskEventPayload getPayload() {
		return payload;
	}

	@Override
	public String toString() {
		return "TaskStartedEvent [payload=" + payload + "]";
	}

}
