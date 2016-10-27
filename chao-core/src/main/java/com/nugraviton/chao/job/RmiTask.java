package com.nugraviton.chao.job;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.UUID;

/**
 * A Task that container uses to communicate with process. {@link RmiTask}.
 * 
 * @author fred.wang@nuGraviton.com
 */
public class RmiTask implements Serializable {

	private static final long serialVersionUID = -5902878828594799587L;
	
	private String jobName;
	private String taskName;
	private String methodName;
    private String declaringClass;
    private String[] parameterTypes;    
    private String returnType;
	private boolean startTask;
	private boolean endTask;
	private boolean springEnabled;
	private UUID sessionId;

	public String getJobName() {
		return jobName;
	}
	
	RmiTask withJobName(String jobName){
		this.jobName = jobName;
		return this;
	}
    
	public String getTaskName() {
		return taskName;
	}
	
	RmiTask withTaskName(String taskName) {
		this.taskName = taskName;
		return this;
	}
	
	public String getMethodName() {
		return methodName;
	}
	
	RmiTask withMethodName(String methodName) {
		this.methodName = methodName;
		return this;
	}
	
	public String getDeclaringClass() {
		return declaringClass;
	}
	
	RmiTask withDeclaringClass(String declaringClass) {
		this.declaringClass = declaringClass;
		return this;
	}
	
	public String[] getParameterTypes() {
		return parameterTypes;
	}
	
	
	RmiTask withParameterTypes(String[] parameterTypes) {
		this.parameterTypes = parameterTypes;
		return this;
	}
	
	
	public String getReturnType() {
		return returnType;
	}
	
	RmiTask withReturnType(String returnType) {
		this.returnType = returnType;
		return this;
	}

	public boolean isStartTask() {
		return this.startTask;
	}
	
	RmiTask withStartTask(boolean startTask) {
		this.startTask = startTask;
		return this;
	}
	
	public boolean isEndTask() {
		return this.endTask;
	}
	
	RmiTask withEndTask(boolean endTask) {
		this.endTask = endTask;
		return this;
	}
	
	public boolean isSpringEnabled() {
		return this.springEnabled;
	}
	
	RmiTask withSpringEnabled(boolean springEnabled) {
		this.springEnabled = springEnabled;
		return this;
	}
	
	RmiTask withSessionId(UUID sessionId){
		this.sessionId = sessionId;
		return this;
	}
	
	public UUID getSessionId(){
		return this.sessionId;
	}

	@Override
	public String toString() {
		return "RmiTask [jobName=" + jobName + ", taskName=" + taskName + ", methodName=" + methodName
				+ ", declaringClass=" + declaringClass + ", parameterTypes=" + Arrays.toString(parameterTypes)
				+ ", returnType=" + returnType + ", startTask=" + startTask + ", endTask=" + endTask
				+ ", springEnabled=" + springEnabled + ", sessionId=" + sessionId + "]";
	}

	public Method getMethod() {
		try{
    		Class<?>[] parameters = new Class<?>[parameterTypes.length];
    		
    		for(int i = 0; i < parameterTypes.length; i++){
    			parameters[i] = Class.forName(parameterTypes[i]);
    		}
    		return  Class.forName(declaringClass).getMethod(methodName, parameters);
    		
    	} catch (Exception e) {
    		throw new RuntimeException(String.format("Error deserializing method '%s.%s'", declaringClass, methodName), e);
    	}
	}
}
