package com.nugraviton.chao.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import com.nugraviton.chao.Embedded;
import com.nugraviton.chao.annotation.EventListener;

public class SessionCompletedListener {

	@EventListener
	public void listen(SessionCompletedEvent event, Embedded core) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Method method = Embedded.class.getDeclaredMethod("deregisterStream", UUID.class);
		method.setAccessible(true);
		method.invoke(core, event.getSessionId());
		method.setAccessible(false);
	}
}
