package com.nugraviton.chao.event;

import com.nugraviton.chao.job.JobContainer;

public class JobStartedEvent {
	
	private final JobContainer job;

	public JobStartedEvent(JobContainer job) {
		this.job = job;
	}

	public JobContainer getJob() {
		return job;
	}

	@Override
	public String toString() {
		return "JobStartedEvent [job=" + job.getJobName() + "]";
	}
	
	
	
}
