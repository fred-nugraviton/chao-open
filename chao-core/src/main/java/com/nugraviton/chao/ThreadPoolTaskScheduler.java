package com.nugraviton.chao;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * The core scheduler. 
 * 
 * @author fred.wang@nuGraviton.com
 *
 */
public class ThreadPoolTaskScheduler implements TaskScheduler {

	private final ScheduledThreadPoolExecutor scheduledExecutor;

	ThreadPoolTaskScheduler(){
		
		ThreadFactory threadFactory = new ChaoThreadFactory("chao", "chao-", true); 
		this.scheduledExecutor = (ScheduledThreadPoolExecutor)Executors.newScheduledThreadPool(3, threadFactory);
		this.scheduledExecutor.setRemoveOnCancelPolicy(true);
		scheduledExecutor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
			
			@Override
			public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
				
			}
		});
	}
	/**
	 * Set the ScheduledExecutorService's pool size.
	 * Default is 2.
	 * <p><b>This setting can be modified at runtime, for example through JMX.</b>
	 * @param poolSize -- the size of the thread pool
	 */
	public void setPoolSize(int poolSize) {
		if(poolSize < 1){
			throw new ChaoException("[poolSize] must be 1 or higher");
		}
		this.scheduledExecutor.setCorePoolSize(poolSize);
	}

	/**
	 * Return the underlying ScheduledExecutorService for native access.
	 * @return the underlying ScheduledExecutorService (never {@code null})
	 * @throws IllegalStateException if the ThreadPoolTaskScheduler hasn't been initialized yet
	 */
	public ScheduledExecutorService getScheduledExecutor() throws IllegalStateException {
		return this.scheduledExecutor;
	}

	/**
	 * Return the current pool size.
	 * <p>Requires an underlying {@link ScheduledThreadPoolExecutor}.
	 * @see java.util.concurrent.ScheduledThreadPoolExecutor#getPoolSize()
	 * @return the size of thread pool
	 */
	public int getPoolSize() {
		return this.scheduledExecutor.getPoolSize();
	}

	
	/**
	 * Return the number of currently active threads.
	 * <p>Requires an underlying {@link ScheduledThreadPoolExecutor}.
	 * @see java.util.concurrent.ScheduledThreadPoolExecutor#getActiveCount()
	 * @return the count of active threads.
	 */
	public int getActiveCount() {
		return this.scheduledExecutor.getActiveCount();
	}


	// SchedulingTaskExecutor implementation
	
	public void execute(Runnable task) {
		this.scheduledExecutor.execute(task);
	}

	public void execute(Runnable task, long startTimeout) {
		execute(task);
	}

	public Future<?> submit(Runnable task) {
		return scheduledExecutor.submit(task);
	}

	public <T> Future<T> submit(Callable<T> task) {
		return scheduledExecutor.submit(task);
	}

	@Override
	public ScheduledFuture<?> schedule(Runnable task, Trigger trigger) {
		return new ReschedulingRunnable(task, trigger, scheduledExecutor).schedule();
	}

	@Override
	public ScheduledFuture<?> schedule(Runnable task, Date startTime) {
		long initialDelay = startTime.getTime() - System.currentTimeMillis();
		return scheduledExecutor.schedule(task, initialDelay, TimeUnit.MILLISECONDS);
	}

	@Override
	public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, Date startTime, long period) {
		long initialDelay = startTime.getTime() - System.currentTimeMillis();
		return scheduledExecutor.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.MILLISECONDS);
	}

	@Override
	public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long period) {
		return scheduledExecutor.scheduleAtFixedRate(task, 0, period, TimeUnit.MILLISECONDS);
	}

	@Override
	public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, Date startTime, long delay) {
		long initialDelay = startTime.getTime() - System.currentTimeMillis();
		return scheduledExecutor.scheduleWithFixedDelay(task, initialDelay, delay, TimeUnit.MILLISECONDS);
	}

	@Override
	public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, long delay) {
		return scheduledExecutor.scheduleWithFixedDelay(task, 0, delay, TimeUnit.MILLISECONDS);
	}
	@Override
	public void shutdown() {
		scheduledExecutor.shutdown();
	}
	
	@Override
	public void shutdownNow() {
		scheduledExecutor.shutdownNow();
	}
	
	
}
