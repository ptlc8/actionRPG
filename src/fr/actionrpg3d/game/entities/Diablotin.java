package fr.actionrpg3d.game.entities;

import fr.actionrpg3d.game.Game;
import fr.actionrpg3d.game.collision.Prism;
import fr.actionrpg3d.game.collision.Shape;
import fr.actionrpg3d.math.Vector3f;

public class Diablotin extends Enemy {
	
	private static final long serialVersionUID = 1L;
	private static final float speed = .025f;
	private static final int health = 12, sight = 22, reach = 2, maxReach = 1, cooldown = 60;
	private static final Prism hitbox = new Prism(new Shape.RegularPolygon(8, .5f), 1.9f); // TODO
	private static final String model = "robot";
	
	public Diablotin(int id, Vector3f position) {
		super(id, position, model, hitbox, health, speed, sight, reach, maxReach, cooldown);
	}
	
	public Diablotin(Diablotin original) {
		super(original);
	}

	@Override
	public void attack(Game game, Creature target) {
		super.attack(game, target);
		// TODO 

	}
	
	@Override
	public Entity clone() {
		return new Diablotin(this);
	}
	
}
