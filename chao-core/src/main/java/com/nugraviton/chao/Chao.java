package com.nugraviton.chao;

import javax.annotation.concurrent.ThreadSafe;

/**
 * The sole interface that users interact with Chao.
 * It provides life cycle control of Chao and {@link Job}s.
 * <pre>
 * {@code
 * 	{@literal @}Job(name = "hello-world")
 *	public class HellloWorld {
 *		
 *	  public static void main(String[] args) throws InterruptedException{
 *			Chao chao = Chao.create();
 *			chao.start();
 *			Thread.sleep(16000);
 *			chao.shutdown();
 *			chao.awaitTermination();
 *	  }
 *		
 *	  {@literal @}Cron(name ="starter-task", jobName = "hello-world", cron="**&#47;5 * * * * *", description="every 5 seconds")
 *	  public void helloWorld() throws InterruptedException {
 *	 	//assuming the task takes 6 seconds.
 *		Thread.sleep(6000);
 *		System.out.println("Hello world! - start task");
 *	  }
 *
 *	  {@literal @}TriggeredBy(taskName="starter-task")
 *	  {@literal @}Task(name ="second-task", jobName = "hello-world", description="a task triggered by [starter-task]")
 *	  public void trailerTask() throws InterruptedException {
 *		//assuming the task takes 5 seconds.
 *		Thread.sleep(5000);
 *		System.out.println("Hello world! - second task");
 *	  }
 *	}
 *
 * </code>
 * </pre>
 *  
 * @author fred.wang@nuGraviton.com
 *
 */
@ThreadSafe
public interface Chao {
	
	/**
	 * Constructs a Chao instance. All class paths are scanned to load jobs
	 * when {@link #start()} is called. 
	 * <p>
	 * This is a convenient way if your class path are not too big. {@link #create(String...)}
	 * is preferable.
	 * <p>
	 * There is only one instance allowed at any time. Once an instance is created, 
	 * you cannot create another one until it is terminated.
	 *  
	 * Once it is terminated by calling {@link #shutdown()} or {@link #shutdownNow()},  
	 * you can create another one by calling {@link #create()} or {@link #create(String...)}.
	 * 
	 * @return a Chao instance.
	 */
	static Chao create(){
		return new Embedded();
	}
	
	/**
	 * Preferred way to create Chao instance. All the given packages will be scanned 
	 * to load jobs when {@link #start()} is called.
	 *  
	 * There is only one instance allowed at any time. Once an instance is created, 
	 * you cannot create another one until it is terminated.
	 *  
	 * Once it is terminated by calling {@link #shutdown()} or {@link #shutdownNow()},  
	 * you can create another one by calling {@link #create()} or {@link #create(String...)}.
	 * 
	 * 
	 * @param packages -- packages to scan.
	 * @return a Chao instance.
	*/
	static Chao create(String... packages){
		return new Embedded(packages);
	}
	
	/**
	 * Starts Chao, loads all {@link Job}s from class paths and
	 * puts all of them in active status.
	 * No effect if it's already started.
	 * 
	 * @throws IllegalStateException if it has been shut down.
	 */
	void start();
	
	/**
	 * Shuts down Chao and puts all {@link Job}s in inactive status.
	 * No new session is started; will wait for all present running sessions to complete.
	 * 	
	 * Once this is called, the Chao instance cannot be restart and should be thrown away.
	 * 
	 * No effect if it's already shut down.    
	 */
	void shutdown();
	
	/**
	 * Shuts down Chao, puts all {@link Job}s in inactive status and
	 * terminates all present running sessions immediately.
	 * 	
	 * Once this is called, the Chao instance cannot be restart and should be thrown away.
	 * 
	 * No effect if it's already shut down.    
	 */
	void shutdownNow();
	
	/**
	 * Checks if Chao is shut down. Calling {@link #shutdown()} 
	 * or {@link #shutdownNow()} puts Chao to shut down status.
	 * 
	 * @return true if it is shut down, otherwise false.
	 */

	boolean isShutdown();

	/**
	 * Checks if Chao is terminated which means Chao is shut down and no running sessions. 
	 * Calling {@link #shutdownNow()} terminates all running sessions and puts Chao to terminated immediately.
	 * Calling {@link #shutdown()}  makes Chao to wait for all running sessions to complete, then puts itself 
	 * to terminated status. 
	 * 
	 * @return true if it is terminated, otherwise false.
	 */
	boolean isTerminated();

	/**
	 * Causes current thread to wait until getting notified by Chao 
	 * when it's terminated.
	 * 
	 */
	void awaitTermination();

	/**
	 * Puts the job to active status if it's inactive;
	 * no effect if it's already active.
	 * 
	 * @param jobName -- the name of a job.
	 * @throws IllegalStateException if Chao is not started or shut down.
	 * @throws illegalargumentexception if job does not exists.
	 */
	void activateJob(String jobName);
	
	/**
	 * Puts the job to inactive status if it's active; 
	 * no effect if it's already inactive.
	 * All present running sessions of the job will keep running till complete, 
	 * no new sessions can be started.
	 * 
	 * An inactive job can be activated by calling {@link #activateJob(String)}.
	 *   
	 * @param jobName -- the name of a job
	 * @throws IllegalStateException if Chao is not started or shut down.
	 * @throws IllegalArgumentException if job does not exists.
	 */
	
	void deactivateJob(String jobName);
}