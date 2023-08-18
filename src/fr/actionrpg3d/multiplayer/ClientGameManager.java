package fr.actionrpg3d.multiplayer;

import fr.actionrpg3d.GameManager;
import fr.actionrpg3d.Utils;
import fr.actionrpg3d.game.Game;
import fr.actionrpg3d.inputs.Input;
import fr.actionrpg3d.inputs.InputName;

public class ClientGameManager extends GameManager {
	
	private SocketClient socket;
	private Input lastFinalInput = null;
	private Game save;
	
	public ClientGameManager(Game game, SocketClient socket) {
		super(game);
		save = game.clone();
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
		insertInput(input);
		if (tick <= game.getTick())
			rollback(game.getTick() - tick + 1);
		return input;
	}
	
	private void updateLastFinalInput() {
		synchronized (allInputs) {
			Input next;
			if (lastFinalInput != null) {
				next = allInputs.higher(lastFinalInput);
			} else {
				next = allInputs.first();
				if (next.hashCode() != GameManager.firstInputId)
					return;
			}
			while (next != null && (lastFinalInput == null || next.getId() - lastFinalInput.getId() == 1)) {
				lastFinalInput = next;
				next = allInputs.higher(lastFinalInput);
			}
		}
	}
	
	
	/**
	 * Rollback game, maybe to include missing past inputs
	 * @param tick number of ticks to rollback
	 */
	public void rollback(int tick) {
		updateLastFinalInput();
		int currentTick = game.getTick();
		game = save;
		System.out.println("rollback to tick " + save.getTick() + " going back to tick " + currentTick);
		while(game.getTick() < currentTick) {
			if (game.getTick() == lastFinalInput.getTick() - 1)
				save = game.clone();
			game.update(getControls());
		}
	}

}
