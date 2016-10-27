package com.nugraviton.chao.core.event;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.nugraviton.chao.core.DeploymentException;
import com.nugraviton.chao.job.Deployment;

public class DeploymentEvent implements Deployment{
	
	private URL[] urls;

	public DeploymentEvent(){
		
		String[] cpStrings = System.getProperty("java.class.path").split(File.pathSeparator);
		urls = new URL[cpStrings.length];
		for(int i = 0; i < cpStrings.length; i++){
			try {
				this.urls[i] = Paths.get(cpStrings[i]).toUri().toURL();
			} catch (MalformedURLException e) {
				throw new DeploymentException(e);
			} 
		}		
	}

	public DeploymentEvent(String... packages) {
		
		List<URL> urlList = new ArrayList<>();
		try {
			for(String pkg : packages){
				String packageName = pkg.replace(".", "/");
				Enumeration<URL> urlEnum = ClassLoader.getSystemResources(packageName);
				while(urlEnum.hasMoreElements()){
					URL url = urlEnum.nextElement();
					urlList.add(url);
				}
				this.urls = new URL[urlList.size()];
				urlList.toArray(this.urls);
			}
		} catch (IOException e) {
			throw new DeploymentException(e);
		}
	}

	@Override
	public Path getAppWorkDir() {
		
		return Paths.get(System.getProperty("user.dir"));
	}

	@Override
	public URL[] getScannerUrls() {
		
		return this.urls;
	}

	@Override
	public String getClassPathString() {
		return System.getProperty("java.class.path");
	}

}
