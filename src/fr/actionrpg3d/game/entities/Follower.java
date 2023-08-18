package fr.actionrpg3d.game.entities;

import fr.actionrpg3d.game.Game;
import fr.actionrpg3d.math.Vector3f;
import fr.actionrpg3d.render.Model;

public class Follower extends Entity implements Modelizable, AI, Gravity {
	
	private static final long serialVersionUID = 1L;
	private static final float reach = 2f;
	private static final float speedValue = 0.01f;
	
	private String model;
	private Vector3f rotation;
	private Vector3f speed;
	private Vector3f acceleration;
	private int targetId;
	
	public Follower(int id, Vector3f position, String model, Entity target) {
		super(id, position);
		this.model = model;
		rotation = new Vector3f();
		speed = new Vector3f();
		acceleration = new Vector3f();
		this.targetId = target == null ? -1 : target.getId();
	}
	
	public Follower(Follower original) {
		super(original);
		this.model = original.model;
		this.rotation = original.rotation.clone();
		this.speed = original.speed.clone();
		this.acceleration = original.acceleration.clone();
		this.targetId = original.targetId;
	}
	
	@Override
	public void updateAI(Game game) {
		if (targetId == -1) return;
		Entity target = game.getEntities().get(targetId);
		Vector3f direction = new Vector3f(target.getPosition()).sub(getPosition());
		getRotation().setY((float)Math.toDegrees(Math.atan2(direction.getX(), direction.getZ())));
		if (direction.length() < reach) getAcceleration().set(0, 0, 0);
		else getAcceleration().add(direction.normalize().mul(speedValue));
	}
	
	@Override
	public Entity clone() {
		return new Follower(this);
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
		return new Vector3f(1.2f, 1.2f, 1.2f);
	}
	
	@Override
	public float getSolidAltitude() {
		return getPosition().getY();
	}
	
}
