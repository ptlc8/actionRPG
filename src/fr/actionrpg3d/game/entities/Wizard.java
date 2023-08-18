package fr.actionrpg3d.game.entities;

import fr.actionrpg3d.game.Game;
import fr.actionrpg3d.game.collision.Prism;
import fr.actionrpg3d.game.collision.Shape;
import fr.actionrpg3d.math.Vector3f;

public class Wizard extends Enemy {

	private static final long serialVersionUID = 1L;
	private static final float speed = .015f;
	private static final int health = 20, sight = 22, reach = 16, maxReach = 10, cooldown = 120;
	private static final Prism hitbox = new Prism(new Shape.RegularPolygon(8, .5f), 1.9f); // TODO
	private static final String model = "robot";
	
	public Wizard(int id, Vector3f position) {
		super(id, position, model, hitbox, health, speed, sight, reach, maxReach, cooldown);
	}
	
	public Wizard(Wizard original) {
		super(original);
	}

	@Override
	public void attack(Game game, Creature target) {
		super.attack(game, target);
		Vector3f direction = new Vector3f(target.getPosition());
		direction.sub(getPosition());
		direction.normalize();
		direction.mul(2);
		direction.addY(0.1f);
		game.addEntity(new Snowball(game.nextId(), new Vector3f(getPosition()).addY(1), direction));
	}
	
	@Override
	public Entity clone() {
		return new Wizard(this);
	}

}