package com.nugraviton.chao.core;

import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Internal adapter that reschedules an underlying {@link Runnable} according
 * to the next execution time suggested by a given {@link Trigger}.
 *
 * <p>Necessary because a native {@link ScheduledExecutorService} supports
 * delay-driven execution only. The flexibility of the {@link Trigger} interface
 * will be translated onto a delay for the next execution time (repeatedly).
 *
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @since 3.0
 */
class ReschedulingRunnable implements Runnable, ScheduledFuture<Object> {

	private final Trigger trigger;

	private final SimpleTriggerContext triggerContext = new SimpleTriggerContext();

	private final ScheduledExecutorService executor;

	private ScheduledFuture<?> currentFuture;

	private Date scheduledExecutionTime;
	
	private Runnable task;

	private final Object triggerContextMonitor = new Object();


	public ReschedulingRunnable(Runnable task, Trigger trigger, ScheduledExecutorService executor) {
		this.trigger = trigger;
		this.executor = executor;
		this.task = task;
	}


	public ScheduledFuture<?> schedule() {
		synchronized (this.triggerContextMonitor) {
			this.scheduledExecutionTime = this.trigger.nextExecutionTime(this.triggerContext);
			if (this.scheduledExecutionTime == null) {
				return null;
			}
			long initialDelay = this.scheduledExecutionTime.getTime() - System.currentTimeMillis();
			this.currentFuture = this.executor.schedule(this, initialDelay, TimeUnit.MILLISECONDS);
			return this;
		}
	}

	public void run() {
		Date actualExecutionTime = new Date();
		Date completionTime = new Date();
		synchronized (this.triggerContextMonitor) {
			this.triggerContext.update(this.scheduledExecutionTime, actualExecutionTime, completionTime);
			if (!this.currentFuture.isCancelled()) {
				task.run();
				schedule();
			}
		}
	}


	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		synchronized (this.triggerContextMonitor) {
			return this.currentFuture.cancel(mayInterruptIfRunning);
		}
	}

	@Override
	public boolean isCancelled() {
		synchronized (this.triggerContextMonitor) {
			return this.currentFuture.isCancelled();
		}
	}

	@Override
	public boolean isDone() {
		synchronized (this.triggerContextMonitor) {
			return this.currentFuture.isDone();
		}
	}

	@Override
	public Object get() throws InterruptedException, ExecutionException {
		ScheduledFuture<?> curr;
		synchronized (this.triggerContextMonitor) {
			curr = this.currentFuture;
		}
		return curr.get();
	}

	@Override
	public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		ScheduledFuture<?> curr;
		synchronized (this.triggerContextMonitor) {
			curr = this.currentFuture;
		}
		return curr.get(timeout, unit);
	}

	@Override
	public long getDelay(TimeUnit unit) {
		ScheduledFuture<?> curr;
		synchronized (this.triggerContextMonitor) {
			curr = this.currentFuture;
		}
		return curr.getDelay(unit);
	}

	@Override
	public int compareTo(Delayed other) {
		if (this == other) {
			return 0;
		}
		long diff = getDelay(TimeUnit.MILLISECONDS) - other.getDelay(TimeUnit.MILLISECONDS);
		return (diff == 0 ? 0 : ((diff < 0)? -1 : 1));
	}

}
