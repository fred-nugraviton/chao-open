package com.nugraviton.chao.core.event;

import java.lang.reflect.Method;

import com.nugraviton.chao.annotation.EventListener;
import com.nugraviton.chao.core.EmbeddedCore;

/**
 * Listens to {@code EventType.JOB_TERMINATED} event, 
 * 
 * @author fred.wang@nuGraviton.com
 *
 */
class JobTerminatedListener {

	@EventListener
	public void listen(JobTerminatedEvent event, EmbeddedCore core) throws Exception {
		Method method = EmbeddedCore.class.getDeclaredMethod("tryNotifyTermination");
		method.setAccessible(true);
		method.invoke(core);
		method.setAccessible(false);
	}

}
