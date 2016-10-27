package com.nugraviton.chao.core;

import javax.annotation.concurrent.ThreadSafe;

import com.nugraviton.chao.annotation.Job;

/**
 * The sole interface that users interact with Chao.
 * It provides life cycle control of Chao and {@link Job}s.
 * 
 * <pre>Hello world! example</pre>
 * <pre>
 * 	{@code
 *   man
 * 
 * 
 * 
 * }
 * </pre>
 *  
 * 
 * @author fredwang@nuGraviton.com
 *
 */
@ThreadSafe
public interface Core {
	
	/**
	 * Starts Chao, loads all {@link Job}s from class path and
	 * puts all of them in active status.
	 * No effect if it's already started.
	 * 
	 * @throws IllegalStateException if Chao has been shut down.
	 */
	void start();
	
	/**
	 * Shuts down Chao and puts all {@link Job}s in inactive status.
	 * No new session is started; will wait for all present running sessions to complete.
	 * 	
	 * Once this is called, Chao cannot be restart and should be thrown away.
	 * 
	 * No effect if it's already shut down.    
	 */
	void shutdown();
	
	/**
	 * Shuts down Chao, puts all {@link Job}s in inactive status and
	 * terminates all present running sessions immediately.
	 * 	
	 * Once this is called, Chao cannot be restart and should be thrown away.
	 * 
	 * No effect if it's already shut down.    
	 */
	void shutdownNow();
	
	/**
	 * Checks if Chao is shut down. Calling {@link #shutdown()} 
	 * or {@link #shutdownNow()} puts Chao to shut down status.
	 * 
	 * @return boolean -- true if it is shut down, otherwise false.
	 */

	boolean isShutdown();

	/**
	 * Checks if Chao is terminated which means Chao is shut down and no running sessions. 
	 * Calling {@link #shutdownNow()} terminates all running sessions and puts Chao to terminated immediately.
	 * If {@link #shutdown()} is called, Chao will wait for all running sessions to complete and puts itself 
	 * to terminated status. 
	 * 
	 * @return boolean -- true if it is terminated, otherwise false.
	 */
	boolean isTerminated();

	/**
	 * Causes current thread to wait until getting notified by Chao 
	 * when it's terminated.
	 * 
	 */
	void awaitTermination();

	/**
	 * Puts the job to active status if it's inactive;
	 * not effect if it's already active.
	 * 
	 * @param jobName -- the name of job.
	 */
	void activateJob(String jobName);
	
	/**
	 * Puts the job to inactive status. 
	 * All present running sessions of the job will keep running till complete, 
	 * no new sessions can be started.
	 * 
	 * An inactive job can be activated by calling {@link #activateJob(String)}.
	 *   
	 * @param jobName -- the name of job
	 */
	
	void deactivateJob(String jobName);
}