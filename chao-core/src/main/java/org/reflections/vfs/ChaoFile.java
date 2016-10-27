package org.reflections.vfs;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public class ChaoFile implements Vfs.File{

	private Path file;
	
	ChaoFile(Path file){
		this.file = file;
	}
	
	@Override
	public String getName() {
		return file.toFile().getName();
	}

	@Override
	public String getRelativePath() {
		Path parent = file.getParent();
		return null==parent ? file.toString() : parent.toString();
	}

	@Override
	public InputStream openInputStream() throws IOException {
		return new FileInputStream(file.toFile());
	}

}
