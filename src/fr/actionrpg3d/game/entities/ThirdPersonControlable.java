package fr.actionrpg3d.game.entities;

import fr.actionrpg3d.math.Vector3f;

public interface ThirdPersonControlable extends Controlable {
	
	default void updateControlable() {
		Vector3f move = new Vector3f();
		if (moveForward()) {
			move.subZ(1f);
		}
		if (moveBackward()) {
			move.addZ(1f);
		}
		if (moveLeft()) {
			move.subX(1f);
		}
		if (moveRight()) {
			move.addX(1f);
		}
		if (moveDown()) {
			move.subY(1f);
		}
		if (!move.isZero()) move.normalize();
		move.mul(getSpeedValue());
		getAcceleration().add(move);
		//rotation
		getRotation().setY((float)Math.toDegrees(Math.atan2(move.getX(), move.getZ())));
		
		//jump
		if (moveUp() && getSolidAltitude() <= 0) {
			getAcceleration().addY(.3f);
		}
	}
	
}
