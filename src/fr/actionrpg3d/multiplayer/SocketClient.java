package fr.actionrpg3d.multiplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.function.Consumer;

public class SocketClient extends Socket {

	private PrintWriter writer;
	private BufferedReader reader;
	
	private ArrayList<Consumer<String>> dataListeners = new ArrayList<>();
	private ArrayList<Runnable> closeListeners = new ArrayList<>();
	
	public SocketClient() throws IOException {
		super();
	}

	public void connect(int port) throws IOException {
		connect(new InetSocketAddress(InetAddress.getLocalHost(), port));
	}

	public void connect(String hostname, int port) throws IOException {
		connect(new InetSocketAddress(InetAddress.getByName(hostname), port));
	}
	
	public void connect(SocketAddress host) throws IOException {
		super.connect(host);
		writer = new PrintWriter(getOutputStream(), true);
		reader = new BufferedReader(new InputStreamReader(getInputStream()));
		new Thread(() -> {
			try {
				String line;
				while (!isClosed() && (line = reader.readLine()) != null) {
					String data = line;
					//dataListeners.forEach(l -> l.accept(data));
					for (int i = 0; i < dataListeners.size(); i++) dataListeners.get(i).accept(data);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			closeListeners.forEach(l -> l.run());
		}).start();
	}

	public void sendData(String data) {
		writer.println(data);
	}
	
	public void addDataListener(Consumer<String> listener) {
		dataListeners.add(listener);
	}
	
	public void addCloseListener(Runnable listener) {
		closeListeners.add(listener);
	}

}
