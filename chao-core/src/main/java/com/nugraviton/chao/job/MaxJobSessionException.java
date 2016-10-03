package com.nugraviton.chao.job;

/**
 * Thrown when trying to create JobHandle but maximum number of handle has reached.
 * @author fredwang
 *
 */
public class MaxJobSessionException extends JobException{

	private static final long serialVersionUID = -66454312853402811L;
	
	public MaxJobSessionException(){
		super();
	}
	
	public MaxJobSessionException(String msg){
		super(msg);
	}

	public MaxJobSessionException(String msg, Throwable cause){
		super(msg, cause);
	}
	
	 public MaxJobSessionException(Throwable cause) {
		 super(cause);
	 }
	   
    protected MaxJobSessionException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

