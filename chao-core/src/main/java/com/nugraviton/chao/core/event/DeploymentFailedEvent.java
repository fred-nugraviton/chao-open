package com.nugraviton.chao.core.event;

import com.nugraviton.chao.job.Deployment;

public class DeploymentFailedEvent {

	private final Deployment deployment;
	private final Throwable e;

	public DeploymentFailedEvent(Deployment deployment, Throwable e) {
		this.deployment = deployment;
		this.e = e;
	}

	public Deployment getDeployment() {
		return deployment;
	}
	
	public Throwable getThrowable(){
		return e;
	}

	@Override
	public String toString() {
		return "DeploymentFailedEvent";
	}
	
}
