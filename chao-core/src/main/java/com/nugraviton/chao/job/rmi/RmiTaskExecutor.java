package com.nugraviton.chao.job.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.nugraviton.chao.job.AsyncContext;
import com.nugraviton.chao.job.RmiTask;

/**
 * Executes tasks on a separate JVM.
 * 
 * @author fred.wang@nuGraviton.com
 *
 */
public interface RmiTaskExecutor extends Remote{
	
	/**
	 * Signals process threat this job operator has been binded to RMI registry.
	 */
	public static final String JOB_EXECUTOR_BINDED_SIGNAL = "task executor has been binded to rmi rigistery.";
	
	public static final String JOB_RUNNER_ERROR_SIGNAL="error running JobRunner.";
	
	/**
	 * Start the job described in job definition.
	 * @param taskDef -- the task to execute.
	 * @return AsyncContext -- the async execution context.
	 * @throws RemoteException -- the implementation should not throw any exception.
	 * 							  the {@link RemoteException} is only for the unpredictable exceptions.
	 */
	public AsyncContext start(RmiTask taskDef) throws RemoteException;
	
	
	/**
	 * Shut down this JVM immediately by calling System.exit(int);
	 * Intended to terminates this job.
	 *   
	 * @throws RemoteException
	 */
	public void exit(int status) throws RemoteException;
	
	/**
	 * Take a snapshot of currently JVM status.
	 * 
	 * @return RmiProcessInfo -- JVM info.
	 * @throws RemoteException
	 */
	public RmiProcessInfo getStatus() throws RemoteException;
	
}
