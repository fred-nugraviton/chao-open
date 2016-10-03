package org.reflections.vfs;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarFile;

import org.reflections.vfs.Vfs.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChaoDir implements Vfs.Dir{
	
	private static final Logger log = LoggerFactory.getLogger(ChaoDir.class);
	
	private Set<ZipDir> zipDirs = new HashSet<>();
	
	private final URL url;
	public ChaoDir(URL url){
		this.url = url;
	}

	@Override
	public Iterable<File> getFiles() {
		
		final Set<File> files = new HashSet<>();
		try{
			Files.walkFileTree(Paths.get(url.toURI()),  new SimpleFileVisitor<Path>() {
                
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                	String fileName = file.toString();
                	if(fileName.endsWith(".class") || fileName.endsWith(".properties")){
               			Vfs.File systemFile = new ChaoFile(file);
               			files.add(systemFile);
                		if(log.isDebugEnabled()){
                			log.debug("ChaoFile loaded {}", file);
                		}
                		
                	}else if (fileName.endsWith(".jar")){
                		ZipDir jarInputDir = new ZipDir(new JarFile(file.toFile()));
                		Iterable<File> jarInputFiles = jarInputDir.getFiles();
                		for(File jarInputFile : jarInputFiles){
                			files.add(jarInputFile);
                			if(log.isDebugEnabled()){
                    			log.debug("JarInputFile loaded {} from {}",jarInputFile.getName(), file);
                    		}
                		}
                		zipDirs.add(jarInputDir);
                	}
                	
                    return FileVisitResult.CONTINUE;
                }
			});
		}catch(IOException | URISyntaxException e){
			log.error("error creating virtrual files for " + url, e);
		}
			
		return files;
	}

	@Override
	public String getPath() {
		return url.getPath();
	}

	@Override
	public void close() {
		
		for (ZipDir zipDir : zipDirs){
			zipDir.close();
		}
	}
}
