package com.nugraviton.chao.job.rmi;

import java.io.Serializable;
import java.rmi.Remote;

public interface RmiProcessInfo extends Remote, Serializable {
	
	/**
	 * Get JVM total Memory in MB
	 * @return total memory in MB
	 */
	Long getTotalMemory();
	
	/**
	 * JVM free memory in MB
	 * @return JVM free memory in MB
	 */
	Long getFreeMemory();
	
	/**
	 * Maximum available memory in MB
	 * @return Maximum available memory in MB
	 */
	Long getMaxMemory();
}
