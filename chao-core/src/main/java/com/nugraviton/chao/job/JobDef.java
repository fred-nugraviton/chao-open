package com.nugraviton.chao.job;

import java.io.Serializable;

public interface JobDef extends Cloneable, Serializable {

	String getJobName();
	String getJobDesc();
	String getClassPath();
	String getJobWorkDir();
	int getMaxSession();
	String[] getProperties();
	JobDef clone();
	boolean isFixedDelay();
	void setFixedDelay(boolean fixDelay);
}
