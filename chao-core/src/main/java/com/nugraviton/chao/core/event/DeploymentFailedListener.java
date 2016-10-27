package com.nugraviton.chao.core.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nugraviton.chao.annotation.EventListener;
import com.nugraviton.chao.core.EmbeddedCore;

/**
 * Listens on {@code ContainerEventType.JOB_DEPLOYMENT} event; 
 * @author fred.wang@nuGraviton.com
 *
 */
class DeploymentFailedListener{
	
	private final static Logger log  = LoggerFactory.getLogger(DeploymentFailedListener.class);
	
	@EventListener
	public void listen(DeploymentFailedEvent event, EmbeddedCore core){
		
		log.error("deployment failed", event.getThrowable());
	}
	
}
