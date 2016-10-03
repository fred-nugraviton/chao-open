package com.nugraviton.chao.spi.event;

/**
 * Signals the core is going to be shut down gracefully.
 * 
 * @author fred.wang@nuGraviton.com
 *
 * @param String -- name of this core.
 */
public class CoreShutDownEvent {
	
	private final String coreName;

	public CoreShutDownEvent(String coreName){
		this.coreName = coreName;
	}

	public String getPayload() {
		return coreName;
	};
	
	@Override
	public String toString() {
		return "AppStartedEvent [app=" + coreName + "]";
	}
	
}

