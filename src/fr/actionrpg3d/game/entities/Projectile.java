package fr.actionrpg3d.game.entities;

import fr.actionrpg3d.game.Game;
import fr.actionrpg3d.game.collision.Prism;
import fr.actionrpg3d.math.Vector3f;
import fr.actionrpg3d.render.Model;

public class Projectile extends Entity implements Modelizable, Gravity, Tangible {

	private Prism hitbox;
	private Model model;
	private Vector3f friction;
	private Vector3f rotation;
	private Vector3f speed;
	private Vector3f acceleration;
	
	public Projectile(Game game, Vector3f position, Prism hitbox, Model model, Vector3f friction, Vector3f acceleration) {
		super(game, position);
		this.hitbox = hitbox;
		this.model = model;
		this.friction = friction;
		rotation = new Vector3f();
		speed = new Vector3f();
		this.acceleration = acceleration;
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
		return friction;
	}

	@Override
	public float getSolidAltitude() {
		return getPosition().getY();
	}

	@Override
	public Prism getHitbox() {
		return hitbox;
	}
	
}
