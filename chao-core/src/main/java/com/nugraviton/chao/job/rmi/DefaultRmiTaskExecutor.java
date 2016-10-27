package com.nugraviton.chao.job.rmi;

import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.UUID;

import com.nugraviton.chao.job.AsyncContext;
import com.nugraviton.chao.job.DefaultAsyncContext;
import com.nugraviton.chao.job.RmiTask;

public class DefaultRmiTaskExecutor extends AbstractRmiTaskExecutor {

	public DefaultRmiTaskExecutor(UUID jobHandleId, RmiCallback rmiCallback){
		this.jobHandleId = jobHandleId;
		this.rmiCallback = rmiCallback;
	}
	
	@Override
	public AsyncContext start(RmiTask task) throws RemoteException {
		
		String jobName = task.getJobName();
		String taskName = task.getTaskName();
		UUID sessionId = task.getSessionId();
		UUID executionId = UUID.randomUUID();
		
		DefaultAsyncContext context = new DefaultAsyncContext(jobName, taskName, sessionId, executionId);
			
		Runnable callable = new Runnable() {

			@Override
			public void run() {
				try{
					String className = task.getDeclaringClass();
					Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
					Object instance = clazz.newInstance();
					Method method = task.getMethod();
					@SuppressWarnings("unused")
					Object rtn =method.invoke(instance);
					
					context.setEndTime(LocalDateTime.now());
					rmiCallback.notifyTaskCompleted(context);
				}catch(RemoteException e){
					//the connection to server dead already, exit!
					System.exit(1);
				}catch(Throwable t){
					try {
						context.setEndTime(LocalDateTime.now());
						if(t.getCause() != null){
							t = t.getCause();
						}
						context.setThrowable(t);
						rmiCallback.notifyTaskFailed(context);
					} catch (RemoteException e) {
						//the server dead already, exit!
						System.exit(1);
					}
				}
			}
		};
		
		executor.submit(callable);
			
		return context;
	}

	
}
