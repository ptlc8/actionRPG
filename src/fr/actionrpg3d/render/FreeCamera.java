package fr.actionrpg3d.render;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import fr.actionrpg3d.math.Vector3f;

public class FreeCamera extends Camera {
	
	public FreeCamera(Vector3f position) {
		super(position);
	}
	
	private float speed = .2f;

	@Override
	public void update() {
		Vector3f position = getPosition();
		Vector3f rotation = getRotation();
		if (Mouse.isButtonDown(0)) {
			rotation.addX(-Mouse.getDY());
			rotation.addY(Mouse.getDX());
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			position.addZ((float)(speed*Math.cos(Math.toRadians(rotation.getY()))));
			position.addX((float)(-speed*Math.sin(Math.toRadians(rotation.getY()))));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			position.addZ((float)(-speed*Math.cos(Math.toRadians(rotation.getY()))));
			position.addX((float)(speed*Math.sin(Math.toRadians(rotation.getY()))));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			position.addX((float)(speed*Math.cos(Math.toRadians(rotation.getY()))));
			position.addZ((float)(speed*Math.sin(Math.toRadians(rotation.getY()))));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			position.addX((float)(-speed*Math.cos(Math.toRadians(rotation.getY()))));
			position.addZ((float)(-speed*Math.sin(Math.toRadians(rotation.getY()))));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			position.addY(-speed);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			position.addY(speed);
		}
	}
}