package fr.actionrpg3d.game;

import fr.actionrpg3d.math.Vector3f;

public abstract class Entity {
	
	private Vector3f position;
	
	public Entity(Vector3f position) {
		this.position = position;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
}
