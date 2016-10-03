package com.nugraviton.chao.spi.event;

/**
 * Signals the core is going to be shut down gracefully.
 * 
 * @author fred.wang@nuGraviton.com
 *
 * @param String -- name of this core.
 */
public class ExceptionEvent {
	
	private final Throwable throwable;

	public ExceptionEvent(Throwable throwable){
		this.throwable = throwable;
	}

	public Throwable getThrowable() {
		return throwable;
	}

	@Override
	public String toString() {
		throwable.printStackTrace();
		return "";
	};
	
	
}

