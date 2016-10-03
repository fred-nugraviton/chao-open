package org.reflections.adapters;

import java.io.DataInputStream;
import java.io.IOException;

import org.reflections.ReflectionsException;
import org.reflections.vfs.Vfs;

import javassist.bytecode.ClassFile;

/**
 * Read the class byte code stream from a file in jar of jar. 
 * @author fredwang
 *
 */
public class ChaoMetaDataAdapter extends JavassistAdapter{

	@Override
	public ClassFile getOfCreateClassObject(final Vfs.File file) {
		try (DataInputStream dis = new DataInputStream(file.openInputStream());){
            return new ClassFile(dis);
        } catch (IOException e) {
            throw new ReflectionsException("could not create class file from " + file.getName(), e);
        } 
    }

}
