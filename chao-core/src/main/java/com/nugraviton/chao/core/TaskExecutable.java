package com.nugraviton.chao.core;

import com.nugraviton.chao.job.AsyncContext;
import com.nugraviton.chao.spi.event.TaskEventPayload;

/**
 * The interface to execute tasks.
 * 
 * 
 * @author fred.wang@nuGraviton.com
 *
 */
public interface TaskExecutable {
	
	/**
	 * The generic method to execute any task.
	 * The task is always returned with a {@link AsyncContext}.
	 * 
	 * 
	 * @param taskEventPayload --  a run for a task.
	 */
	void taskStart(TaskEventPayload taskEventPayload);
	
	/**
	 * Called by job runner when a task completed successfully.
	 * 
	 * @param asyncContext -- the same {@link AsyncContext} which started this task.
	 */
	void taskCompleted(AsyncContext asyncContext);
	
	/**
	 * Called by job runner when a task failed.
	 * 
	 * @param asyncContext -- the same {@link AsyncContext} which started this task.
	 */
	void taskFailed(AsyncContext asyncContext);
	
	/**
	 * Terminate the session immediately. 
	 * 
	 * @param payload -- paylaod with info for terminating the seesion.
	 */
	void terminateSession(TaskEventPayload payload);

}
