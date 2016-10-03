package com.nugraviton.chao.job;

/**
 * Thrown when an invalid job definition is detected.
 * 
 * @author fred.wang@nuGraviton.com
 *
 */
public class InvalidJobDefinitionException extends RuntimeException{

	private static final long serialVersionUID = -66454312853402811L;
	
	public InvalidJobDefinitionException(){
		super();
	}
	
	public InvalidJobDefinitionException(String msg){
		super(msg);
	}

	public InvalidJobDefinitionException(String msg, Throwable cause){
		super(msg, cause);
	}
	
	 public InvalidJobDefinitionException(Throwable cause) {
		 super(cause);
	 }

	   
    protected InvalidJobDefinitionException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

