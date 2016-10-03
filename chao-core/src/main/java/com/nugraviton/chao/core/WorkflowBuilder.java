package com.nugraviton.chao.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.nugraviton.chao.annotation.Cron;
import com.nugraviton.chao.annotation.FixedDelay;
import com.nugraviton.chao.annotation.FixedRate;
import com.nugraviton.chao.annotation.Task;
import com.nugraviton.chao.job.Deployment;
import com.nugraviton.chao.job.InvalidJobDefinitionException;
import com.nugraviton.chao.job.DefaultJobDef;
import com.nugraviton.chao.job.Workflow;
import com.nugraviton.chao.job.WorkflowImpl;

/**
 * Build work flows.
 * 
 * @author fred.wang@nuGraviton.com
 *
 */
public class WorkflowBuilder {
	
	private Set<Class<?>> jobConfs;
	
	private final Map<Class<?>,  Set<Method>> taskMethods = new HashMap<>();
	
	public WorkflowBuilder withJobConfs(Set<Class<?>> jobConfs){
		this.jobConfs = jobConfs;
		return this;
	}; 
	
	public WorkflowBuilder withTasks(Set<Method> taskMethods, Class<? extends Annotation> annotationType){
		this.taskMethods.put(annotationType, taskMethods);
		return this;
	}
	
	public Map<String, Workflow> build(){
		
		return doBuild(null);
	}

	public Map<String, Workflow> build(Deployment dinfo) {
		return doBuild(dinfo);
	}
	
	
	private Map<String, Workflow> doBuild(Deployment dinfo) {
		
		Map<String, Workflow> wfs = new HashMap<>();
		
		for(Class<?> jobConf: jobConfs){
			DefaultJobDef jobConfinfo = new DefaultJobDef(jobConf, dinfo);
			WorkflowImpl wf = new WorkflowImpl(jobConfinfo);
			
			if(wfs.containsKey(wf.getJobName())){
				throw new InvalidJobDefinitionException(String.format("duplicated job name [%s]", wf.getJobName()));
			}
			wfs.put(wf.getJobName(), wf);
		}
		
		Set<Method> fixedDelays = taskMethods.get(FixedDelay.class);
		for(Method method : fixedDelays){
			FixedDelay fixedDelay = method.getAnnotation(FixedDelay.class);
			WorkflowImpl wf = (WorkflowImpl) wfs.get(fixedDelay.jobName());
			checkJobName(wf, fixedDelay.jobName());
			wf.addTaskDef(method);
		}
		
		Set<Method> fixedRates = taskMethods.get(FixedRate.class);
		for(Method method : fixedRates){
			FixedRate fixedRate = method.getAnnotation(FixedRate.class);
			WorkflowImpl wf = (WorkflowImpl) wfs.get(fixedRate.jobName());
			checkJobName(wf, fixedRate.jobName());
			wf.addTaskDef(method);
		}
		
		Set<Method> crons = taskMethods.get(Cron.class);
		for(Method method : crons){
			Cron cron = method.getAnnotation(Cron.class);
			WorkflowImpl wf = (WorkflowImpl) wfs.get(cron.jobName());
			checkJobName(wf, cron.jobName());
			wf.addTaskDef(method);
		}
		
		Set<Method> tasks = taskMethods.get(Task.class);
		for(Method method : tasks){
			Task task = method.getAnnotation(Task.class);
			WorkflowImpl wf = (WorkflowImpl) wfs.get(task.jobName());
			wf.addTaskDef(method);
		}
		
		for(Workflow wf : wfs.values()){
			if(wf.getTaskDefs().isEmpty()){
				throw new InvalidJobDefinitionException("no task in job : " + wf.getJobName());
			}
			((WorkflowImpl)wf).assignNextTasks(); 
		}
		
		return wfs;
	}

	private void checkJobName(Workflow wf, String jobName) {
		if(null == wf){
			throw new InvalidJobDefinitionException("job not defined : " + jobName);
		}
		
	}

}
