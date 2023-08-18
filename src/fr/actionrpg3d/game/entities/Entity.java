package fr.actionrpg3d.game.entities;

import java.io.Serializable;

import fr.actionrpg3d.math.Vector3f;

public abstract class Entity implements Cloneable, Serializable {
	
	private static final long serialVersionUID = 1L;

	private final int id;
	private Vector3f position;
	
	public Entity(int id, Vector3f position) {
		this.id = id;
		this.position = position;
	}
	
	public Entity(Entity original) {
		this.id = original.id;
		this.position = original.position.clone();
	}
	
	@Override
	public int hashCode() {
		return id;
	}
	
	@Override
	public boolean equals(Object obj) {
		return this == obj || (obj instanceof Entity && ((Entity) obj).id == id);
	}
	
	public abstract Entity clone();
	
	public int getId() {
		return id;
	}
	
	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
}
