package com.nugraviton.chao.core.event;

public class JobStopEvent {
	
	private final String appName;
	private final String jobName;

	public JobStopEvent(String appName, String jobName) {
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
		return "JobStopEvent [appName=" + appName + ", jobName=" + jobName + "]";
	}
}
