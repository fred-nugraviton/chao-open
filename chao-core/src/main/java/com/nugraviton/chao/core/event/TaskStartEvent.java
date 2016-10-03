package com.nugraviton.chao.core.event;

import com.nugraviton.chao.spi.event.TaskEventPayload;

public class TaskStartEvent {

	private TaskEventPayload payload;

	public TaskStartEvent(TaskEventPayload payload) {
		this.payload = payload;
	}

	public TaskEventPayload getPayload() {
		return payload;
	}

	@Override
	public String toString() {
		return "TaskStartEvent [payload=" + payload + "]";
	}

}
