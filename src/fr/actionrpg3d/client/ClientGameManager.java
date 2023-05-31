package fr.actionrpg3d.client;

import fr.actionrpg3d.GameManager;
import fr.actionrpg3d.Input;
import fr.actionrpg3d.Utils;
import fr.actionrpg3d.game.Game;
import fr.actionrpg3d.inputs.InputName;

public class ClientGameManager extends GameManager {
	
	SocketClient socket;
	
	public ClientGameManager(Game game, SocketClient socket) {
		super(game);
		this.socket = socket;
		socket.addDataListener(this::onServerData);
	}
	
	private void onServerData(String data) {
		String[] args = data.split(" ");
		if ("input".equals(args[0])) {
			receiveInput(Utils.parseInt(args[1]), Utils.parseInt(args[2]), Utils.parseInt(args[3]), InputName.valueOf(args[4]), Utils.parseFloat(args[5]));
		}
		if ("join".equals(args[0])) {
			game.addPlayer(Utils.parseInt(args[1]));
		}
		if ("leave".equals(args[0])) {
			game.removePlayer(Utils.parseInt(args[1]));
		}
	}

	@Override
	public Input receiveInput(int player, InputName name, float value) {
		socket.sendData("input " + name + " " + value);
		return null;
	}

	private Input receiveInput(int id, int tick, int player, InputName name, float value) {
		//super.receiveInput(player, name, value);
		Input input = new Input(id, tick, player, name, value);
		if (tick <= game.getTick())
			rollback(game.getTick() - tick + 1);
		allInputs.add(input);
		return input;
	}
	
	
	/**
	 * Rollback game, maybe to include missing inputs
	 * @param tick number of ticks to rollback
	 */
	public void rollback(int tick) {
		// TODO rollback
	}

}
