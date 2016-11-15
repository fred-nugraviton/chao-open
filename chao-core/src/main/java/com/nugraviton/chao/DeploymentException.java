package com.nugraviton.chao;

public class DeploymentException extends RuntimeException{

private static final long serialVersionUID = -6629874658383402811L;
	
	public DeploymentException(){
		super();
	}
	
	public DeploymentException(String msg){
		super(msg);
	}

	public DeploymentException(String msg, Throwable cause){
		super(msg, cause);
	}
	
	 public DeploymentException(Throwable cause) {
		 super(cause);
	 }

	   
    protected DeploymentException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
