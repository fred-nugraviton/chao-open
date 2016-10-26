package com.nugraviton.chao.core;

import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nugraviton.chao.annotation.Cron;
import com.nugraviton.chao.annotation.FixedDelay;
import com.nugraviton.chao.annotation.FixedRate;
import com.nugraviton.chao.common.StringUtils;
import com.nugraviton.chao.core.event.EventBus;
import com.nugraviton.chao.core.event.DefaultTaskEventPayload;
import com.nugraviton.chao.core.event.TaskStartEvent;
import com.nugraviton.chao.job.InvalidJobDefinitionException;
import com.nugraviton.chao.job.rmi.TaskDef;
import com.nugraviton.chao.schedule.CronTrigger;
import com.nugraviton.chao.spi.event.TaskEventPayload;

/**
 * JobContainer uses this to register or cancel scheduled tasks.
 * Each JobContainer has one and only one of this.
 * 
 * @author fred.wang@nuGraviton.com
 *
 */
public class TaskRegistrar {
	
	private static Logger log = LoggerFactory.getLogger(TaskRegistrar.class);
	
	private TaskScheduler taskScheduler;
	
	/**
	 * Map<TaskName, ScheduledFuture>
	 */
	private final Map<String, ScheduledFuture<?>> scheduledFutures = new ConcurrentHashMap<>();

	private final EventBus eventBus;
	
	public TaskRegistrar(EventBus eventBus, TaskScheduler taskScheduler) {
		this.taskScheduler = taskScheduler; 
		this.eventBus = eventBus;
	}

	public void scheduleTask(TaskDef taskDef) {
		if(taskDef.getAnnotationType() == FixedDelay.class){
			processFixedDelay(taskDef);
		}else if(taskDef.getAnnotationType() ==FixedRate.class){
			processFixedRate(taskDef);
		}else if(taskDef.getAnnotationType() ==Cron.class){
			processCron(taskDef);
		}
	}
	
