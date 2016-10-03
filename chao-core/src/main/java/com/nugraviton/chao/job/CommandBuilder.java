package com.nugraviton.chao.job;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.nugraviton.chao.job.ContainerException;
/**
 * To build command string to start a JVM process.
 * After setting all necessary parameters, call newCommand to create a new command.  
 * A command is supposed to be throw away after every use. It's not reusable.
 * 
 * @author fred.wang@nuGraviton.com
 *
 */
class CommandBuilder {
	
	private static String jdwpString = "-agentlib:jdwp=transport=dt_socket,address=127.0.0.1:%d,server=y,suspend=%s";
	
	private String classPath;

	private boolean jdwpEnabled;
	
	private int jdwpPort;
	
	private String suspend; 
	
	private String jobRunnerClass = "com.nugraviton.chao.job.runner.JobRunner";

	private Path appWorkDir;
	
	private int rmiPort = 1099;

	private UUID jobHandleId;

	
	/**
	 * The class to start OS process.
	 * Optional, default is {@code com.nugraviton.chao.job.runner.JobRunner}
	 * @param jobRunnerClassName
	 * @return this command builder
	 */
	CommandBuilder withJobRunnerClass(String jobRunnerClassName){
		Objects.requireNonNull(jobRunnerClassName);
		this.jobRunnerClass = jobRunnerClassName;
		return this;
	}
	
	/**
	 * Mandatory, the path to deployed app word dir.
	 * @param appWorkDir
	 * @return this command builder.
	 */
	CommandBuilder withJobWorkDir(Path appWorkDir){
		Objects.requireNonNull(appWorkDir);
		this.appWorkDir = appWorkDir;
		return this;
	}
	
	/**
	 * The RMI registry port,  used to bind remote objects.
	 * Optional, default is 1099.
	 * @param rmiPort
	 * @return
	 */
	CommandBuilder withRmiPort(Integer rmiPort){
		Objects.requireNonNull(rmiPort);
		this.rmiPort = rmiPort;
		return this;
	}
	
	/**
	 * The class path URLs, there is at least one has to be added.
	 * @param classPath
	 * @return this command builder.
	 */
	CommandBuilder withClassPath(String classPath){
		Objects.requireNonNull(classPath);
		this.classPath = classPath;
		return this;
	}

	/**
	 * Enable remote debug.  Default is disabled.
	 * @param jdwpPort the port the debugger listens to.
	 * @param suspend y/n, y means the process suspends till a remote debugger is attached.
	 * 						n means no suspend.
	 * @return this command builder.
	 */
	CommandBuilder withJdwp(Integer jdwpPort, Boolean suspend){
		Objects.requireNonNull(jdwpPort);
		Objects.requireNonNull(suspend);
		this.jdwpEnabled = true;
		this.jdwpPort = jdwpPort;
		this.suspend = suspend? "y":"n";
		return this;
	}
	
	/**
	 * Create the an executable command with unique ID. 
	 * The command can only be used once. Calling this method to 
	 * create a new one to use.
	 * @return command.
	 */
	List<String> build(){
		
		if(appWorkDir == null){
			throw new ContainerException("app work dir not found, please set");
		}
		
		if(classPath == null){
			throw new ContainerException("class path is empty, please set");
		}
		
		List<String> command = new ArrayList<>();
		command.add("java");
		
		if(jdwpEnabled){
			command.add(String.format(jdwpString, jdwpPort, suspend));
		}
		
		command.add("-classpath");
		command.add(classPath);
		
		command.add(jobRunnerClass);
		command.add(jobHandleId.toString());
		command.add(appWorkDir.toString());
		command.add(String.valueOf(rmiPort));
		
		return command;
	}
	
	void setJobHandleId(UUID executionId) {
		this.jobHandleId=executionId;
	}
}
