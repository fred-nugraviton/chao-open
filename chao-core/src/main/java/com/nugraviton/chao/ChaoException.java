package com.nugraviton.chao;

public class ChaoException extends RuntimeException{

private static final long serialVersionUID = -6625874658383402811L;
	
	public ChaoException(){
		super();
	}
	
	public ChaoException(String msg){
		super(msg);
	}

	public ChaoException(String msg, Throwable cause){
		super(msg, cause);
	}
	
	 public ChaoException(Throwable cause) {
		 super(cause);
	 }

	   
    protected ChaoException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
