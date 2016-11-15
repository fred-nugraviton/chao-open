package com.nugraviton.chao.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nugraviton.chao.spi.event.ExceptionEvent;
import com.nugraviton.chao.Embedded;
import com.nugraviton.chao.annotation.EventListener;

/**
 * Listens on {@code ExceptionEvent} event;
 * 
 * @author fred.wang@nuGraviton.com
 *
 */
public class ExceptionListener{
	
	private static final Logger log = LoggerFactory.getLogger(ExceptionListener.class);


	@EventListener
	public void listen(ExceptionEvent event, Embedded core) {
		/**
		 * TODO: in the future we may persist to DB or push the error message to front end.
		 */
		log.error("error ", event.getThrowable());
	}

}
