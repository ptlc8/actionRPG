package fr.actionrpg3d.game.entities;

import java.util.List;

import fr.actionrpg3d.game.Game;
import fr.actionrpg3d.game.collision.Prism;
import fr.actionrpg3d.math.Vector3f;
import fr.actionrpg3d.render.Model;

public abstract class Enemy extends Creature implements AI {
	
	private float speedValue;
	private int sight;
	private int reach;
	private int maxReach;
	private final int cooldownMax;
	private int cooldown;
	
	private float direction = 0;
	
	public Enemy(Game game, Vector3f position, Model model, Prism hitbox, int health, float speed, int sight, int reach, int maxReach, int cooldown) {
		super(game, position, model, hitbox, health);
		this.speedValue = speed;
		this.sight = sight;
		this.reach = reach;
		this.maxReach = maxReach;
		this.cooldown = this.cooldownMax = cooldown;
	}
	
	public void updateAI(List<Entity> entities) {
		if (cooldown < cooldownMax) cooldown++;
		float distanceNearestEntity = Float.MAX_VALUE;
		Creature nearestEntity = null;
		for (Entity entity : entities) {
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
					attack(nearestEntity);
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
	
	void attack(Creature target) {
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