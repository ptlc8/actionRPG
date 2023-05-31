package fr.actionrpg3d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import fr.actionrpg3d.game.Controls;
import fr.actionrpg3d.game.Game;
import fr.actionrpg3d.inputs.InputName;
import fr.actionrpg3d.render.Renderer;

public class GameManager {

	public static final int TPS = 60;

	protected Game game;
	private int currentTPS;
	protected final Set<Input> allInputs = Collections.synchronizedSet(new HashSet<>());
	protected Map<Integer, Map<InputName, Float>> currentInputsValues = new HashMap<>();
	private int inputsIdIncrementer = 0;

	private ArrayList<Consumer<Map<Integer, Controls>>> updateListeners = new ArrayList<>();

	public GameManager(Game game) {
		this.game = game;
	}

	public Input receiveInput(int player, InputName name, float value) {
		Input input = new Input(inputsIdIncrementer++, getGame().getTick() + 1, player, name, value);
		allInputs.add(input);
		return input;
	}

	public Map<Integer, Controls> getControls() {
		for (Input input : allInputs) {
			if (input.getTick() == getGame().getTick()) {
				if (!currentInputsValues.containsKey(input.getPlayer())) {
					currentInputsValues.put(input.getPlayer(), new HashMap<>());
					for (InputName name : InputName.values())
						currentInputsValues.get(input.getPlayer()).put(name, 0f);
				}
				currentInputsValues.get(input.getPlayer()).put(input.getName(), input.getValue());
			}
		}
		Map<Integer,Controls> controlsMap = new HashMap<>();
		currentInputsValues.forEach((id, currentInput) -> {
			Controls controls = new Controls(
					currentInput.get(InputName.RIGHT) - currentInput.get(InputName.LEFT),
					currentInput.get(InputName.FORWARD) - currentInput.get(InputName.BACKWARD),
					currentInput.get(InputName.UP) - currentInput.get(InputName.DOWN),
					currentInput.get(InputName.CAMERA_X_POS) - currentInput.get(InputName.CAMERA_X_NEG),
					currentInput.get(InputName.CAMERA_Y_POS) - currentInput.get(InputName.CAMERA_Y_NEG),
					currentInput.get(InputName.ACTION) > 0
			);
			controlsMap.put(id, controls);
		});
		return controlsMap;
	}

	public void startUpdateLoop() {
		new Timer().scheduleAtFixedRate(new TimerTask() {
			private long lastUpdateTPS = System.nanoTime();
			private int ticks = 0;

			public void run() {
				Map<Integer, Controls> controls = getControls();
				game.update(controls);
				updateListeners.forEach(l -> l.accept(controls));
				ticks++;
				if (System.nanoTime() - lastUpdateTPS > 1_000_000_000) {
					lastUpdateTPS = System.nanoTime();
					currentTPS = ticks;
					ticks = 0;
					System.out.println("TPS: " + currentTPS + ", FPS: " + Renderer.getFps());
				}
			}
		}, 1000 / TPS, 1000 / TPS);
	}

	public Game getGame() {
		return game;
	}

	public int getCurrentTPS() {
		return currentTPS;
	}

	public boolean addPlayer(int playerId) {
		return game.addPlayer(playerId);
	}

	public boolean removePlayer(int playerId) {
		return game.removePlayer(playerId);
	}

	public void addUpdateListener(Consumer<Map<Integer, Controls>> listener) {
		updateListeners.add(listener);
	}

}
