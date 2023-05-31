package fr.actionrpg3d.render;

import fr.actionrpg3d.game.Controls;
import fr.actionrpg3d.math.Vector3f;

public class FreeCamera extends Camera {
	
	public FreeCamera(Vector3f position) {
		super(position);
	}
	
	private float speed = .2f;

	@Override
	public void update(Controls controls) {
		Vector3f position = getPosition();
		Vector3f rotation = getRotation();
		// control rotation
		rotation.addX(-controls.getCameraXAxis()*2);
		rotation.addY(controls.getCameraYAxis()*3);
		// move forward and backward
		position.addZ(controls.getBackwardForwardAxis()*speed*(float)(Math.cos(Math.toRadians(rotation.getY()))));
		position.addX(-controls.getBackwardForwardAxis()*speed*(float)(Math.sin(Math.toRadians(rotation.getY()))));
		// move right and left
		position.addX(-speed*controls.getLeftRightAxis()*(float)(Math.cos(Math.toRadians(rotation.getY()))));
		position.addZ(-speed*controls.getLeftRightAxis()*(float)(Math.sin(Math.toRadians(rotation.getY()))));
		// move up and down
		position.addY(-speed*controls.getDownUpAxis());
	}
}