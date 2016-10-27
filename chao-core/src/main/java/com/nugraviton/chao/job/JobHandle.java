package com.nugraviton.chao.job;

import java.util.UUID;

import com.nugraviton.chao.core.TaskExecutionInfo;

/**
 * Wraps a JVM process for a {@link RunningSession}  
 * which uses this to communicate with the process.
 * It must be closed and throw away after a session completed either successfully or failed.
 *  
 * @author fred.wang@nuGraviton.com
 *
 */
public interface JobHandle {
	
	
	/**
	 * The id of this job execution session. 
	 * Always return new instance to prevent from memory leaking.
	 * @return UUID -- id of this job handle.
	 */
	public UUID getJobHandleId();
	
	
	/**
	 * start a task.
	 * @param rmiTask -- task to run.
	 * @return AnyncContext
	 */
	AsyncContext taskStart(RmiTask rmiTask);
	
	/**
	 * The running JVM process can be inspected through this method any time
	 * as long as it's alive. Always return new instance to prevent from memory leaking.
	 * @return TaskExecutionInfo -- the info of this JVM process.
	 */
	public TaskExecutionInfo getExecutionInfo();

	public void close();
	
}
