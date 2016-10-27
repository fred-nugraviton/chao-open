package com.nugraviton.chao.job.runner;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;

import com.nugraviton.chao.job.rmi.DefaultRmiTaskExecutor;
import com.nugraviton.chao.job.rmi.RmiCallback;
import com.nugraviton.chao.job.rmi.RmiTaskExecutor;

/**
 * 
 * @author fred.wang@nuGravition.com
 *
 */
public class JobRunner {
	
	/**
	 * Load the spring context and run a job.
	 * @param args
	 * args[0] --- executionId
	 * args[1] --- work directory
	 * args[2] --- RMI registery port.
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args){
		
		UUID sessionId = UUID.fromString(args[0]);
		Path appWorkDir = Paths.get(args[1]);
		Integer rmiPort = Integer.valueOf(args[2]);
		
		try{
				
			/*if (System.getSecurityManager() == null) {
	            System.setSecurityManager(new SecurityManager());
	        }*/
			
			Registry registry = LocateRegistry.getRegistry(rmiPort);
			RmiCallback rmiCallback = (RmiCallback)registry.lookup(RmiCallback.RMI_CALL_BACK);
			
			final RmiTaskExecutor taskExecutor = getTaskExecutor(args, sessionId, rmiCallback);
			
	        Remote taskExecutorStub = UnicastRemoteObject.exportObject(taskExecutor, 0);
	        registry.rebind(sessionId.toString(), taskExecutorStub);
	        
	        
	        Runnable unbindTaskExecutor = new Runnable() {
				
				@Override
				public void run() {
					try {
						registry.unbind(sessionId.toString());
						UnicastRemoteObject.unexportObject(taskExecutor, true);
					} catch (RemoteException | NotBoundException e) {
						// ignore
					}
				}
			};
			
			Runtime.getRuntime().addShutdownHook(new Thread(unbindTaskExecutor));
			
	        //must have, signal the client its binded.
	        //System.out cannot be redirected.
	        System.out.println(RmiTaskExecutor.JOB_EXECUTOR_BINDED_SIGNAL);
	        
	        while(true){
	        	Thread.sleep(5000);
	        	rmiCallback.ping();
	        }
        } catch (Throwable e) {
        	e.printStackTrace(System.out);
        	System.out.println(RmiTaskExecutor.JOB_RUNNER_ERROR_SIGNAL);
        	System.exit(1);
        }
		
	}

	private static RmiTaskExecutor getTaskExecutor(String[] args, UUID sessionId, RmiCallback rmiCallback)
			throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException,
			InvocationTargetException {
		RmiTaskExecutor taskExecutor;
		if(args.length > 3){
			String springConfigClassName = args[3];
			Class<?> springTaskExecutorClass = Class.forName("");
			Constructor<?> constructor = springTaskExecutorClass.getDeclaredConstructor(UUID.class, RmiCallback.class, String.class);
			taskExecutor = (RmiTaskExecutor)constructor.newInstance(sessionId, rmiCallback, springConfigClassName);
		}else{
			taskExecutor = new DefaultRmiTaskExecutor(sessionId, rmiCallback);
		}
		return taskExecutor;
	}
}
