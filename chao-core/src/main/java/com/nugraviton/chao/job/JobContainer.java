package com.nugraviton.chao.job;

import java.util.Map;

import com.nugraviton.chao.Activable;
import com.nugraviton.chao.TaskExecutable;
import com.nugraviton.chao.Terminable;
import com.nugraviton.chao.job.rmi.TaskDef;

/**
 * TODO: document
 * 1. what a jobContainer is,
 * 2. how to configure it,
 * 3. how it behaves with every configuration.
 * 
 * NOTE: some ideas: 
 * 1. start task starts a job process
 * 2. end task exit its process.
 * 3. max number of job process is defined in JobConfiguration.
 * 
 * Container uses this interface to deal with each Job which 
 * is loaded by its own parent-last class loader. So jobs are isolated
 * and cannot see each other. It is kind of simulating OSGI for 
 * memory leak prevention and security reason. 
 * 
 * @author fred.wang@nuGraviton.com
 *
 */
public interface JobContainer extends TaskExecutable, Terminable, Activable{
	
	
	public String getJobName();
	
	/**
	 * Get a copy of all {@code TaskDef}s of this Job.
	 * @return a map of TaskDef.
	 */
	public Map<String, TaskDef> getTaskDefs();
	
	/**
	 * Get configuration info of this job.
	 * @return JobConfInfo -- configuration info 
	 */
	public JobDef getJobConfInfo();

}
