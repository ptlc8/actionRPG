package fr.actionrpg3d.game.entities;

import fr.actionrpg3d.game.Game;
import fr.actionrpg3d.math.Vector3f;

public abstract class Entity {
	
	private static int kId = 0;

	private final int id;
	private final Game game;
	private Vector3f position;
	
	public Entity(Game game, Vector3f position) {
		this.id = kId++;
		this.game = game;
		this.position = position;
	}
	
	@Override
	public int hashCode() {
		return id;
	}
	
	public Game getGame() {
		return game;
	}
	
	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
}
