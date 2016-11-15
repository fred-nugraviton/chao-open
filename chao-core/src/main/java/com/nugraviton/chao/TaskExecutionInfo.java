package com.nugraviton.chao;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * A snapshot info for a running job.
 * @author fred.wang@nuGraviton.com
 *
 */
public interface TaskExecutionInfo {

	String getJobName();
	
	UUID getSessionId();
	
	UUID getExecutionId();
	
	LocalDateTime getStartTime();
	
	Integer getPrograss();
	
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
