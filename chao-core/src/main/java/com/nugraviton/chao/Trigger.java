package com.nugraviton.chao;

import java.util.Date;


/**
 * Common interface for trigger objects that determine the next execution time
 * of a task that they get associated with.
 *
 * @author Juergen Hoeller
 * @since 3.0
 * @see TaskScheduler#schedule(Runnable, Trigger)
 * @see com.nugraviton.chao.schedule.CronTrigger
 */
public interface Trigger {

	/**
	 * Determine the next execution time according to the given trigger context.
	 * @param triggerContext context object encapsulating last execution times
	 * and last completion time
	 * @return the next execution time as defined by the trigger,
	 * or {@code null} if the trigger won't fire anymore
	 */
	Date nextExecutionTime(TriggerContext triggerContext);

}

