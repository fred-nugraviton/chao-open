package com.nugraviton.chao.core.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nugraviton.chao.core.EmbeddedCore;
import com.nugraviton.chao.annotation.EventListener;

/**
 * Listens to {@code TaskStartEvent} event;
 * 
 * @author fred.wang@nuGraviton.com
 *
 */
class TaskStartListener{
	
	private static final Logger log = LoggerFactory.getLogger(TaskStartListener.class);

	@EventListener
	public void listen(TaskStartEvent event, EmbeddedCore core) {
		if(log.isDebugEnabled()){
			log.debug("{}", event);
		}
		core.taskStart(event.getPayload());
		
	}

}
