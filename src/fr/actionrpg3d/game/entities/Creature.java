package fr.actionrpg3d.game.entities;

import fr.actionrpg3d.game.Game;
import fr.actionrpg3d.game.collision.Prism;
import fr.actionrpg3d.math.Vector3f;
import fr.actionrpg3d.render.Model;

public abstract class Creature extends Entity implements Moveable, Modelizable, Tangible, Gravity {
	
	private static final long serialVersionUID = 1L;
	
	private String model;
	private Vector3f rotation;
	private Vector3f speed;
	private Vector3f acceleration;
	private Prism hitbox;
	private static final Vector3f friction = new Vector3f(1.2f, 1.05f, 1.2f);
	
	private final int maxHealth;
	private int health;
	
	public Creature(int id, Vector3f position, String model, Prism hitbox, int health) {
		super(id, position);
		this.model = model;
		this.hitbox = hitbox;
		rotation = new Vector3f();
		speed = new Vector3f();
		acceleration = new Vector3f();
		this.health = this.maxHealth = health;
	}
	
	public Creature(Creature original) {
		super(original);
		model = original.model;
		rotation = original.rotation.clone();
		speed = original.speed.clone();
		acceleration = original.acceleration.clone();
		hitbox = original.hitbox;
		maxHealth = original.maxHealth;
		health = original.health;
	}

	@Override
	public Model getModel() {
		return Model.get(model);
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
	
	public int takeDamage(Game game, int damage) {
		health -= damage;
		onTakeDamage(game, damage);
		if (health <= 0) onDeath(game);
		return damage;
	}
	
	void onTakeDamage(Game game, int damage) {}
	
	public int heal(int heal) {
		health = Math.min(health+heal, maxHealth);
		onHeal(heal);
		return heal;
	}
	
	void onHeal(int heal) {}
	
	void onDeath(Game game) {
		game.removeEntity(this);
	}
	
}
