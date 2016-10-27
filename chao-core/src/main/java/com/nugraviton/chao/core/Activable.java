package com.nugraviton.chao.core;

/**
 * Interface to enable active/inactive control of a component,
 * so the component can activated and deactivated as wish. 
 * 
 * All methods are thread safe.
 * 
 * @author fred.wang@nuGraviton.com
 *
 */
public interface Activable {

	/**
	 * Checks if it is active.
	 * @return boolean -- true if it's active.
	 */
	boolean isActive();

	/**
	 * Puts the component to active status if it's inactive. No effect if it's already active.
	 * 
	 */
	void activate();
	
	/**
	 * Puts the component to inactive if it's active. No effect if it's already inactive.
	 */
	void deactivate();
	
}
