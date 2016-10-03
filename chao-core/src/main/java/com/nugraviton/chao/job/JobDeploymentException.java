package com.nugraviton.chao.job;

/**
 * Thrown when an invalid job definition is detected.
 * 
 * @author fred.wang@nuGraviton.com
 *
 */
public class JobDeploymentException extends RuntimeException{

	private static final long serialVersionUID = -66454312853402811L;
	
	public JobDeploymentException(){
		super();
	}
	
	public JobDeploymentException(String msg){
		super(msg);
	}

	public JobDeploymentException(String msg, Throwable cause){
		super(msg, cause);
	}
	
	 public JobDeploymentException(Throwable cause) {
		 super(cause);
	 }

	   
    protected JobDeploymentException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

