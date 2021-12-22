package fr.actionrpg3d.game.entities;

public interface Gravity extends Moveable {
	
	public default void updateGravity() {
		if (getSolidAltitude() + getSpeed().getY() > 0) {
			getAcceleration().addY(-.01f);
		} else {
			getSpeed().setY(-getSolidAltitude());
		}
	}
	
}
