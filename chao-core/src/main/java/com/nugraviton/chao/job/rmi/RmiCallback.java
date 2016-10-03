package com.nugraviton.chao.job.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.nugraviton.chao.job.AsyncContext;

public interface RmiCallback extends Remote{
	
	static final String RMI_CALL_BACK = "rmiCallback";
	
	/**
	 * call back when a task completed successfully.
	 * 
	 * @param asyncContext -- the context this the task was started.
	 * 
	 * @throws RemoteException -- 	all the task exceptions are notified by {@code notifyTaskFailed} method,
	 * 								this exception happens only when there is an environmental issue. 
	 */
	void notifyTaskCompleted(AsyncContext asyncContext) throws RemoteException;
	
	/**
	 * call back when a task execution failed.
	 * @param asyncContext -- the context of this task execution. The throwable is wrapped.
	 * @throws RemoteException -- this exception happens only when there is an environmental issue.
	 */
	void notifyTaskFailed(AsyncContext asyncContext) throws RemoteException;
	
	/**
	 * Running sessions ping server periodically to detect server's liveness, if die, it kills itself immediately. 
	 *
	 * @throws RemoteException -- this exception happens only when there is an environmental issue.
	 */
	void ping() throws RemoteException;

}
