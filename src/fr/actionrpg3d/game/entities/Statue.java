package fr.actionrpg3d.game.entities;

import fr.actionrpg3d.game.Game;
import fr.actionrpg3d.game.collision.Prism;
import fr.actionrpg3d.game.collision.Shape;
import fr.actionrpg3d.math.Vector3f;
import fr.actionrpg3d.render.Model;

public class Statue extends Enemy {
	
	private static final float speed = .015f;
	private static final int health = 50, sight = 22, reach = 3, maxReach = 0, cooldown = 180;
	private static final Prism hitbox = new Prism(new Shape.RegularPolygon(8, .5f, 0), 1.9f); // TODO
	
	public Statue(Game game, Vector3f position) {
		super(game, position, new Model("/models/statue.model"), hitbox, new Vector3f(0, -.95f, 0), health, speed, sight, reach, maxReach, cooldown);
		
	}
	
	@Override
	public void goOnTarget(Creature target) {
		if (isSeenBy(target)) return;
		super.goOnTarget(target);
	}

	@Override
	public void attack(Creature target) {
		if (isSeenBy(target)) return;
		super.attack(target);
		// TODO 

	}
	
	private boolean isSeenBy(Creature creature) {
		return Math.cos(Math.atan2(creature.getPosition().getX()-getPosition().getX(),creature.getPosition().getZ()-getPosition().getZ())-Math.toRadians(creature.getRotation().getY()))<0;
	}

}
