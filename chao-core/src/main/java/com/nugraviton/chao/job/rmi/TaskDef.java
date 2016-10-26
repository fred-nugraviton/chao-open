package com.nugraviton.chao.job.rmi;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.UUID;

import com.nugraviton.chao.job.RmiTask;

/**
 * Represents a task.
 * 
 * @author fred.wang@nuGraviton.com
 *
 */
public interface TaskDef extends Serializable {

	public String getJobName();
	public String getTaskName();
	public String getTaskDesc();
	
    public String getMethodName() ;
	public String getDeclaringClass();
	public String[] getParameterTypes();
	public String getReturnType() ;
	
	Annotation getAnnotation();
	public Class<? extends Annotation> getAnnotationType();
	
	public boolean isSpringTask();
	public boolean isStartTask();
	public boolean isEndTask();
	public TaskDef getTriggeredby();
	public List<TaskDef> getNextTasks();

	public RmiTask getRmiTaskDef(UUID sessionId);

}
