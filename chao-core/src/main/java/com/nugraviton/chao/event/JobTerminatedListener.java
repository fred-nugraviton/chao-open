package com.nugraviton.chao.event;

import java.lang.reflect.Method;

import com.nugraviton.chao.Embedded;

/**
 * Listens to {@code EventType.JOB_TERMINATED} event, 
 * 
 * @author fred.wang@nuGraviton.com
 *
 */
class JobTerminatedListener {

	@EventListener
	public void listen(JobTerminatedEvent event, Embedded core) throws Exception {
		Method method = Embedded.class.getDeclaredMethod("tryNotifyTermination");
		method.setAccessible(true);
		method.invoke(core);
		method.setAccessible(false);
	}

}
