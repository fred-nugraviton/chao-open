package com.nugraviton.chao.job;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.nugraviton.chao.annotation.Cron;
import com.nugraviton.chao.annotation.FixedDelay;
import com.nugraviton.chao.annotation.FixedRate;
import com.nugraviton.chao.annotation.Task;
import com.nugraviton.chao.annotation.TriggeredBy;
import com.nugraviton.chao.job.rmi.TaskDef;

/**
 * The remote method sends to JobRunner to execute.
 *
 * @author fred.wang@nuGraviton.com
 */
public class TaskDefImpl implements TaskDef{

	private static final long serialVersionUID = 6631604036553063657L;
	
	private String jobName;
	
	protected String taskName;
	protected String taskDesc;
	
	private String methodName;
    private String declaringClass;
    private String[] parameterTypes;    
    private String returnType;
    
	private boolean startTask = true;
	private boolean endTask = true;
	private Boolean springTask = Boolean.FALSE;
	
	private String triggeredByTaskName;
	
	private TaskDef triggeredBy;

	private Annotation annotation;
	
	private List<TaskDef> nextTasks = new ArrayList<>();
	
	private int generation = 0;
    
	public TaskDefImpl(Method method){
        this.methodName = method.getName();
        this.taskName = method.getName();
        Class<?> clazz = method.getDeclaringClass();
        this.declaringClass = clazz.getName();
        
        Class<?>[] paramTypes = method.getParameterTypes();
        String[] paramTypeStrings = new String[paramTypes.length];
		for(int i =0 ; i < paramTypes.length; i++){
			paramTypeStrings[i] = paramTypes[i].getName();
		}
		this.parameterTypes = paramTypeStrings;
        this.returnType = method.getReturnType().getName();
        
        int taskAnnotationCounter = 0;
        
       if (method.isAnnotationPresent(Task.class)) {
    	   Task annotation = method.getAnnotation(Task.class);
    	   	if(!annotation.name().isEmpty()){
       			this.taskName = annotation.name();
    	   	}
    	   	this.annotation = annotation;
    	   	this.taskDesc = annotation.description();
            this.jobName = annotation.jobName();
            taskAnnotationCounter++;
            
        } else if (method.isAnnotationPresent(FixedDelay.class)){
        	FixedDelay annotation = method.getAnnotation(FixedDelay.class);
        	if(!annotation.name().isEmpty()){
        		this.taskName = annotation.name();
        	}
        	this.annotation = annotation;
        	this.taskDesc = annotation.description();
            this.jobName = annotation.jobName();
            taskAnnotationCounter++;
            
        } else if (method.isAnnotationPresent(FixedRate.class)){
        	FixedRate annotation = method.getAnnotation(FixedRate.class);
        	if(!annotation.name().isEmpty()){
        		this.taskName = annotation.name();
        	}
        	this.annotation = annotation;
        	this.taskDesc = annotation.description();
            this.jobName = annotation.jobName();
            taskAnnotationCounter++;
            
        } else if (method.isAnnotationPresent(Cron.class)){
     	   	Cron annotation = method.getAnnotation(Cron.class);
	       	if(!annotation.name().isEmpty()){
	       		this.taskName = annotation.name();
	       	}
	       	this.annotation = annotation;
	       	this.taskDesc = annotation.description();
	       	this.jobName = annotation.jobName();
	       	taskAnnotationCounter++;
       } 
       
		if(taskAnnotationCounter != 1){
			throw new InvalidJobDefinitionException(String.format("invalide task definition for [%s], only one of task can be annotated.", taskName));
		}
		
		if(method.isAnnotationPresent(TriggeredBy.class) && !method.isAnnotationPresent(Task.class)){
			throw new InvalidJobDefinitionException(String.format("invalide task definition for [%s], only @Task can be with @Scheduled and @TriggeredBy", taskName));
		}

       if (method.isAnnotationPresent(TriggeredBy.class)){
       		this.startTask = false;
       		TriggeredBy annotation = method.getAnnotation(TriggeredBy.class);
       		this.triggeredByTaskName = annotation.taskName();
       }
    }

    public String getMethodName() {
		return methodName;
	}

	public String getDeclaringClass() {
		return declaringClass;
	}

	public String[] getParameterTypes() {
		return parameterTypes;
	}

	public String getReturnType() {
		return returnType;
	}

	public String getTaskName() {
		return taskName;
	}

	public String getTaskDesc() {
		return taskDesc;
	}
	
	@Override
	public String getJobName() {
		return this.jobName;
	}

	/**
	 * @return true if it's a springTask
	 */
	public boolean isSpringTask() {
		return this.springTask;
	}
	
	public boolean isStartTask() {
		return this.startTask;
	}
	
	@Override
	public boolean isEndTask() {
		return this.endTask;
	}
	
	@Override
	public Annotation getAnnotation(){
		return this.annotation;
	}
	
	@Override
	public Class<? extends Annotation> getAnnotationType() {
		return this.annotation.annotationType();
	}
	
	@Override
	public List<TaskDef> getNextTasks() {
		return this.nextTasks;
	}
	
	public void addNextTask(TaskDefImpl task){
		this.nextTasks.add(task);
		this.endTask = false;
	}
	
	@Override
	public TaskDef getTriggeredby() {
		return this.triggeredBy;
	}
	
	public void setTriggeredBy(TaskDefImpl upstream){
		upstream.addNextTask(this);
		this.triggeredBy = upstream;
		increaseGeneration(upstream.generation);
	}
	
	private void increaseGeneration(int upStreamGeneration) {
		this.generation = ++upStreamGeneration;
		for(TaskDef downstream: nextTasks){
			((TaskDefImpl)downstream).increaseGeneration(this.generation);
		}
	}

	public String getTriggeredByTaskName(){
		return this.triggeredByTaskName;
	}

	@Override
	public RmiTask getRmiTaskDef(UUID sessionId) {
		
		return  new RmiTask()
				 .withSessionId(sessionId)
				 .withJobName(this.jobName)
				 .withTaskName(this.taskName)
				 .withMethodName(this.methodName)
				 .withDeclaringClass(this.declaringClass)
			     .withParameterTypes(this.parameterTypes)    
			     .withReturnType(this.returnType)
			     .withStartTask(this.startTask)
				 .withEndTask(this.endTask)
				 .withSpringEnabled(this.springTask);
	}

	@Override
	public String toString() {
		return "TaskDefImpl [taskName=" + taskName + ", jobName=" + jobName + ", methodName="
				+ methodName + ", startTask=" + startTask + ", endTask=" + endTask + ", springTask=" + springTask
				+ ", triggeredByTaskName=" + triggeredByTaskName + "]";
	}
	
	
	
}
