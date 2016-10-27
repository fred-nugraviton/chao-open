package com.nugraviton.chao.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates the annotated task is triggered by another task. 
 * This only works when used together with other task annotations.
 * 
 * @author fred.wang@nuGraviton.com
 *
 */
@Documented 
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
public @interface TriggeredBy {
	
	/**
	 * The trigger task name.
	 * 
	 * @return String -- the task name that triggers this task.
	 */
	String taskName();
	
}
