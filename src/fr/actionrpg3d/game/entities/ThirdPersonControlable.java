package fr.actionrpg3d.game.entities;

import fr.actionrpg3d.inputs.Controls;
import fr.actionrpg3d.math.Vector3f;

public interface ThirdPersonControlable extends Controlable {
	
	default void updateControlable(Controls controls) {
		Vector3f move = new Vector3f();
		if (controls.getForward()>0) {
			move.subZ(1f);
		}
		if (controls.getBackward()>0) {
			move.addZ(1f);
		}
		if (controls.getLeft()>0) {
			move.subX(1f);
		}
		if (controls.getRight()>0) {
			move.addX(1f);
		}
		if (controls.getDown()>0) {
			move.subY(1f);
		}
		if (!move.isZero()) move.normalize();
		move.mul(getSpeedValue());
		getAcceleration().add(move);
		//rotation
		getRotation().setY((float)Math.toDegrees(Math.atan2(move.getX(), move.getZ())));
		
		//jump
		if (controls.getUp()>0 && getSolidAltitude() <= 0) {
			getAcceleration().addY(.3f);
		}
	}
	
}