	private void processCron(TaskDef taskDef) {
		
		Cron cron = (Cron) taskDef.getAnnotation();
		
		if(taskDef.getParameterTypes().length > 0){
				throw new InvalidJobDefinitionException("Only no-arg methods can be annotated with @Cron");
		}
		
		
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				String jobName = taskDef.getJobName();
				String taskName = taskDef.getTaskName();
				TaskEventPayload payload = new DefaultTaskEventPayload(jobName, taskName, true);
				eventBus.publish(new TaskStartEvent(payload));
			}
		};
		
		String zoneString = cron.zone();
		String cronString = cron.cron();
		if (StringUtils.hasText(zoneString)) {
			this.scheduledFutures.put(taskDef.getTaskName(), taskScheduler.schedule(runnable, new CronTrigger(cronString, parseTimeZoneString(zoneString))));
		}else {
			this.scheduledFutures.put(taskDef.getTaskName(), taskScheduler.schedule(runnable, new CronTrigger(cronString)));
		}
	}

	public void cancelAllTasks(){
		for(ScheduledFuture<?> future : scheduledFutures.values()){
			future.cancel(false);
		}
		scheduledFutures.clear();
	}
	
	public void cancelTask(TaskEventPayload payload){
		if(log.isTraceEnabled()){
			log.trace("cancelled task {}", payload );
		}
		
		ScheduledFuture<?> future =scheduledFutures.remove(payload.getTaskName());
		if(future != null){
			future.cancel(false);
		}
	}
	
	
	private void processFixedRate(TaskDef taskDef) {
		FixedRate scheduled = (FixedRate) taskDef.getAnnotation();
		
		if(taskDef.getParameterTypes().length > 0){
				throw new InvalidJobDefinitionException("Only no-arg methods can be annotated with @FixDelay");
		}
		
		try{				
			long initialDelay = Long.valueOf(scheduled.initialDelay());
			long fixedRate = Long.valueOf(scheduled.fixedRate());
			
			if(initialDelay < 0){
				throw new InvalidJobDefinitionException("initialDelay cannot be lesser than zero with @FixDelay");
			}
		
			if(fixedRate < 0){
				throw new InvalidJobDefinitionException("fixedRate cannot be lesser than zero with @FixRate");
			}
		
			Runnable runnable = new Runnable() {
				
				@Override
				public void run() {
					String jobName = taskDef.getJobName();
					String taskName = taskDef.getTaskName();
					TaskEventPayload payload = new DefaultTaskEventPayload(jobName, taskName, true);
					eventBus.publish(new TaskStartEvent(payload));
				}
			};
			
			long now = System.currentTimeMillis();
			Date startTime = new Date(now + initialDelay);
			this.scheduledFutures.put(taskDef.getTaskName(), taskScheduler.scheduleAtFixedRate(runnable, startTime, fixedRate));
		
		}catch(NumberFormatException e){
			//fall back to resolve the variable. like ${initialDelay}
			//initialDelayMilli = vairableResolver.resolve(scheduled.initialDelay()); 
			throw new InvalidJobDefinitionException("error when register "+ taskDef, e);
		}
		
	}
	
	private void processFixedDelay(TaskDef taskDef) {
		FixedDelay scheduled = (FixedDelay) taskDef.getAnnotation();
		
		if(taskDef.getParameterTypes().length > 0){
				throw new InvalidJobDefinitionException("Only no-arg methods can be annotated with @FixDelay");
		}
		
		try{				
			long initialDelay = Long.valueOf(scheduled.initialDelay());
			long fixedDelay = Long.valueOf(scheduled.fixedDelay());
			
			if(initialDelay < 0){
				throw new InvalidJobDefinitionException("initialDelay cannot be lesser than zero with @FixDelay");
			}
		
			if(fixedDelay < 0){
				throw new InvalidJobDefinitionException("fixedDelay cannot be lesser than zero with @FixDelay");
			}
		
			Runnable runnable = new Runnable() {
				
				@Override
				public void run() {
					String jobName = taskDef.getJobName();
					String taskName = taskDef.getTaskName();
					TaskEventPayload payload = new DefaultTaskEventPayload(jobName, taskName, true);
					eventBus.publish(new TaskStartEvent(payload));
				}
			};
			
			long now = System.currentTimeMillis();
			Date startTime = new Date(now + initialDelay);
			this.scheduledFutures.put(taskDef.getTaskName(), taskScheduler.scheduleWithFixedDelay(runnable, startTime, fixedDelay));
		
		}catch(NumberFormatException e){
			//fall back to resolve the variable. like ${initialDelay}
			//initialDelayMilli = vairableResolver.resolve(scheduled.initialDelay()); 
			throw new InvalidJobDefinitionException("error when register "+ taskDef);
		}
	}
	
	
	private static TimeZone parseTimeZoneString(String timeZoneString) {
		TimeZone timeZone = TimeZone.getTimeZone(timeZoneString);
		if ("GMT".equals(timeZone.getID()) && !timeZoneString.startsWith("GMT")) {
			// We don't want that GMT fallback...
			throw new InvalidJobDefinitionException("Invalid time zone specification '" + timeZoneString + "'");
		}
		return timeZone;
	}

	/**
	 * Reschedule the task with new session Id.
	 * @param taskDef -- a fixed delay task.
	 */
	public void tryRescheduleFixedDelayJob(TaskDef taskDef) {
		
		try{
			FixedDelay scheduled = (FixedDelay) taskDef.getAnnotation();
			
			if(scheduled == null){
				return;
			}
			
			
			long fixedDelay = -1;
			
			try{
				fixedDelay = Long.valueOf(scheduled.fixedDelay());
			}catch(NumberFormatException e){
				//fall back to resolve the variable. like ${task1.fixedDelay}
				//fixedDelayMilli = vairableResolver.resolve(scheduled.fixedDelay()); 
			}
			
			Runnable runnable = new Runnable() {
				
				@Override
				public void run() {
					String jobName = taskDef.getJobName();
					String taskName = taskDef.getTaskName();
					TaskEventPayload payload = new DefaultTaskEventPayload(jobName, taskName, true);
					eventBus.publish(new TaskStartEvent(payload));
				}
			};
			
			if(log.isDebugEnabled()){
				log.debug("rescheduled {}", taskDef);
			}
			long now = System.currentTimeMillis();
			Date startTime = new Date(now + fixedDelay);
			this.scheduledFutures.put(taskDef.getTaskName(), taskScheduler.scheduleWithFixedDelay(runnable, startTime, fixedDelay));
			
		}catch(Throwable e){
			eventBus.publishExceptionEvent(e);
		}
	}

	public boolean isScheduled(String taskId) {
		return scheduledFutures.containsKey(taskId);
	}


}
