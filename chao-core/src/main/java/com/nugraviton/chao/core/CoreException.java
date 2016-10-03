package com.nugraviton.chao.core;

public class CoreException extends RuntimeException{

private static final long serialVersionUID = -6625874658383402811L;
	
	public CoreException(){
		super();
	}
	
	public CoreException(String msg){
		super(msg);
	}

	public CoreException(String msg, Throwable cause){
		super(msg, cause);
	}
	
	 public CoreException(Throwable cause) {
		 super(cause);
	 }

	   
    protected CoreException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
