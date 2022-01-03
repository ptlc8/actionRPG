package fr.actionrpg3d.game.entities;

import fr.actionrpg3d.inputs.Controls;
import fr.actionrpg3d.math.Vector3f;

public interface FirstPersonControlable extends Controlable {
	
	default void updateControlable(Controls controls) {
		Vector3f move = new Vector3f();
		Vector3f rot = getRotation();
		//if (Mouse.isButtonDown(0)) {
			rot.addX(-controls.getCameraXAxis()*2);
			rot.addY(-controls.getCameraYAxis()*3);
		//}
		if (controls.getForward()>0) {
			move.addZ((float)(Math.cos(Math.toRadians(rot.getY()))));
			move.addX((float)(Math.sin(Math.toRadians(rot.getY()))));
		}
		if (controls.getBackward()>0) {
			move.addZ((float)(-Math.cos(Math.toRadians(rot.getY()))));
			move.addX((float)(-Math.sin(Math.toRadians(rot.getY()))));
		}
		if (controls.getLeft()>0) {
			move.addX((float)(Math.cos(Math.toRadians(rot.getY()))));
			move.addZ((float)(-Math.sin(Math.toRadians(rot.getY()))));
		}
		if (controls.getRight()>0) {
			move.addX((float)(-Math.cos(Math.toRadians(rot.getY()))));
			move.addZ((float)(Math.sin(Math.toRadians(rot.getY()))));
		}
		if (controls.getDown()>0) {
			move.addY(-1);
		}
		if (!move.isZero()) move.normalize();
		move.mul(getSpeedValue());
		getAcceleration().add(move);
		//jump
		if (controls.getUp()>0 && getSolidAltitude() <= 0.01f) {
			getAcceleration().addY(.3f);
		}
	}
	
}
