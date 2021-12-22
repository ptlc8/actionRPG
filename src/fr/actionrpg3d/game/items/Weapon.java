package fr.actionrpg3d.game.items;

import fr.actionrpg3d.game.Game;
import fr.actionrpg3d.game.entities.Creature;
import fr.actionrpg3d.math.Vector3f;
import fr.actionrpg3d.render.Model;

public abstract class Weapon {
	
	private Model model;
	
	private final int cooldown;
	
	public Weapon(Model model, int cooldown) {
		this.model = model;
		this.cooldown = cooldown;
	}
	
	public abstract void attack(Creature attacker, Game game, Vector3f position, Vector3f direction);
	
	public Model getModel() {
		return model;
	}
	
	public int getCooldown() {
		return cooldown;
	}
	
}
