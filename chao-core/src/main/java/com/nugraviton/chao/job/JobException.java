package com.nugraviton.chao.job;

/**
 * Thrown when trying to create JobHandle but maximum number of handle has reached.
 * @author fredwang
 *
 */
public class JobException extends RuntimeException{

	private static final long serialVersionUID = -66454312853402811L;
	
	public JobException(){
		super();
	}
	
	public JobException(String msg){
		super(msg);
	}

	public JobException(String msg, Throwable cause){
		super(msg, cause);
	}
	
	 public JobException(Throwable cause) {
		 super(cause);
	 }

	   
    protected JobException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

