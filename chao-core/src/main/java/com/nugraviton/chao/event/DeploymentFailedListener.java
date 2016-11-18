package com.nugraviton.chao.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nugraviton.chao.Embedded;

/**
 * Listens on {@code ContainerEventType.JOB_DEPLOYMENT} event; 
 * @author fred.wang@nuGraviton.com
 *
 */
class DeploymentFailedListener{
	
	private final static Logger log  = LoggerFactory.getLogger(DeploymentFailedListener.class);
	
	@EventListener
	public void listen(DeploymentFailedEvent event, Embedded core){
		
		log.error("deployment failed", event.getThrowable());
	}
	
}
