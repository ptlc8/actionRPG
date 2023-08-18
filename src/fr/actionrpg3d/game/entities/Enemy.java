package fr.actionrpg3d.game.entities;

import fr.actionrpg3d.game.Game;
import fr.actionrpg3d.game.collision.Prism;
import fr.actionrpg3d.math.Vector3f;

public abstract class Enemy extends Creature implements AI {
	
	private static final long serialVersionUID = 1L;
	
	private float speedValue;
	private int sight;
	private int reach;
	private int maxReach;
	private final int cooldownMax;
	private int cooldown;
	
	private float direction = 0;
	
	public Enemy(int id, Vector3f position, String model, Prism hitbox, int health, float speed, int sight, int reach, int maxReach, int cooldown) {
		super(id, position, model, hitbox, health);
		this.speedValue = speed;
		this.sight = sight;
		this.reach = reach;
		this.maxReach = maxReach;
		this.cooldown = this.cooldownMax = cooldown;
	}
	
	public Enemy(Enemy original) {
		super(original);
		speedValue = original.speedValue;
		sight = original.sight;
		reach = original.reach;
		maxReach = original.maxReach;
		cooldownMax = original.cooldownMax;
		cooldown = original.cooldown;
		direction = original.direction;
	}
	
	@Override
	public void updateAI(Game game) {
		if (cooldown < cooldownMax) cooldown++;
		float distanceNearestEntity = Float.MAX_VALUE;
		Creature nearestEntity = null;
		for (Entity entity : game.getEntities().values()) {
			if (entity instanceof Player) {// TODO : playerTeam interface
				float distance = entity.getPosition().clone().sub(getPosition()).length();
				if (distance < distanceNearestEntity) {
					distanceNearestEntity = distance;
					nearestEntity = (Creature)entity;
				}
			}
		}
		if (distanceNearestEntity > sight) {
			// idle
		} else {
			if (distanceNearestEntity > reach) {
				goOnTarget(nearestEntity);
			} else if (distanceNearestEntity < maxReach) {
				goBack(nearestEntity);
			} else {
				if (canAttack())
					attack(game, nearestEntity);
			}
		}
	}
	
	void goOnTarget(Creature target) {
		direction = (float) Math.atan2(target.getPosition().getX()-getPosition().getX(), target.getPosition().getZ()-getPosition().getZ());
		getAcceleration().addX((float)(Math.sin(direction)*speedValue)).addZ((float)(Math.cos(direction)*speedValue));
		getRotation().setY((float)Math.toDegrees(direction));
	}
	
	void goBack(Creature target) {
		direction = (float) Math.atan2(getPosition().getX()-target.getPosition().getX(), getPosition().getZ()-target.getPosition().getZ());
		getAcceleration().addX((float)(Math.sin(direction)*speedValue)).addZ((float)(Math.cos(direction)*speedValue));
		getRotation().setY((float)Math.toDegrees(direction));
	}
	
	void attack(Game game, Creature target) {
		direction = (float) Math.atan2(target.getPosition().getX()-getPosition().getX(), target.getPosition().getZ()-getPosition().getZ());
		getRotation().setY((float)Math.toDegrees(direction));
		// TODO : no default attack ?
		//getAcceleration().addY(.3f);// TODO : debug only
		cooldown = 0;
	}
	
	public boolean canAttack() {
		return cooldown >= cooldownMax;
	}
	
}