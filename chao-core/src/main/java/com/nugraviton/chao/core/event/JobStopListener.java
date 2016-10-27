package com.nugraviton.chao.core.event;

import java.lang.reflect.Method;

import com.nugraviton.chao.annotation.EventListener;
import com.nugraviton.chao.core.EmbeddedCore;

/**
 * 
 * @author fred.wang@nuGraviton.com
 *
 */
class JobStopListener {

	@EventListener
	public void listen(JobStopEvent event, EmbeddedCore core) throws Exception {
		Method stopJob = core.getClass().getDeclaredMethod("stopJob", String.class, String.class);
		stopJob.setAccessible(true);
		stopJob.invoke(core, event.getAppName(), event.getJobName());
		stopJob.setAccessible(false);
	}
	
}
