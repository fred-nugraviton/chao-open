package com.nugraviton.chao.core;

import static org.junit.Assert.assertNotNull;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

import com.nugraviton.chao.common.URLUtil;

public class URLUtilTest {

	@Test
	public void toClassPathTest() throws MalformedURLException {
		
		URL[] urls = new URL[3];
		urls[0] = new URL("https://test-cloud-pln.pbcs.us1.oraclecloud.com/workspace");
		urls[1] = new URL("file:/D:/tools/drive/chao/chao-container/target/work");
		urls[2] = new URL("file:/D:/tools/drive/chao/");
		
		String path = URLUtil.toClassPathString(urls);
		assertNotNull(path);
		
	}

}
