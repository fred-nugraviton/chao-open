package com.nugraviton.chao;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

class ChaoThreadFactory implements ThreadFactory {

	private final ThreadGroup threadGroup;
	private final String threadNamePrefix;
	private final Boolean daemon;

	private final int threadPriority = Thread.NORM_PRIORITY;
	private final AtomicInteger threadCount = new AtomicInteger(0);
	
	ChaoThreadFactory(String threadGroupName, String threadNamePrefix, boolean daemon){
		this.threadGroup = new ThreadGroup(threadGroupName);
		this.threadNamePrefix = threadNamePrefix;
		this.daemon = daemon;
	}
	
	@Override
	public Thread newThread(Runnable runnable) {
		Thread thread = new Thread(threadGroup, runnable, nextThreadName());
		thread.setPriority(threadPriority);
		thread.setDaemon(daemon);
		return thread;
	}
	
	private String nextThreadName() {
		return threadNamePrefix + this.threadCount.incrementAndGet();
	}

}
