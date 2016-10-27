package com.nugraviton.chao.job;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

/**
 * A similar concept to Servlet AsyncContext.
 * All the tasks are executed in async. 
 * An {@code AsyncContext} instance is returned immediately 
 * after a task execution. The job process will callback to 
 * notify completion or failure.
 * 
 *    
 * @author fred.wang@nuGraviton.com
 *
 */
public interface AsyncContext{
	
	/**
	 * Job name.
	 * @return String -- job name;
	 */
	String getJobName();
	
	
	/**
	 * Task name.
	 * @return String -- task name.
	 */
	String getTaskName();

	/**
	 * The id of session in which this task is running.
	 * @return UUID -- the session Id.
	 */
	UUID getSessionId();
	
	/**
	 * The execution id for the task execution.
	 * @return UUID -- execution id.
	 */
	UUID getExecutionId();
	
	/**
	 * The time point on which the task started.
	 * @return {@link Date} -- the task start time.
	 */
	LocalDateTime getStartTime();
	
	/**
	 * Gets task execution end time.
	 * @return LocalDateTime -- task execution end time
	 */
	LocalDateTime getEndTime();
	
	/**
	 * Gets time consumed to execute the task.
	 * @return Long -- milliseconds, null if task not completed.
	 */
	Long getTimeConsumed();

	/**
	 * Indicates if the task execution failed.
	 * @return boolean -- true if it's failed.
	 */
	boolean isFailed();

	/**
	 * Get the throwable of the task execution.
	 * @return Throwable -- null if task not failed.
	 */
	Throwable getThrowable();



}
