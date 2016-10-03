package com.nugraviton.chao.job;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nugraviton.chao.annotation.FixedDelay;
import com.nugraviton.chao.annotation.TriggeredBy;
import com.nugraviton.chao.job.rmi.TaskDef;

public class WorkflowImpl implements Workflow {
	
	private static final long serialVersionUID = -935572481005341760L;
	
	private final JobDef jobConf;
	private TaskDef startTaskDef;
	
	// Map<TaskName, TaskDefinition>
	private final Map<String, TaskDef> taskDefs = new HashMap<>();
	
	public WorkflowImpl(JobDef jobConf){
		this.jobConf = jobConf;
	}
	
	@Override
	public JobDef getJobDef() {
		return jobConf;
	}
	
	@Override
	public String getJobName() {
		return jobConf.getJobName();
	}

	@Override
	public Map<String, TaskDef> getTaskDefs() {
		return taskDefs;
	}
	
	@Override
	public TaskDef getTaskDef(String taskName) {
		return taskDefs.get(taskName);
	}

	@Override
	public TaskDef getStartTask() {
		return startTaskDef;
	}

	@Override
	public List<TaskDef> getNextTasks(String taskName) {
		return taskDefs.get(taskName).getNextTasks();
	}

	public void addTaskDef(Method method) {
		
		TaskDef taskDef = new TaskDefImpl(method);
		
		if(startTaskDef != null && !method.isAnnotationPresent(TriggeredBy.class)){
			throw new InvalidJobDefinitionException(String.format("A job can have only one start task, [%s] ", method.getName()));
		}
		
		if(!method.isAnnotationPresent(TriggeredBy.class)){
			startTaskDef = taskDef;
		}
		
		if(method.isAnnotationPresent(FixedDelay.class)){
			this.jobConf.setFixedDelay(true);
		}
		
		if(taskDefs.containsKey(taskDef.getTaskName())){
			throw new InvalidJobDefinitionException(String.format("Duplicated task name [%s]", taskDef.getTaskName()));
		}
		
		String taskName = taskDef.getTaskName();
		taskDefs.put(taskName, taskDef);
	}

	
	public void assignNextTasks() {
		for(TaskDef task : taskDefs.values()){
			TaskDefImpl thisTask = (TaskDefImpl)task;
			if(!thisTask.isStartTask()){
				String upStreamTaskName = ((TaskDefImpl)task).getTriggeredByTaskName();
				TaskDefImpl upStreamTask = (TaskDefImpl)taskDefs.get(upStreamTaskName);
				if(null == upStreamTask){
					throw new InvalidJobDefinitionException(String.format("cannot find upper stream task [%s] for task [%s]", upStreamTaskName, thisTask.taskName));
				}
				thisTask.setTriggeredBy(upStreamTask);
			}
		}
	}
	
	
	public Workflow deepCopy(){
		
		Workflow clone = null;
		try{
	        ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        ObjectOutputStream out = new ObjectOutputStream(bos);
	        out.writeObject(this);
	 
	        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
	        ObjectInputStream in = new ObjectInputStream(bis);
	        clone = (Workflow) in.readObject();
	        bos.close();
	        bis.close();
	        
		}catch(Exception e){
			throw new SerializationException("error serializing TaskDef " + this.getJobName(), e);
		}
        return clone;
	}
	
}
