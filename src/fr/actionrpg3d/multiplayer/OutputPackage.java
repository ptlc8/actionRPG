package fr.actionrpg3d.multiplayer;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Collection;

public class OutputPackage extends OutputStream {
	
	private OutputStream out;
	
	public OutputPackage(OutputStream out) {
		this.out = out;
	}

	@Override
	public void write(int b) throws IOException {
		out.write(b);
	}
	
	public void write(byte[] bytes) throws IOException {
		out.write(bytes);
	}
	
	public void writeInt(int i) throws IOException {
		write(ByteBuffer.allocate(4).putInt(i).array());
	}
	
	public void writeFloat(float f) throws IOException {
		write(ByteBuffer.allocate(4).putFloat(f).array());
	}
	
	public void write(char c) throws IOException {
		write(ByteBuffer.allocate(2).putChar(c).array());
	}
	
	public void write(String s) throws IOException {
		writeInt(s.length());
		for (Character c : s.toCharArray())
			write(c);
	}
	
	public <T extends Packageable<T>> void write(Packageable<T> p) throws IOException {
		p.pack(this);
	}
	
	public <T extends Packageable<T>> void write(Collection<T> c) throws IOException {
		writeInt(c.size());
		for (T item : c)
			write(item);
	}
	
	@Override
	public void flush() throws IOException {
		out.flush();
	}
	
	@Override
	public void close() throws IOException {
		out.close();
	}
	
}
