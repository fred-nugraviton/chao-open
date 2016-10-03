package com.nugraviton.chao.common;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.util.List;
import java.util.UUID;

public class FileUtil {
	
	/**
	 * Delete the directory if exists.
	 * @param directory
	 */
	public static void deleteIfExists(Path directory){
		try {
			
			if(!directory.toFile().exists()){
				return;
			}
			
			Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
				   @Override
				   public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					   Files.delete(file);
					   return FileVisitResult.CONTINUE;
				   }

				   @Override
				   public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
					   Files.delete(dir);
					   return FileVisitResult.CONTINUE;
				   }

			   });
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Create the directories if not exists.
	 * @param paths
	 */
	public static void createDirectoriesIfNotExists(Path... paths){
		for (Path path : paths){
			try {
				if (!path.toFile().exists()){
					Files.createDirectories(path);
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	/**
	 * Create the file if not exists.
	 * @param path
	 * @param attrs
	 */
	public static void createFilesIfNotExists(Path path, FileAttribute<?>... attrs){
		
		try {
			if (!path.toFile().exists()){
				Files.createFile(path, attrs);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Path getDeployedFile(Path appFile, Path workDir) {
		String lockFileName = appFile.getFileName()+ ".deployed";
		return workDir.resolve(lockFileName);
	}
	
	public static UUID getAppId(Path appFile, Path workDir){
		Path deployedFile = getDeployedFile(appFile, workDir);
		try {
			if(deployedFile.toFile().exists()){
				List<String> lines;
					lines = Files.readAllLines(deployedFile);
				String uuidString = lines.get(0).split("=")[1];
				return UUID.fromString(uuidString);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return null;
	}
	
	public static Path getDeployedFailedFile(Path appFile, Path workDir) {
		String lockFileName = appFile.getFileName()+ ".deployedfailed";
		return workDir.resolve(lockFileName);
	}
	
	private FileUtil(){}
}
