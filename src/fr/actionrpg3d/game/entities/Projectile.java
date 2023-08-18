package fr.actionrpg3d.game.entities;

import fr.actionrpg3d.game.collision.Prism;
import fr.actionrpg3d.math.Vector3f;
import fr.actionrpg3d.render.Model;

public class Projectile extends Entity implements Modelizable, Gravity, Tangible {

	private static final long serialVersionUID = 1L;
	
	private Prism hitbox;
	private String model;
	private Vector3f friction;
	private Vector3f rotation;
	private Vector3f speed;
	private Vector3f acceleration;
	
	public Projectile(int id, Vector3f position, Prism hitbox, String model, Vector3f friction, Vector3f acceleration) {
		super(id, position);
		this.hitbox = hitbox;
		this.model = model;
		this.friction = friction;
		rotation = new Vector3f();
		speed = new Vector3f();
		this.acceleration = acceleration;
	}
	
	public Projectile(Projectile original) {
		super(original);
		this.hitbox = original.hitbox;
		this.model = original.model;
		this.friction = original.friction.clone();
		this.rotation = original.rotation.clone();
		this.speed = original.speed.clone();
		this.acceleration = original.acceleration.clone();
	}
	
	@Override
	public Entity clone() {
		return new Projectile(this);
	}

	@Override
	public Model getModel() {
		return Model.get(model);
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
