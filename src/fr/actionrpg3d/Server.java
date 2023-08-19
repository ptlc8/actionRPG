package fr.actionrpg3d;

import java.io.IOException;

import fr.actionrpg3d.game.Game;
import fr.actionrpg3d.multiplayer.ServerGameManager;
import fr.actionrpg3d.multiplayer.SocketServer;

public class Server {
	
	private SocketServer server;
	private ServerGameManager game;

	public Server(String hostname) throws IOException {
		server = new SocketServer(13028, 10, hostname);
		server.addCloseListener(() -> System.out.println("Server closed"));
		server.addJoinListener(this::onJoin);
		server.addDataListener(this::onData);
		server.addLeaveListener(this::onLeave);
		game = new ServerGameManager(new Game(-1984888624), server);
		System.out.println("Server started on " + server.getLocalPort());
		game.startUpdateLoop();
	}
	
	public Server() throws IOException {
		this("localhost");
	}
	
	public void onJoin(int id) {
		//System.out.println("Client " + id + " joined");
		server.sendData(id, "self " + id);
	}
	
	public void onLeave(int id) {
		System.out.println("Client " + id + " disconnected");
	}
	
	public void onData(int id, String data) {
		System.out.println("Client " + id + " : " + data);
	}
	
	public ServerGameManager getGameManager() {
		return game;
	}
	
	public int getClientsCount() {
		return server.getClients().size();
	}
	
}
