package com.nugraviton.chao.schedule;

import com.nugraviton.chao.job.RmiTask;

/**
 * Throws when failed to start a task.
 * 
 * @author fred.wang@nuGraviton.com
 *
 */
public class TaskStartException extends RuntimeException {

	private static final long serialVersionUID = -8451458690000664830L;
	private RmiTask taskDef;
	
	public TaskStartException(RmiTask taskDef) {
		this.taskDef=taskDef;
	}
	
	public RmiTask getTaskDef(){
		return taskDef;
	}
}
