package fr.actionrpg3d.multiplayer;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;

import fr.actionrpg3d.multiplayer.Packageable.UnpackMethod;

public class InputPackage extends InputStream {
	
	private InputStream in;
	
	public InputPackage(InputStream in) {
		this.in = in;
	}

	@Override
	public int read() throws IOException {
		return in.read();
	}
	
	public byte[] readBytes(int count) throws IOException {
		byte[] bytes = new byte[count];
		read(bytes);
		return bytes;
	}
	
	public int readInt() throws IOException {
		return ByteBuffer.wrap(readBytes(4)).getInt();
	}
	
	public float readFloat() throws IOException {
		return ByteBuffer.wrap(readBytes(4)).getFloat();
	}
	
	public char readChar() throws IOException {
		return ByteBuffer.wrap(readBytes(2)).getChar();
	}
	
	public String readString() throws IOException {
		char[] chars = new char[readInt()];
		for (int i = 0; i < chars.length; i++)
			chars[i] = readChar();
		return String.valueOf(chars);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T read(Class<T> clazz) throws IOException {
		for (Method method : clazz.getDeclaredMethods()) {
			if (method.isAnnotationPresent(UnpackMethod.class)) {
				if (method.getParameterCount() != 1 || !method.getParameterTypes()[0].equals(InputPackage.class) || !method.getReturnType().equals(clazz) || !Modifier.isStatic(method.getModifiers())) {
					throw new RuntimeException("Invalid unpack method in class " + clazz.getCanonicalName());
				}
				try {
					return (T) method.invoke(null, this);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		throw new RuntimeException("No unpack method in class " + clazz.getCanonicalName());
	}
	
	public <T> Collection<T> readCollection(Class<T> clazz) throws IOException {
		int count = readInt();
		ArrayList<T> list = new ArrayList<>();
		for (int i = 0; i < count; i++)
			list.add(read(clazz));
		return list;
	}
	
	@Override
	public void close() throws IOException {
		in.close();
	}
	
}
