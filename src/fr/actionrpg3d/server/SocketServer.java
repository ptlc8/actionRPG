package fr.actionrpg3d.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SocketServer extends ServerSocket {

	private ArrayList<Consumer<Integer>> joinListeners = new ArrayList<>();
	private ArrayList<BiConsumer<Integer, String>> dataListeners = new ArrayList<>();
	private ArrayList<Consumer<Integer>> leaveListeners = new ArrayList<>();
	private ArrayList<Runnable> closeListeners = new ArrayList<>();

	private Map<Integer, SocketServerClient> clients = new HashMap<>();
	private int clientIdIncrementer = 1;

	public SocketServer(int port, int backlog) throws IOException {
		this(port, backlog, InetAddress.getLocalHost());
	}

	public SocketServer(int port, int backlog, String hostname) throws IOException {
		this(port, backlog, InetAddress.getByName(hostname));
	}

	public SocketServer(int port, int backlog, InetAddress host) throws IOException {
		super(port, backlog, host);
		new Thread(() -> {
			try {
				while (!isClosed()) {
					Socket socket = accept();
					int clientId = clientIdIncrementer++;
					clients.put(clientId, new SocketServerClient(socket, clientId));
					joinListeners.forEach(l -> l.accept(clientId));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();
	}

	@Override
	public void close() throws IOException {
		if (clients != null)
			for (SocketServerClient client : clients.values())
				client.close();
		super.close();
		if (closeListeners != null)
			closeListeners.forEach(l -> l.run());
	}

	public boolean sendData(int clientId, String data) {
		SocketServerClient client = clients.get(clientId);
		if (client == null)
			return false;
		client.sendData(data);
		return true;
	}

	public void broadcastData(String data) {
		for (SocketServerClient client : clients.values())
			client.sendData(data);
	}

	public Map<Integer, SocketServerClient> getClients() {
		return clients;
	}
	
	public void addJoinListener(Consumer<Integer> listener) {
		joinListeners.add(listener);
	}

	public void addDataListener(BiConsumer<Integer, String> listener) {
		dataListeners.add(listener);
	}

	public void addLeaveListener(Consumer<Integer> listener) {
		leaveListeners.add(listener);
	}

	public void addCloseListener(Runnable listener) {
		closeListeners.add(listener);
	}

	public class SocketServerClient {

		private final Socket socket;
		private BufferedReader reader;
		private PrintWriter writer;

		public SocketServerClient(Socket socket, int clientId) throws IOException {
			this.socket = socket;
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(socket.getOutputStream(), true);
			new Thread(() -> {
				try {
					String line;
					while ((line = reader.readLine()) != null && !socket.isClosed()) {
						String data = line;
						dataListeners.forEach(l -> l.accept(clientId, data));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				clients.remove(clientId);
				leaveListeners.forEach(l -> l.accept(clientId));
			}).start();
		}

		public void sendData(String data) {
			writer.println(data);
		}

		public void close() throws IOException {
			socket.close();
		}

	}

}
