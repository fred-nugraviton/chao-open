package com.nugraviton.chao.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * TODO: more document on how to use this annotation.
 * 
 * Indicates this class is a job configuration class.
 * 
 * @author fred.wang@nuGraviton.com
 *
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Job {
	
	/**
	 * Job name. Default to fully qualified name of the class 
	 * on which is annotated.
	 * 
	 * @return String -- Name of this job.
	 */
	String name() default "";
	
	/**
	 * Job description. Default empty string.
	 * @return String -- job description.
	 */
	String desc() default "";
	
	/**
	 * The max number of job sessions this job can have in parallel, 
	 * the scheduled sessions are ignored when max number is reached.
	 * Default {@code Integer.MAX_VALUE}. Zero means the job cannot be run at all.
	 * 
	 * @return int -- max number of session this job can run in parallel.
	 */
	int maxSession() default Integer.MAX_VALUE;
	
	/**
	 * Path of properties files which contain configuration values.
	 * The path is relative to class path root.
	 * 
	 * TODO: more document on how this properties file is used.
	 * 
	 * @return String -- path of properties file like conf/config.properties
	 */
	String[] properties() default "";
}
