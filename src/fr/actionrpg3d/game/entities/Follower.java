package fr.actionrpg3d.game.entities;

import java.util.List;

import fr.actionrpg3d.game.Game;
import fr.actionrpg3d.math.Vector3f;
import fr.actionrpg3d.render.Model;

public class Follower extends Entity implements Modelizable, AI, Gravity {
	
	private Model model;
	private Vector3f rotation;
	private Vector3f speed;
	private Vector3f acceleration;
	private Entity target;
	
	private static final float reach = 2f;
	private static final float speedValue = 0.01f;
	
	public Follower(Game game, Vector3f position, Model model, Entity target) {
		super(game, position);
		this.model = model;
		rotation = new Vector3f();
		speed = new Vector3f();
		acceleration = new Vector3f();
		this.target = target;
	}
	
	@Override
	public void updateAI(List<Entity> entities) {
		if (target == null) return;
		Vector3f direction = new Vector3f(target.getPosition()).sub(getPosition());
		getRotation().setY((float)Math.toDegrees(Math.atan2(direction.getX(), direction.getZ())));
		if (direction.length() < reach) getAcceleration().set(0, 0, 0);
		else getAcceleration().add(direction.normalize().mul(speedValue));
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
		return new Vector3f(1.2f, 1.2f, 1.2f);
	}
	
	public Entity getTarget() {
		return target;
	}
	
	@Override
	public float getSolidAltitude() {
		return getPosition().getY();
	}
	
}
