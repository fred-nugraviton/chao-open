package com.nugraviton.chao.spi.event;

/**
 * Signals the Chao is going to be shut down gracefully.
 * 
 * @author fred.wang@nuGraviton.com
 *
 */
public class ChaoShutDownEvent {
	
	private final String chaoName;

	public ChaoShutDownEvent(String chaoName){
		this.chaoName = chaoName;
	}

	public String getPayload() {
		return chaoName;
	};
	
	@Override
	public String toString() {
		return "AppStartedEvent [app=" + chaoName + "]";
	}
	
}

