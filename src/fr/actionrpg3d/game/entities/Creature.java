package fr.actionrpg3d.game.entities;

import fr.actionrpg3d.game.Game;
import fr.actionrpg3d.game.collision.Prism;
import fr.actionrpg3d.math.Vector3f;
import fr.actionrpg3d.render.Model;

public abstract class Creature extends Entity implements Moveable, Modelizable, Tangible, Gravity {
	
	private Model model;
	private Vector3f rotation;
	private Vector3f speed;
	private Vector3f acceleration;
	private Prism hitbox;
	private static final Vector3f friction = new Vector3f(1.2f, 1.05f, 1.2f);
	
	private final int maxHealth;
	private int health;
	
	public Creature(Game game, Vector3f position, Model model, Prism hitbox, int health) {
		super(game, position);
		this.model = model;
		this.hitbox = hitbox;
		rotation = new Vector3f();
		speed = new Vector3f();
		acceleration = new Vector3f();
		this.health = this.maxHealth = health;
	}

	@Override
	public Model getModel() {
		return model;
	}

	
	@Override
	public Vector3f getRotation() {
		return rotation;
	}
	
	@Override
	public Vector3f getSpeed() {
		return speed;
	}
	
	@Override
	public Vector3f getAcceleration() {
		return acceleration;
	}
	
	@Override
	public Vector3f getFriction() {
		return friction;
	}
	
	@Override
	public float getSolidAltitude() {
		return getPosition().getY();
	}

	@Override
	public Prism getHitbox() {
		return hitbox;
	}
	
	public int getMaxHealth() {
		return maxHealth;
	}
	
	public int getHealth() {
		return health;
	}
	
	public int takeDamage(int damage) {
		health -= damage;
		onTakeDamage(damage);
		if (health <= 0) onDeath();
		return damage;
	}
	
	void onTakeDamage(int damage) {}
	
	public int heal(int heal) {
		health = Math.min(health+heal, maxHealth);
		onHeal(heal);
		return heal;
	}
	
	void onHeal(int heal) {}
	
	void onDeath() {
		//getGame().getEntities().remove(this); TODO : sync
	}
	
}
