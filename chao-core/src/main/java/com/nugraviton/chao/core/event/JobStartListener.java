package com.nugraviton.chao.core.event;

import java.lang.reflect.Method;

import com.nugraviton.chao.annotation.EventListener;
import com.nugraviton.chao.core.EmbeddedCore;

class JobStartListener {

	@EventListener
	public void listen(JobStartEvent event, EmbeddedCore core) throws Exception {
		Method startJob = core.getClass().getDeclaredMethod("startJob", String.class, String.class);
		startJob.setAccessible(true);
		startJob.invoke(core, event.getAppName(), event.getJobName());
		startJob.setAccessible(false);
	}
}
