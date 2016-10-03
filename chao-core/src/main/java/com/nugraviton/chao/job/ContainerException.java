package com.nugraviton.chao.job;

/**
 * General ContainerException.
 * 
 * @author fred.wang@nuGraviton.com
 *
 */
public class ContainerException extends RuntimeException{

	private static final long serialVersionUID = -6645431285383402811L;
	
	public ContainerException(){
		super();
	}
	
	public ContainerException(String msg){
		super(msg);
	}

	public ContainerException(String msg, Throwable cause){
		super(msg, cause);
	}
	
	 public ContainerException(Throwable cause) {
		 super(cause);
	 }

	   
    protected ContainerException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
