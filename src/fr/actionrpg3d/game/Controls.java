package fr.actionrpg3d.game;

public class Controls {
	
	private float leftRightAxis=0, backwardForwardAxis=0, downUpAxis=0, cameraXAxis=0, cameraYAxis=0;
	private boolean action=false;
	
	public Controls(float leftRightAxis, float backwardForwardAxis, float downUpAxis, float cameraXAxis, float cameraYAxis, boolean action) {
		this.leftRightAxis = leftRightAxis;
		this.backwardForwardAxis = backwardForwardAxis;
		this.downUpAxis = downUpAxis;
		this.cameraXAxis = cameraXAxis;
		this.cameraYAxis = cameraYAxis;
		this.action = action;
	}
	
	public float getLeftRightAxis() {
		return leftRightAxis;
	}
	public float getLeft() {
		return Math.max(0, -leftRightAxis);
	}
	public float getRight() {
		return Math.max(0, leftRightAxis);
	}
	
	public float getBackwardForwardAxis() {
		return backwardForwardAxis;
	}
	public float getForward() {
		return Math.max(0, backwardForwardAxis);
	}
	public float getBackward() {
		return Math.max(0, -backwardForwardAxis);
	}
	
	public float getDownUpAxis() {
		return downUpAxis;
	}
	public float getUp() {
		return Math.max(0, downUpAxis);
	}
	public float getDown() {
		return Math.max(0, -downUpAxis);
	}
	
	public float getCameraXAxis() {
		return cameraXAxis;
	}
	
	public float getCameraYAxis() {
		return cameraYAxis;
	}
	
	public boolean getAction() {
		return action;
	}
	
}