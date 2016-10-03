package com.nugraviton.chao.core.event;

public class JobStartEvent {
	
	private final String appName;
	private final String jobName;

	public JobStartEvent(String appName, String jobName) {
		this.appName = appName;
		this.jobName = jobName;
	}

	
	public String getAppName() {
		return this.appName;
	}

	public String getJobName() {
		return jobName;
	}


	@Override
	public String toString() {
		return "JobStartEvent [appName=" + appName + ", jobName=" + jobName + "]";
	}
	
}
