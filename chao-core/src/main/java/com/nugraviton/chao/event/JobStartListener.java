package com.nugraviton.chao.event;

import java.lang.reflect.Method;

import com.nugraviton.chao.Embedded;

class JobStartListener {

	@EventListener
	public void listen(JobStartEvent event, Embedded core) throws Exception {
		Method startJob = core.getClass().getDeclaredMethod("startJob", String.class, String.class);
		startJob.setAccessible(true);
		startJob.invoke(core, event.getAppName(), event.getJobName());
		startJob.setAccessible(false);
	}
}
