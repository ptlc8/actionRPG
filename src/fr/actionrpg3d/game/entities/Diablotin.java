package fr.actionrpg3d.game.entities;

import fr.actionrpg3d.game.Game;
import fr.actionrpg3d.game.collision.Prism;
import fr.actionrpg3d.game.collision.Shape;
import fr.actionrpg3d.math.Vector3f;
import fr.actionrpg3d.render.Model;

public class Diablotin extends Enemy {
	
	private static final float speed = .025f;
	private static final int health = 12, sight = 22, reach = 2, maxReach = 1, cooldown = 60;
	private static final Prism hitbox = new Prism(new Shape.RegularPolygon(8, .5f), 1.9f); // TODO
	
	public Diablotin(Game game, Vector3f position) {
		super(game, position, new Model("/models/robot.model"), hitbox, new Vector3f(0, -.95f, 0), health, speed, sight, reach, maxReach, cooldown);
		
	}

	@Override
	public void attack(Creature target) {
		super.attack(target);
		// TODO 

	}
	
}
