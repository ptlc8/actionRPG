package fr.actionrpg3d.game.entities;

import fr.actionrpg3d.game.Game;
import fr.actionrpg3d.math.Vector3f;
import fr.actionrpg3d.render.Model;

public class Snowball extends Entity implements Modelizable, Gravity {
	
	Model model;
	Vector3f rotation;
	Vector3f speed;
	Vector3f acceleration;
	
	public Snowball(Game game, Vector3f position) {
		super(game, position);
		this.model = new Model("/models/snowball.model");
		rotation = new Vector3f();
		speed = new Vector3f();
		acceleration = new Vector3f();
	}
	

	@Override
	public Model getModel() {
		return model;
	}

	@Override
	public Vector3f getRotation() {
		return rotation;
	}

	@Override
	public Vector3f getSpeed() {
		return speed;
	}

	@Override
	public Vector3f getAcceleration() {
		return acceleration;
	}

	@Override
	public Vector3f getFriction() {
		return new Vector3f(1.1f, 1.05f, 1.1f);
	}

	@Override
	public float getSolidAltitude() {
		return getPosition().getY();
	}
	
}
