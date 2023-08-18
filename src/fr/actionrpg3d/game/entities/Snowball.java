package fr.actionrpg3d.game.entities;

import fr.actionrpg3d.game.collision.Prism;
import fr.actionrpg3d.game.collision.Shape;
import fr.actionrpg3d.math.Vector3f;

public class Snowball extends Projectile {

	private static final long serialVersionUID = 1L;
	private static final Prism hitbox = new Prism(new Shape.Rectangle(.2f, .2f), .2f);
	private static final String model = "snowball";
	private static final Vector3f friction = new Vector3f(1.1f, 1.05f, 1.1f);
	
	public Snowball(int id, Vector3f position, Vector3f acceleration) {
		super(id, position, hitbox, model, friction, acceleration);
	}
	
	public Snowball(Snowball original) {
		super(original);
	}
	
	@Override
	public Entity clone() {
		return new Snowball(this);
	}
	
}
