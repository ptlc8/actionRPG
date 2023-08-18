package fr.actionrpg3d.game.entities;

import fr.actionrpg3d.game.Game;
import fr.actionrpg3d.game.collision.Prism;
import fr.actionrpg3d.game.collision.Shape;
import fr.actionrpg3d.math.Vector3f;

public class Skeleton extends Enemy {
	
	private static final long serialVersionUID = 1L;
	private static final float speed = .01f;
	private static final int health = 10, sight = 16, reach = 4, maxReach = 2, cooldown = 120;
	private static final Prism hitbox = new Prism(new Shape.RegularPolygon(8, .5f), 1.9f);
	private static final String model = "skeleton";
	
	public Skeleton(int id, Vector3f position) {
		super(id, position, model, hitbox, health, speed, sight, reach, maxReach, cooldown);
	}
	
	public Skeleton(Skeleton original) {
		super(original);
	}

	@Override
	public void attack(Game game, Creature target) {
		super.attack(game, target);
		
	}
	
	@Override
	public Entity clone() {
		return new Skeleton(this);
	}

}
