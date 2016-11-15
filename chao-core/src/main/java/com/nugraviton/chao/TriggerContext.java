package com.nugraviton.chao;

import java.util.Date;

/**
 * Context object encapsulating last execution times and last completion time
 * of a given task.
 *
 * @author Juergen Hoeller
 * @since 3.0
 */
public interface TriggerContext {

	/**
	 * @return the last <i>scheduled</i> execution time of the task,
	 * or {@code null} if not scheduled before.
	 * 
	 */
	Date lastScheduledExecutionTime();

	/**
	 * @return the last <i>actual</i> execution time of the task,
	 * or {@code null} if not scheduled before.
	 */
	Date lastActualExecutionTime();

	/**
	 * @return the last completion time of the task,
	 * or {@code null} if not scheduled before.
	 */
	Date lastCompletionTime();

}

