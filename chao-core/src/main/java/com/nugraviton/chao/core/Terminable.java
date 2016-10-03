package com.nugraviton.chao.core;

/**
 * An interface to enable life cycle control of a component 
 * so it can be shut down and removed from system.
 * All methods are thread safe.
 * 
 * @author fred.wang@nuGraviton.com
 *
 */
public interface Terminable {
	
	 /**
     * Initiates an orderly shutdown in which previously started
     * tasks are executed, but no new ones will be accepted.
     * Invocation has no additional effect if already shut down or not started.
     *
     * <p>This method does not wait for previously started sessions to
     * complete execution.  Use {@link #awaitTermination awaitTermination}
     * to do that.
     */
    void shutdown();

    /**
     * Stops all actively executing tasks immediately, 
     * waiting tasks are ignored.
     *
     * <p>This method does not wait for actively executing tasks to
     * terminate.  Use {@link #awaitTermination awaitTermination} to
     * do that.
     *
     */
    void shutdownNow();

    /**
     * Test if it's already shut down.
     *
     * @return {@code true} if this executor has been shut down
     */
    boolean isShutdown();

    /**
     * Returns {@code true} if all tasks have completed following shut down.
     * Note that {@code isTerminated} is never {@code true} unless
     * either {@code shutdown} or {@code shutdownNow} was called first.
     *
     * @return {@code true} if all tasks have completed following shut down
     */
    boolean isTerminated();

    /**
     * Notifies the waiting threads on {@code #awaitTermination()} for termination
     * if all requirements are met, otherwise no effect.
     */
	void tryNotifyTermination();

}
