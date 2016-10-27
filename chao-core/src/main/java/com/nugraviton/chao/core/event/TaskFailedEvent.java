package com.nugraviton.chao.core.event;

import com.nugraviton.chao.spi.event.TaskEventPayload;

public class TaskFailedEvent {

	private TaskEventPayload payLoad;

	public TaskFailedEvent(TaskEventPayload payload) {
		this.payLoad = payload;
	}

	public TaskEventPayload getPayload() {
		return payLoad;
	}

}
