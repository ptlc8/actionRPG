package fr.actionrpg3d.game.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import fr.actionrpg3d.game.Game;
import fr.actionrpg3d.game.collision.Prism;
import fr.actionrpg3d.game.collision.Shape;
import fr.actionrpg3d.game.items.InfightWeapon;
import fr.actionrpg3d.game.items.Weapon;
import fr.actionrpg3d.math.Vector3f;
import fr.actionrpg3d.render.Model;

public class Player extends Creature implements FirstPersonControlable {
	
	private static final float speedValue = 0.02f;
	private static final int health = 100;
	private static final Prism hitbox = new Prism(new Shape.RegularPolygon(16, .5f, 22.5f), 2); // TODO : à adapter en cylindre
	private static final Weapon defaultWeapon = new InfightWeapon(new Model("/models/stick.model"), 2, 10, 60, 0.5f);
	
	private Weapon weapon = defaultWeapon; // @NonNullable
	private int cooldown = 0;
	
	public Player(Game game, Vector3f position, Model model) {
		super(game, position, model, hitbox, position, health);
	}
	
	private void attack() {
		if (cooldown >= weapon.getCooldown()) {
			weapon.attack(this, getGame(), getPosition(), new Vector3f(0, 0, 1).rotate(getRotation()));
			cooldown = 0;
		}
	}
	
	@Override
	public void updateControlable() {
		FirstPersonControlable.super.updateControlable();
		if (cooldown < weapon.getCooldown()) cooldown++;
		if (Mouse.isButtonDown(0)) {
			attack();
			// TODO : actions de clic
		}
	}
	
	public Vector3f getHandPosition() {
		return new Vector3f(-.8f, .8f, 1).rotate(getRotation().clone().setX(0).setZ(0));
	}
	
	@Override
	public float getSpeedValue() {
		return speedValue;
	}
	
	@Override
	public boolean moveForward() {
		return Keyboard.isKeyDown(Keyboard.KEY_Z);
	}
	@Override
	public boolean moveBackward() {
		return Keyboard.isKeyDown(Keyboard.KEY_S);
	}
	@Override
	public boolean moveLeft() {
		return Keyboard.isKeyDown(Keyboard.KEY_Q);
	}
	@Override
	public boolean moveRight() {
		return Keyboard.isKeyDown(Keyboard.KEY_D);
	}
	@Override
	public boolean moveUp() {
		return Keyboard.isKeyDown(Keyboard.KEY_SPACE);
	}
	@Override
	public boolean moveDown() {
		return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
	}
	
	public Weapon getWeapon() {
		return weapon;
	}
	
	public void setWeapon(Weapon weapon) {
		if (weapon==null) weapon = defaultWeapon;
		this.cooldown = 0;
		this.weapon = weapon;
	}
	
	public int getCooldown() {
		return cooldown;
	}
	
}
