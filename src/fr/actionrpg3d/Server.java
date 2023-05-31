package fr.actionrpg3d;

import java.io.IOException;

import fr.actionrpg3d.game.Game;
import fr.actionrpg3d.inputs.InputName;
import fr.actionrpg3d.server.ServerGameManager;
import fr.actionrpg3d.server.SocketServer;

public class Server {
	
	private SocketServer server;
	private ServerGameManager game;

	public Server() throws IOException {
		game = new ServerGameManager(new Game(-1984888624));
		server = new SocketServer(13028, 10);
		System.out.println("Server started on " + server.getLocalPort());
		server.addCloseListener(() -> System.out.println("Server closed"));
		server.addJoinListener(this::onJoin);
		server.addDataListener(this::onData);
		server.addLeaveListener(this::onLeave);
	}
	
	public void onJoin(int id) {
		System.out.println("Client " + id + " joined");
		server.sendData(id, "self " + id);
		server.sendData(id, "initgame " + game.getGame().getSeed());
		for (int playerId : game.getGame().getPlayers().keySet()) {
			server.sendData(id, "join " + playerId);
		}
		game.addPlayer(id);
		server.broadcastData("join " + id);
	}
	
	public void onLeave(int id) {
		System.out.println("Client " + id + " disconnected");
		game.removePlayer(id);
		server.broadcastData("leave " + id);
	}
	
	public void onData(int id, String data) {
		System.out.println("Client " + id + " : " + data);
		String[] args = data.split(" ");
		if ("input".equals(args[0]) && args.length > 2) {
			Input input = game.receiveInput(id, InputName.valueOf(args[1]), Utils.parseFloat(args[2]));
			server.broadcastData("input " + input.getId() + " " + input.getTick() + " " + input.getPlayer() + " " + input.getName() + " " + input.getValue());
		}
	}
	
	public ServerGameManager getGameManager() {
		return game;
	}
	
	public int getClientsCount() {
		return server.getClients().size();
	}
	
}
