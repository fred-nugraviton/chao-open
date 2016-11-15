package com.nugraviton.chao.event;

import com.nugraviton.chao.spi.event.SessionEventPayload;

public class SessionStartedEvent {

	private final SessionEventPayload payload;

	public SessionStartedEvent(SessionEventPayload payload) {
		this.payload = payload;
	}

	public SessionEventPayload getPayload() {
		return payload;
	}

	@Override
	public String toString() {
		return "SessionStartedEvent [payload=" + payload + "]";
	}

}
