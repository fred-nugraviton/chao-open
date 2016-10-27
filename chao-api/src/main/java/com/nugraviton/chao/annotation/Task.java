package com.nugraviton.chao.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TODO: in the future tasks can be configured to retry multiple times.
 * 
 * Indicates this class is a task.
 * @author fred.wang@nuGraviton.com
 */
@Documented 
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Task {

	/**
	 * Task name. Default to the method name.
	 * 
	 * @return String -- name of this task.
	 */
	String name() default "";
	
	/**
	 * Job name to which this task belongs.
	 * 
	 * @return String -- job name of this task. 
	 */
	String jobName();
	
	/**
	 * Description of this task. Default to empty string.
	 * 
	 * @return String -- Description of this task.
	 */
	String description() default "";

}
