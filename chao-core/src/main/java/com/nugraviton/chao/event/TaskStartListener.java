package com.nugraviton.chao.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nugraviton.chao.Embedded;

/**
 * Listens to {@code TaskStartEvent} event;
 * 
 * @author fred.wang@nuGraviton.com
 *
 */
class TaskStartListener{
	
	private static final Logger log = LoggerFactory.getLogger(TaskStartListener.class);

	@EventListener
	public void listen(TaskStartEvent event, Embedded core) {
		if(log.isDebugEnabled()){
			log.debug("{}", event);
		}
		core.taskStart(event.getPayload());
		
	}

}
