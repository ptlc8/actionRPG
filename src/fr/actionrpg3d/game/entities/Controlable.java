package fr.actionrpg3d.game.entities;

import fr.actionrpg3d.math.Vector3f;

public interface Controlable extends Moveable {
	
	void updateControlable();
	
	Vector3f getRotation();
	
	float getSpeedValue();
	
	boolean moveForward();
	
	boolean moveBackward();
	
	boolean moveLeft();
	
	boolean moveRight();
	
	boolean moveUp();
	
	boolean moveDown();
	
}
