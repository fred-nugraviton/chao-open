package com.nugraviton.chao.core.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nugraviton.chao.annotation.EventListener;
import com.nugraviton.chao.core.CoreException;
import com.nugraviton.chao.core.TaskScheduler;
import com.nugraviton.chao.core.EmbeddedCore;
import com.nugraviton.chao.spi.event.ExceptionEvent;

/**
 * A simple EventBus to dispatch {@code Event}s.
 * It's the center of event-driven piece.
 * @author fred.wang@nuGraviton.com
 *
 */
public class EventBus implements Runnable{
	 
	private final static Logger log = LoggerFactory.getLogger(EventBus.class);
	
	private final TaskScheduler taskScheduler;
	private final EmbeddedCore core;
	
	private ReadWriteLock rwl = new ReentrantReadWriteLock();
	
	private Map<Class<?>, List<HandlerDef>> handlerMap = new HashMap<>();
	private BlockingQueue<Object> eventQueue = new LinkedBlockingQueue<>();

	public EventBus(TaskScheduler taskScheduler, EmbeddedCore core){
		this.taskScheduler = taskScheduler;
		this.core = core;
		this.register(new DeploymentListener());
		this.register(new ExceptionListener());
		this.register(new DeploymentFailedListener());
		this.register(new JobStartListener());
		this.register(new JobStopListener());
		this.register(new JobTerminatedListener());
		this.register(new TaskStartListener());
		this.register(new SessionCompletedListener());
	}
	
	public void publish(Object event){
		try {
			eventQueue.put(event);
		} catch (InterruptedException e) {
			throw new CoreException(String.format("error publishing %s to EventBus", event), e);
		}
	}
	
	public void register(Object handler){
	
		rwl.writeLock().lock();
		try{
			List<HandlerDef> handlerDefs = getEventType(handler);
			for(HandlerDef def : handlerDefs ){
				if(log.isTraceEnabled()){
					log.trace("registered handler - {}", def);
				}
				if(!handlerMap.containsKey(def.getEventType())){
					List<HandlerDef> handlers = new ArrayList<>();
					handlers.add(def);
					handlerMap.put(def.getEventType(), handlers);
				}else{
					List<HandlerDef> handlers = handlerMap.get(def.getEventType());
					handlers.add(def);
				}
			}
		}catch(Exception e){
			publishExceptionEvent(e);
		}finally{
			rwl.writeLock().unlock();
		}
	}
	

	public void deregister(Object handler) {
		
		rwl.writeLock().lock();
		try {
			List<HandlerDef> handlerDefs = getEventType(handler);
			for(HandlerDef def : handlerDefs ){
				if(handlerMap.containsKey(def.getEventType())){
					List<HandlerDef> remainedhandlers = new ArrayList<>();
					List<HandlerDef> handlers = handlerMap.get(def.getEventType());
					for(HandlerDef handlerDef : handlers){
						if(!handler.equals(handlerDef)){
							remainedhandlers.add(handlerDef);
						}else if(log.isTraceEnabled()){
							log.trace("deregistered handler - {}", handlerDef);
						}
					}
					handlerMap.put(def.getEventType(), remainedhandlers);
				}
			}
		} catch (SecurityException e) {
			publishExceptionEvent(e);
		}finally{
			rwl.writeLock().unlock();
		}
	}

	
	/**
	 * Dispatch events to handlers. 
	 */
	@Override
	public void run() {
		
		while(true){
			try {
				
				Object event = eventQueue.take();
				if(log.isTraceEnabled()){
					log.trace("received  {}", event);
				}
				
				rwl.readLock().lock();
				List<HandlerDef> handlerDefs = handlerMap.get(event.getClass());
				
				if(handlerDefs == null || handlerDefs.isEmpty()){
					if(log.isTraceEnabled()){
						log.trace("unhandled {}", event);
					}
					continue;
				}
				
				for(HandlerDef handlerDef : handlerDefs){
					
					Runnable task = new Runnable() {
						
						@Override
						public void run() {
							try {
								handlerDef.getMethod().invoke(handlerDef.getHandler(), event, core);
								if(log.isTraceEnabled()){
									log.trace("called {}", handlerDef);
								}
							} catch (Throwable e) {
								
								if(e.getCause() != null){
									e = e.getCause();
								}
								
								publishExceptionEvent(e);
							}
						}
					};
					taskScheduler.execute(task);	
				}
				
			} catch (Throwable e) {
				publishExceptionEvent(e);
			}finally{
				rwl.readLock().unlock();
			}
		}
	}

	/**
	 * A convenient method to publish an exception event.
	 * @param e
	 */
	public void publishExceptionEvent(Throwable e) {
		ExceptionEvent errorEvent = new ExceptionEvent(e);
		publish(errorEvent);
	}
	
	private List<HandlerDef> getEventType(Object handler) {
		
		List<HandlerDef> handlers = new ArrayList<>();
		
		Method[] methods = handler.getClass().getDeclaredMethods();
		for(Method method : methods){
			if(method.isAnnotationPresent(EventListener.class)){
				Class<?>[] clazz = method.getParameterTypes();
				if(clazz.length == 2 && EmbeddedCore.class ==clazz[1]){
					handlers.add(new HandlerDef(clazz[0], method, handler)); 
				}else{
					String msg = String.format("invalid event listener method in %s", method.getName());
					throw new IllegalArgumentException(msg);
				}
			}
		}
		if(handlers.isEmpty()){
			String msg = String.format("Cannot find valid listener method in %s", handler.getClass().getName());
			throw new IllegalArgumentException(msg);
		}
		
		return handlers;
	}
	
}
