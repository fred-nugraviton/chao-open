package com.nugraviton.chao.job;

import java.util.UUID;

public interface JobStreamHandler extends Runnable {

	void registerStream(ProcessInputReader reader);

	void deregisterStream(UUID sessionId);

}
