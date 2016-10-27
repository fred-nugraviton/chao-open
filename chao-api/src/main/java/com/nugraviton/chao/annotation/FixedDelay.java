package com.nugraviton.chao.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that marks a method to be a {@code FixedDelay} task. 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface FixedDelay {

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
	 * Default to 0. The value must be equal or greater than zero.
	 * 
	 * @return the initial delay in milliseconds
	 */
	String initialDelay() default "0";
	
	/**
	 * Execute the annotated method with a fixed period between the end
	 * of the last invocation and the start of the next. 
	 * The value must be equal or greater than zero.
	 * 
	 * @return the delay in milliseconds
	 */
	String fixedDelay();

}
