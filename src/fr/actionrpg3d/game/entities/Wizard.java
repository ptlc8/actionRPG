package fr.actionrpg3d.game.entities;

import fr.actionrpg3d.game.Game;
import fr.actionrpg3d.game.collision.Prism;
import fr.actionrpg3d.game.collision.Shape;
import fr.actionrpg3d.math.Vector3f;
import fr.actionrpg3d.render.Model;

public class Wizard extends Enemy {

	private static final float speed = .015f;
	private static final int health = 20, sight = 22, reach = 16, maxReach = 10, cooldown = 120;
	private static final Prism hitbox = new Prism(new Shape.RegularPolygon(8, .5f, 0), 1.9f); // TODO
	
	public Wizard(Game game, Vector3f position) {
		super(game, position, new Model("/models/robot.model"), hitbox, new Vector3f(0, -.95f, 0), health, speed, sight, reach, maxReach, cooldown);
		
	}

	@Override
	public void attack(Creature target) {
		super.attack(target);
		// TODO 

	}

}