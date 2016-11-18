package com.nugraviton.chao.event;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method as a listener to listen to a {@code Event}
 * The method must have signature as below:
 * 
 *  void anyMethodName(Event event).
 *  
 * @author fred.wang@nuGraviton.com
 */
@Documented 
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventListener {

}
