package fr.actionrpg3d.game.entities;

import fr.actionrpg3d.game.Controls;
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
	private static final Prism hitbox = new Prism(new Shape.RegularPolygon(8, .5f), 2);
	private static final Weapon defaultWeapon = new InfightWeapon(new Model("/models/stick.model"), 2, 10, 60, 0.5f);
	
	private int playerId;
	private Weapon weapon = defaultWeapon; // @NonNullable
	private int cooldown = 0;
	
	public Player(Game game, int playerId, Vector3f position, Model model) {
		super(game, position, model, hitbox, health);
		this.playerId = playerId;
	}
	
	private void attack() {
		if (cooldown >= weapon.getCooldown()) {
			weapon.attack(this, getGame(), getPosition(), new Vector3f(0, 0, 1).rotate(getRotation()));
			cooldown = 0;
		}
	}
	
	@Override
	public void updateControlable(Controls controls) {
		FirstPersonControlable.super.updateControlable(controls);
		if (cooldown < weapon.getCooldown()) cooldown++;
		if (controls.getAction()) {
			attack();
		}
	}
	
	@Override
	public int getControlsId() {
		return playerId;
	}
	
	public Vector3f getHandPosition() {
		return new Vector3f(-.8f, .8f, 1).rotate(getRotation().clone().setX(0).setZ(0));
	}
	
	@Override
	public float getSpeedValue() {
		return speedValue;
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
