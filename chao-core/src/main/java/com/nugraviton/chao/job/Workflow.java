package com.nugraviton.chao.job;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.nugraviton.chao.job.rmi.TaskDef;

/**
 * Job in a structured work flow view.
 *  
 * @author fred.wang@nuGraviton.com
 *
 */
public interface Workflow extends Serializable{

	/**
	 * Get the job meta data.
	 * 
	 * @return JobConfInfo -- wraps mainly what's in the {@link Job} annotation.
	 */
	JobDef	getJobDef();
	
	
	String getJobName();

	/**
	 * Get all task defined for this job.
	 * 
	 * @return a copy of internal tasks.
	 */
	Map<String, TaskDef> getTaskDefs();
	
	/**
	 * Get the start task of this job.
	 * @return TaskDef -- {@link TaskDef}
	 */
	TaskDef getStartTask();
	
	/**
	 * Get a TaskDef by its name.
	 * @param taskName -- String. 
	 * @return TaskDef
	 */
	TaskDef getTaskDef(String taskName);
	
	/**
	 * Get next tasks which should be triggered.
	 * @param String -- taskName.
	 * @return List<TaskDef> -- list of tasks triggered by given task.
	 */
	List<TaskDef> getNextTasks(String taskName);
	
	/**
	 * Clone this.
	 * @return the deep clone of this.
	 */
	Workflow deepCopy();
	
}
