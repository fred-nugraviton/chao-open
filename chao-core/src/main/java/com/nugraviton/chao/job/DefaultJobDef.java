package com.nugraviton.chao.job;

import java.net.URL;
import java.util.Arrays;

import com.nugraviton.chao.annotation.Job;

/**
 * Wraps what is in {@link Job}
 * 
 * @author fred.wang@nuGraviton.com
 *
 */
public final class DefaultJobDef implements JobDef{
	
	private static final long serialVersionUID = -4088253771194865424L;
	
	private String jobName;
	private String jobDesc;
	private int maxSession;
	private String[] properties;
	private String jobWorkDir;
	private URL[] classPathUrls;
	
	private String classPathString;
	
	private boolean fixedDelay = false;

	private DefaultJobDef(DefaultJobDef orig){
		this.jobName = String.valueOf(orig.jobName.toCharArray());
		this.jobDesc = String.valueOf(orig.jobDesc.toCharArray());
		this.maxSession = Integer.valueOf(orig.maxSession);
		String[] prop = new String[orig.properties.length];
		for(int i=0; i< orig.properties.length; i++){
			prop[i] = String.valueOf(orig.properties[i].toCharArray());
		}
		this.properties = prop;
		this.jobWorkDir = orig.jobWorkDir.toString();
		this.classPathUrls = orig.classPathUrls;
		this.classPathString = orig.classPathString;
	}
		
	public DefaultJobDef(Class<?> clazz, Deployment dinfo) {
		Job jobConf = clazz.getDeclaredAnnotation(Job.class);
		if(jobConf.name().isEmpty()){
			this.jobName = clazz.getName();
		}else{
			this.jobName = jobConf.name();
		}
		this.jobDesc = jobConf.desc();
		this.maxSession = jobConf.maxSession();
		this.properties = jobConf.properties();
		
		this.jobWorkDir = dinfo.getAppWorkDir().toString();
		this.classPathUrls = dinfo.getScannerUrls();
		this.classPathString = dinfo.getClassPathString();
	}
	
	/**
	 * Job name. Default to fully qualified name of the class 
	 * which is annotated.
	 * @return String -- Job name.
	 */
	@Override
	public String getJobName(){
		return jobName;
	}
	
	/**
	 * Job description. Default empty string.
	 * @return String -- job description.
	 */
	@Override
	public String getJobDesc(){
		return jobDesc;
	}
	
	/**
	 * The max number of process this job can run in parallel, 
	 * the scheduled runs are ignored when max number is reached.
	 * Default 1.
	 * @return int -- max number of process this job can run in parallel.
	 */
	@Override
	public int getMaxSession() {
		return maxSession;
	}
	
	/**
	 * Path of properties file which contains configuration values.
	 * The path is relative to class path root.
	 * 
	 * @return String[] -- path of properties file like conf/config.properties
	 */
	@Override
	public String[] getProperties(){
		return properties;
	}
	
	@Override
	public String getClassPath() {
		return this.classPathString;
	}

	public String getJobWorkDir() {
		return jobWorkDir;
	}
	
	public boolean isFixedDelay() {
		return fixedDelay;
	}

	@Override
	public void setFixedDelay(boolean fixedDelay) {
		this.fixedDelay = fixedDelay;
	}

	@Override
	public DefaultJobDef clone(){
		return new DefaultJobDef(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((jobName == null) ? 0 : jobName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DefaultJobDef other = (DefaultJobDef) obj;
		if (jobName == null) {
			if (other.jobName != null)
				return false;
		} else if (!jobName.equals(other.jobName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "JobConf [jobName=" + jobName + ", jobDesc=" + jobDesc + ", maxSession=" + maxSession + ", properties="
				+ Arrays.toString(properties) + ", jobWorkDir="
				+ jobWorkDir + ", classPathString=" + classPathString + "]";
	}
}
