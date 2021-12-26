package fr.actionrpg3d.game.entities;

import fr.actionrpg3d.game.Game;
import fr.actionrpg3d.game.collision.Prism;
import fr.actionrpg3d.game.collision.Shape;
import fr.actionrpg3d.math.Vector3f;
import fr.actionrpg3d.render.Model;

public class Snowball extends Projectile {

	private static final Prism hitbox = new Prism(new Shape.Rectangle(.2f, .2f), .2f);
	private static final Model model = new Model("/models/snowball.model");
	private static final Vector3f friction = new Vector3f(1.1f, 1.05f, 1.1f);
	
	public Snowball(Game game, Vector3f position, Vector3f acceleration) {
		super(game, position, hitbox, model, friction, acceleration);
	}
	
}
