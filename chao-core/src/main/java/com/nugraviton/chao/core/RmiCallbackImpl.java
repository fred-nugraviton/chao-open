package com.nugraviton.chao.core;

import java.rmi.RemoteException;

import com.nugraviton.chao.job.AsyncContext;
import com.nugraviton.chao.job.rmi.RmiCallback;

/**
 * Call back when task finished or failed.
 * 
 * @author fred.wang@nuGraviton.com
 *
 */
public class RmiCallbackImpl implements RmiCallback{

	private final EmbeddedCore core;
	
	public RmiCallbackImpl(EmbeddedCore core) {
		this.core = core;
	}

	@Override
	public void notifyTaskCompleted(AsyncContext asyncContext) throws RemoteException {
		core.taskCompleted(asyncContext);
	}

	@Override
	public void notifyTaskFailed(AsyncContext asyncContext) throws RemoteException {
		core.taskFailed(asyncContext);
	}

	@Override
	public void ping() throws RemoteException {
		// leave empty, 
	}
}
