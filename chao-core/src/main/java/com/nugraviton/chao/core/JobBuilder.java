package com.nugraviton.chao.core;

import java.lang.reflect.Method;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;

import com.nugraviton.chao.core.event.EventBus;
import com.nugraviton.chao.job.JobContainer;
import com.nugraviton.chao.job.DefaultJobContainer;
import com.nugraviton.chao.job.JobStreamHandler;
import com.nugraviton.chao.job.Workflow;


class JobBuilder {
	
	private Registry registry;
	private JobStreamHandler jobStreamHandler;
	private EventBus eventBus;
	private TaskScheduler taskScheduler;
	private final Map<String, Workflow> wfs  = new HashMap<>();
	
	JobBuilder withWorkflows(Map<String, Workflow> wfs) {
		this.wfs.putAll(wfs);
		return this;
	}

	JobBuilder withStreamHandler(JobStreamHandler jobStreamHandler) {
		this.jobStreamHandler = jobStreamHandler;
		return this;
		
	}

	JobBuilder withRmiRegistry(Registry rmiRegistry) {
		this.registry = rmiRegistry;
		return this;
	}

	JobBuilder withEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
		return this;
	}

	JobBuilder withTaskSchduler(TaskScheduler taskScheduler) {
		this.taskScheduler = taskScheduler;
		return this;
	}

	Map<String, JobContainer> build() throws Exception {
			
		Map<String, JobContainer> jobs = new HashMap<>();
		
		for(Workflow wf : wfs.values()){
				
			//Class<?> jobContainerImplClass = classLoader.loadClass("com.nugraviton.chao.job.JobContainerImpl");
			DefaultJobContainer job = new DefaultJobContainer();
			
			Method setWorkflow = job.getClass().getDeclaredMethod("setWorkflow", Workflow.class);
			setWorkflow.setAccessible(true);
			setWorkflow.invoke(job, wf);
			setWorkflow.setAccessible(false);
			
			Method setStreamHandler = job.getClass().getDeclaredMethod("setStreamHandler", JobStreamHandler.class);
			setStreamHandler.setAccessible(true);
			setStreamHandler.invoke(job, jobStreamHandler);
			setStreamHandler.setAccessible(false);
			
			Method setRmiRegistry = job.getClass().getDeclaredMethod("setRmiRegistry", Registry.class);
			setRmiRegistry.setAccessible(true);
			setRmiRegistry.invoke(job, registry);
			setRmiRegistry.setAccessible(false);
			
			Method setEventBus = job.getClass().getDeclaredMethod("setEventBus", EventBus.class);
			setEventBus.setAccessible(true);
			setEventBus.invoke(job, eventBus);
			setEventBus.setAccessible(false);
			
			Method setJobRegistrar = job.getClass().getDeclaredMethod("setJobRegistrar", TaskScheduler.class);
			setJobRegistrar.setAccessible(true);
			setJobRegistrar.invoke(job, taskScheduler);
			setJobRegistrar.setAccessible(false);
			
			jobs.put(job.getJobName(), job);
		}
		return jobs;
	}
}
