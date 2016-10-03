package com.nugraviton.chao.core.event;

import java.util.UUID;

import com.nugraviton.chao.job.RunningSession;

public class SessionCompletedEvent {

	private final RunningSession session;

	public SessionCompletedEvent(RunningSession session) {
		this.session = session;
	}

	public UUID getSessionId() {
		return session.getSessionId();
	}

	@Override
	public String toString() {
		return "SessionCompletedEvent [session=" + session.getSessionId() + "]";
	}

	
}
