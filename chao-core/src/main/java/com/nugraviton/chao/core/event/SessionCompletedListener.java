package com.nugraviton.chao.core.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import com.nugraviton.chao.annotation.EventListener;
import com.nugraviton.chao.core.EmbeddedCore;

public class SessionCompletedListener {

	@EventListener
	public void listen(SessionCompletedEvent event, EmbeddedCore core) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Method method = EmbeddedCore.class.getDeclaredMethod("deregisterStream", UUID.class);
		method.setAccessible(true);
		method.invoke(core, event.getSessionId());
		method.setAccessible(false);
	}
}
