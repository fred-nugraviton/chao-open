package com.nugraviton.chao.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TODO: need detailed usage and examples here!
 * Annotation that marks a method as a cron scheduled task. 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface Cron {

	/**
	 * Name of this {@code Cron} task. 
	 * 
	 * @return String -- task name. Default to method name if empty.
	 */
	String name() default "";
	
	/**
	 * Job name to which this task belongs.
	 * 
	 * @return String -- job name. 
	 */
	String jobName();
	
	/**
	 * The description of this task. Optional.
	 * 
	 * @return String -- task description.
	 */
	String description() default "";
	
	/**
	 * A cron-like expression, extending the usual UN*X definition to include
	 * triggers on the second as well as minute, hour, day of month, month
	 * and day of week.  e.g. {@code "0 * * * * MON-FRI"} means once per minute on
	 * weekdays (at the top of the minute - the 0th second).
	 * 
	 * @return String - cron expression.
	 */
	String cron();

	/**
	 * A time zone for which the cron expression will be resolved. By default, this
	 * attribute is the empty String (i.e. the server's local time zone will be used).
	 * 
	 * @return a zone id accepted by {@link java.util.TimeZone#getTimeZone(String)},
	 * or an empty String to indicate the server's default time zone
	 * 
	 * @see java.util.TimeZone
	 */
	String zone() default "";
	
}
