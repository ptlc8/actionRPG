package fr.actionrpg3d.inputs;

public enum Action {
	
	LEFT("left"),
	RIGHT("right"),
	BACKWARD("backward"),
	FORWARD("forward"),
	DOWN("down"),
	UP("up"),
	CAMERA_X_POS("cameraXpos"),
	CAMERA_X_NEG("cameraXneg"),
	CAMERA_Y_POS("cameraYpos"),
	CAMERA_Y_NEG("cameraYneg"),
	ACTION("action");
	
	
	private String name;
	
	Action(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
}
