package org.reflections.vfs;

import java.net.URL;

import org.reflections.vfs.Vfs.Dir;

public class ChaoUrlType implements Vfs.UrlType{

	@Override
	public boolean matches(URL url) throws Exception {
		return url.getProtocol().equals("file") && (url.toExternalForm().toLowerCase().contains(".chao")||url.toExternalForm().toLowerCase().contains(".zip"));
	}

	@Override
	public Dir createDir(URL url) throws Exception {
		return new ChaoDir(url);
	}

}
