package com.nugraviton.chao.schedule;

public class TaskExeceutionException extends RuntimeException{

private static final long serialVersionUID = -6625874658383402811L;
	
	public TaskExeceutionException(){
		super();
	}
	
	public TaskExeceutionException(String msg){
		super(msg);
	}

	public TaskExeceutionException(String msg, Throwable cause){
		super(msg, cause);
	}
	
	 public TaskExeceutionException(Throwable cause) {
		 super(cause);
	 }

	   
    protected TaskExeceutionException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
