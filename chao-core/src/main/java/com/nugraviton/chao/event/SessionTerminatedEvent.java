package com.nugraviton.chao.event;

import com.nugraviton.chao.spi.event.SessionEventPayload;

public class SessionTerminatedEvent {

	private final SessionEventPayload payload;

	public SessionTerminatedEvent(SessionEventPayload payload) {
		this.payload = payload;
	}

	public SessionEventPayload getPayload() {
		return payload;
	}

	@Override
	public String toString() {
		return "SessionTerminatedEvent [payload=" + payload + "]";
	}

}
