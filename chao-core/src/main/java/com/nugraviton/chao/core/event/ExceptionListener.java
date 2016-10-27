package com.nugraviton.chao.core.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nugraviton.chao.spi.event.ExceptionEvent;
import com.nugraviton.chao.annotation.EventListener;
import com.nugraviton.chao.core.EmbeddedCore;

/**
 * Listens on {@code ExceptionEvent} event;
 * 
 * @author fred.wang@nuGraviton.com
 *
 */
public class ExceptionListener{
	
	private static final Logger log = LoggerFactory.getLogger(ExceptionListener.class);


	@EventListener
	public void listen(ExceptionEvent event, EmbeddedCore core) {
		/**
		 * TODO: in the future we may persist to DB or push the error message to front end.
		 */
		log.error("error ", event.getThrowable());
	}

}
