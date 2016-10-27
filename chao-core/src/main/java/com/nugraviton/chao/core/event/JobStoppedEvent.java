package com.nugraviton.chao.core.event;

import com.nugraviton.chao.job.JobContainer;

public class JobStoppedEvent {

	private final JobContainer jobContainer;

	public JobStoppedEvent(JobContainer jobContainer) {
		this.jobContainer = jobContainer;
	}

	public JobContainer getJobContainer() {
		return jobContainer;
	}

	@Override
	public String toString() {
		return "JobStoppedEvent [jobContainer=" + jobContainer.getJobName() + "]";
	}
	
	
}
