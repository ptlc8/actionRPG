package fr.actionrpg3d.multiplayer;

import fr.actionrpg3d.GameManager;
import fr.actionrpg3d.Utils;
import fr.actionrpg3d.game.Game;
import fr.actionrpg3d.inputs.Input;
import fr.actionrpg3d.inputs.InputName;

public class ServerGameManager extends GameManager {
	
	private SocketServer server;
	
	public ServerGameManager(Game game, SocketServer server) {
		super(game);
		this.server = server;
		server.addJoinListener(this::onJoin);
		server.addDataListener(this::onClientData);
		server.addLeaveListener(this::onLeave);
	}
	
	public void onJoin(int id) {
		server.sendData(id, "initgame " + getGame().toBase64());
		addPlayer(id);
		server.broadcastData("join " + id);
	}
	
	public void onClientData(int id, String data) {
		String[] args = data.split(" ");
		if ("input".equals(args[0]) && args.length > 2) {
			Input input = receiveInput(id, InputName.valueOf(args[1]), Utils.parseFloat(args[2]));
			server.broadcastData("input " + input.getId() + " " + input.getTick() + " " + input.getPlayer() + " " + input.getName() + " " + input.getValue());
		}
	}
	
	public void onLeave(int id) {
		removePlayer(id);
		server.broadcastData("leave " + id);
	}
	
}
