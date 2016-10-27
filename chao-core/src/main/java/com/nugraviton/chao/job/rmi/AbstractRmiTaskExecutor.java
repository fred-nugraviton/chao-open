package com.nugraviton.chao.job.rmi;

import java.rmi.RemoteException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.nugraviton.chao.job.AsyncContext;
import com.nugraviton.chao.job.RmiTask;

public abstract class AbstractRmiTaskExecutor implements RmiTaskExecutor {

	protected static final ExecutorService executor = Executors.newCachedThreadPool();	
	
	private static long MB = 1024 * 1024;

	protected RmiCallback rmiCallback;
	
	protected UUID jobHandleId;
	
	
	
	/**
	 * Called by server to execute a task.
	 * 
	 * @param taskDef -- the task to execute.
	 * @return UUID -- the execution id for this task execution. 
	 */
	@Override
	public abstract AsyncContext start(RmiTask taskDef) throws RemoteException ;
	
	/**
	 * Called by server to exit this process.
	 */
	@Override
	public void exit(int status) throws RemoteException {
		
		System.exit(status);		
	}

	/**
	 * Get the current process status. 
	 */
	@Override
	public RmiProcessInfo getStatus() throws RemoteException {
		
		Runtime instance = Runtime.getRuntime();
		Long totalMemory = instance.totalMemory()/MB;
		Long freeMemory = instance.freeMemory()/MB;
		Long maxMemory = instance.maxMemory()/MB;
		
		return new RmiProcessInfoImpl(totalMemory, freeMemory, maxMemory);
	}
	
}
