package com.nugraviton.chao.core.event;

import java.lang.reflect.Method;

class HandlerDef {
	
	private final Class<?> eventType;
	private final Method method;
	private final Object handler;
	
	HandlerDef(Class<?> eventType, Method method, Object handler) {
		this.eventType = eventType;
		this.method = method;
		this.handler = handler;
	}

	public Class<?> getEventType() {
		return eventType;
	}

	public Method getMethod() {
		return method;
	}

	public Object getHandler() {
		return handler;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((eventType == null) ? 0 : eventType.hashCode());
		result = prime * result + ((handler == null) ? 0 : handler.hashCode());
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HandlerDef other = (HandlerDef) obj;
		if (eventType == null) {
			if (other.eventType != null)
				return false;
		} else if (!eventType.equals(other.eventType))
			return false;
		if (handler == null) {
			if (other.handler != null)
				return false;
		} else if (!handler.equals(other.handler))
			return false;
		if (method == null) {
			if (other.method != null)
				return false;
		} else if (!method.equals(other.method))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Handler [eventType=" + eventType + ", method=" + method + ", handler=" + handler + "]";
	}
	
	
	
}
