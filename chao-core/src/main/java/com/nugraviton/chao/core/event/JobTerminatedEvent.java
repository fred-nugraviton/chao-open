package com.nugraviton.chao.core.event;

import com.nugraviton.chao.job.JobContainer;

public class JobTerminatedEvent {

	private final JobContainer job;

	public JobTerminatedEvent(JobContainer job) {
		this.job = job;
	}

	public JobContainer getJob() {
		return job;
	}

	@Override
	public String toString() {
		return "JobTerminatedEvent [job=" + job.getJobName() + "]";
	}
	
	
}
