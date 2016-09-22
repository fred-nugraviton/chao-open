package com.nugraviton.chao.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that marks a method to be a {@code FixedRate} task. 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface FixedRate {

	/**
	 * Name of this {@code FixedDelay} task. Default to method name if empty.
	 * 
	 * @return String -- task name.
	 */
	String name() default "";
	
	/**
	 * Job name to which this task belongs.
	 * 
	 * @return String -- job name. 
	 */
	String jobName();
	
	/**
	 * The description of this task. Default to empty string.
	 * 
	 * @return String -- task description.
	 */
	String description() default "";

	/**
	 * Number of milliseconds to delay before the first execution.
	 * Default to 0.
	 * 
	 * @return the initial delay in milliseconds
	 */
	String initialDelay() default "0";
	
	/**
	 * Starts a new session by executing the annotated method with a fixed period between invocations.
	 * 
	 * @return the period in milliseconds
	 */
	String fixedRate();
}
