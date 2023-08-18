package fr.actionrpg3d;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controllers;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import fr.actionrpg3d.game.Controls;
import fr.actionrpg3d.game.Game;
import fr.actionrpg3d.inputs.InputName;
import fr.actionrpg3d.inputs.PhysicInput;
import fr.actionrpg3d.inputs.PhysicInputsSource;
import fr.actionrpg3d.multiplayer.ClientGameManager;
import fr.actionrpg3d.multiplayer.SocketClient;
import fr.actionrpg3d.render.Camera;
import fr.actionrpg3d.render.FirstPersonCamera;
import fr.actionrpg3d.render.Renderer;
import fr.actionrpg3d.render.ThirdPersonCamera;

public class Client {

	private int selfId = 0;
	private GameManager game;
	private Camera camera;
	private PhysicInputsSource inputsSource;
	HashMap<InputName, Float> lastInputsStates = new HashMap<>();
	private SocketClient socket;
	
	private boolean echaped = false; // is the cursor free
	private boolean echaping = false; // the echap key was pressed or not, maybe temporary
	private boolean gui = true;

	public Client(boolean nogui) throws Exception {
		this.gui = !nogui;
		if (gui) {
			Renderer.init();
			Controllers.create();
			PhysicInput.refreshControllerIndex();
		}
		this.inputsSource = new PhysicInputsSource(new File("controls"));
		for (InputName name : InputName.values())
			lastInputsStates.put(name, 0f);
		
		camera = new FirstPersonCamera(1.6f);
		//camera = new ThirdPersonCamera(new Vector3f(0, 8, 0));
		//camera = new FreeCamera(new Vector3f(0, -5, 0));
		camera.setPerspectiveProjection(70f, 0.1f, 100f);
	}
	
	public Client() throws Exception {
		this(false);
	}

	public void enableCLI() {
		new Thread(() -> {
			@SuppressWarnings("resource")
			Scanner sc = new Scanner(System.in);
			while (true)
				socket.sendData(sc.nextLine());
		}).start();
	}
	
	public void joinServer() throws IOException {
		this.socket = new SocketClient();
		socket.addDataListener(this::onServerResponse);
		socket.addCloseListener(() -> System.out.println("Disconnected from server"));
		socket.connect(13028);
		System.out.println("Connected to server");
	}
	
	public void joinSolo() {
		selfId = 0;
		startGame(new SoloGameManager(new Game(-1984888624)));
	}

	private void onServerResponse(String data) {
		//System.out.println("Server : " + data);
		String[] args = data.split(" ");
		if ("self".equals(args[0]))
			selfId = Utils.parseInt(args[1]);
		if ("initgame".equals(args[0])) {
			startGame(new ClientGameManager(Game.fromBase64(data.replaceFirst("initgame ", "")), socket));
		}
	}

	private void onGameUpdate(Map<Integer, Controls> controls) {
		// extra inputs
		if (Keyboard.isCreated() && Mouse.isCreated()) {
			game.getGame().setDebug(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL));
			if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && !echaping) {
				echaping = true;
				echaped = !echaped;
				Mouse.setGrabbed(echaped);
			} else if (echaping && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				echaping = false;
			}
		}
		
		// inputs
		PhysicInput.refresh();
		for (InputName inputName : InputName.values()) {
			float value = inputsSource.getInputValue(inputName);
			if (value != lastInputsStates.get(inputName))
				game.receiveInput(selfId, inputName, inputsSource.getInputValue(inputName));
			lastInputsStates.put(inputName, value);
		}
		
		// camera
		if (camera instanceof FirstPersonCamera)
			((FirstPersonCamera)camera).setFollowed(game.getGame().getPlayers().get(selfId));
		if (camera instanceof ThirdPersonCamera)
			((ThirdPersonCamera)camera).setFollowed(game.getGame().getPlayers().get(selfId));
		camera.update(controls.get(selfId));
	}
	
	private void startGame(GameManager game) {
		this.game = game;
		game.addUpdateListener(this::onGameUpdate);
		
		if (camera instanceof FirstPersonCamera)
			((FirstPersonCamera)camera).setFollowed(game.getGame().getPlayers().get(selfId));
		if (camera instanceof ThirdPersonCamera)
			((ThirdPersonCamera)camera).setFollowed(game.getGame().getPlayers().get(selfId));
		
		game.startUpdateLoop();
		if (gui) {
			new Thread(() -> {
				try {
					Renderer.start(game.getGame(), camera);
				} catch (LWJGLException | InterruptedException e) {
					e.printStackTrace();
				}
			}, "Render").start();
		}
	}
	
	public GameManager getGameManager() {
		return game;
	}
	
	public int getSelfId() {
		return selfId;
	}
	
	public SocketClient getSocket() {
		return socket;
	}

}
