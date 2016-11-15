package com.nugraviton.chao.event;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.adapters.ChaoMetaDataAdapter;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.vfs.ChaoUrlType;
import org.reflections.vfs.Vfs;

import com.nugraviton.chao.Embedded;
import com.nugraviton.chao.WorkflowBuilder;
import com.nugraviton.chao.annotation.Cron;
import com.nugraviton.chao.annotation.FixedDelay;
import com.nugraviton.chao.annotation.FixedRate;
import com.nugraviton.chao.annotation.Job;
import com.nugraviton.chao.annotation.EventListener;
import com.nugraviton.chao.annotation.Task;
import com.nugraviton.chao.job.Deployment;
import com.nugraviton.chao.job.Workflow;

/**
 * Listens to {@code DeploymentEvent}.
 * 
 * @author fred.wang@nuGraviton.com
 *
 */
class DeploymentListener{

	@EventListener
	public void listen(DeploymentEvent event, Embedded chao) throws IOException{
		
		Deployment dinfo = event;
		
		try{
			
			WorkflowBuilder wfBuilder = new WorkflowBuilder();
			
			Vfs.addDefaultURLTypes(new ChaoUrlType());
			ConfigurationBuilder confBuilder = 
					new ConfigurationBuilder()
					.setMetadataAdapter(new ChaoMetaDataAdapter())
					.setScanners(new MethodAnnotationsScanner(), new TypeAnnotationsScanner(), new SubTypesScanner())
					.addUrls(dinfo.getScannerUrls());
					//.addClassLoader(this.getClass().getClassLoader());
			
			
			
			Reflections ref = new Reflections(confBuilder);
			Set<Class<?>> jobConfs = ref.getTypesAnnotatedWith(Job.class);
			Set<Method> jobMethods = ref.getMethodsAnnotatedWith(Task.class);
			Set<Method> fixedRateMethods = ref.getMethodsAnnotatedWith(FixedRate.class);
			Set<Method> fixedDelayMethods = ref.getMethodsAnnotatedWith(FixedDelay.class);
			Set<Method> cronMethods = ref.getMethodsAnnotatedWith(Cron.class);
			
			wfBuilder
			.withJobConfs(jobConfs)
			.withTasks(jobMethods, Task.class)
			.withTasks(fixedRateMethods, FixedRate.class)
			.withTasks(fixedDelayMethods, FixedDelay.class)
			.withTasks(cronMethods, Cron.class);
			
			Map<String, Workflow> wfs = wfBuilder.build(dinfo);
			
			Method deployJob = chao.getClass().getDeclaredMethod("deployJobs", Map.class);
			deployJob.setAccessible(true);
			deployJob.invoke(chao, wfs);
			deployJob.setAccessible(false);
			
		}catch(Throwable e){
			chao.publishExceptionEvent(e);
		}
	}
	
}
