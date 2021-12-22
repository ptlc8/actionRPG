package fr.actionrpg3d.game.entities;

import org.lwjgl.input.Mouse;

import fr.actionrpg3d.math.Vector3f;

public interface FirstPersonControlable extends Controlable {
	
	default void updateControlable() {
		Vector3f move = new Vector3f();
		Vector3f rot = getRotation();
		//if (Mouse.isButtonDown(0)) {
			rot.addX(-Mouse.getDY()/2);
			rot.addY(-Mouse.getDX()/2);
		//}
		if (moveForward()) {
			move.addZ((float)(Math.cos(Math.toRadians(rot.getY()))));
			move.addX((float)(Math.sin(Math.toRadians(rot.getY()))));
		}
		if (moveBackward()) {
			move.addZ((float)(-Math.cos(Math.toRadians(rot.getY()))));
			move.addX((float)(-Math.sin(Math.toRadians(rot.getY()))));
		}
		if (moveLeft()) {
			move.addX((float)(Math.cos(Math.toRadians(rot.getY()))));
			move.addZ((float)(-Math.sin(Math.toRadians(rot.getY()))));
		}
		if (moveRight()) {
			move.addX((float)(-Math.cos(Math.toRadians(rot.getY()))));
			move.addZ((float)(Math.sin(Math.toRadians(rot.getY()))));
		}
		if (moveDown()) {
			move.addY(-1);
		}
		if (!move.isZero()) move.normalize();
		move.mul(getSpeedValue());
		getAcceleration().add(move);
		//jump
		if (moveUp() && getSolidAltitude() <= 0.01f) {
			getAcceleration().addY(.3f);
		}
	}
	
}
