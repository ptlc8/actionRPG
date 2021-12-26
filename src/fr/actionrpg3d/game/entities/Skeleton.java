package fr.actionrpg3d.game.entities;

import fr.actionrpg3d.game.Game;
import fr.actionrpg3d.game.collision.Prism;
import fr.actionrpg3d.game.collision.Shape;
import fr.actionrpg3d.math.Vector3f;
import fr.actionrpg3d.render.Model;

public class Skeleton extends Enemy {
	
	private static final float speed = .01f;
	private static final int health = 10, sight = 16, reach = 4, maxReach = 2, cooldown = 120;
	private static final Prism hitbox = new Prism(new Shape.RegularPolygon(8, .5f), 1.9f);
	
	public Skeleton(Game game, Vector3f position) {
		super(game, position, new Model("/models/skeleton.model"), hitbox, new Vector3f(0, -.95f, 0), health, speed, sight, reach, maxReach, cooldown);
		
	}

	@Override
	public void attack(Creature target) {
		super.attack(target);
		
	}

}
