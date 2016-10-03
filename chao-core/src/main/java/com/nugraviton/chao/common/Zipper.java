package com.nugraviton.chao.common;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Zipper {
	/**
     * Size of the buffer to read/write data
     */
    private static final int BUFFER_SIZE = 4096;
    /**
     * Extracts a zip file specified by the zipFilePath to a directory specified by
     * destDirectory (will be created if does not exists)
     * @param zipFilePath
     * @param destDirectory
     * @throws IOException
     */
    public static void unzip(Path zipFilePath, Path destDirectory) throws IOException {
        
        try(ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath.toFile()));){
	        ZipEntry entry = zipIn.getNextEntry();
	        while (entry != null) {
	            Path filePath = destDirectory.resolve(entry.getName());
	            Path parentPath = filePath.getParent();
	            Files.createDirectories(parentPath);
	            if (!entry.isDirectory()) {	            	
	            	extractFile(zipIn, filePath);
	            } 
	            zipIn.closeEntry();
	            entry = zipIn.getNextEntry();
	        }
        }
    }
    
    /**
     * Extracts a zip entry (file entry)
     * @param zipIn
     * @param filePath
     * @throws IOException
     */
    private static void extractFile(ZipInputStream zipIn, Path filePath) throws IOException {
 
        try(BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath.toFile()));){
	        byte[] bytesIn = new byte[BUFFER_SIZE];
	        int read = 0;
	        while ((read = zipIn.read(bytesIn)) != -1) {
	            bos.write(bytesIn, 0, read);
	        }
        }
    }
}
