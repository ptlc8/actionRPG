package fr.actionrpg3d.game.items;

import fr.actionrpg3d.game.Game;
import fr.actionrpg3d.game.entities.Creature;
import fr.actionrpg3d.game.entities.Entity;
import fr.actionrpg3d.math.Vector3f;
import fr.actionrpg3d.render.Model;

public class InfightWeapon extends Weapon {
	
	private final int reach;
	private final int damage;
	private final float knockback;
	
	public InfightWeapon(Model model, int reach, int damage, int cooldown, float knockback) {
		super(model, cooldown);
		this.reach = reach;
		this.damage = damage;
		this.knockback = knockback;
	}

	@Override
	public void attack(Creature attacker, Game game, Vector3f position, Vector3f direction) {
		for (Entity entity : game.getEntities()) {
			if (entity == attacker) continue;
			if (entity instanceof Creature) {
				Creature creature = (Creature)entity;
				Vector3f diff = creature.getPosition().clone().sub(position);
				if (diff.length() > reach) continue;
				diff.normalize();
				//if (diff.clone().sub(direction).length() > 1) continue;
				creature.getAcceleration().add(diff.clone().mul(knockback));
				creature.takeDamage(damage);
			}
		}
	}
	
	public int getReach() {
		return reach;
	}
	
	public int getDamage() {
		return damage;
	}
	
}
