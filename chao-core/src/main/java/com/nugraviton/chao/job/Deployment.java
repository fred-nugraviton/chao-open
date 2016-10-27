package com.nugraviton.chao.job;

import java.net.URL;
import java.nio.file.Path;

public interface Deployment {

	Path getAppWorkDir();

	/**
	 * the class path URLs of this deployment.
	 * @return URL[] -- class path URLs
	 */
	URL[] getScannerUrls();

	String getClassPathString();

}