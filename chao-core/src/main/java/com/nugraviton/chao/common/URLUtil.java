package com.nugraviton.chao.common;

import java.io.File;
import java.net.URL;

public class URLUtil {

	private URLUtil() {
	}
	
	public static String toClassPathString(URL[] urls){
		StringBuilder path = new StringBuilder();
		for(URL url : urls){
			
			String r = url.getFile().replaceFirst("/", "");
			path.append(r);
			path.append(File.pathSeparator);
		}
		return path.toString();
	}
	
}
