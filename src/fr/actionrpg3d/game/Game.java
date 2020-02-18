package fr.actionrpg3d.game;

import java.util.ArrayList;
import java.util.List;

public class Game {
	
	private List<Entity> entities = new ArrayList<Entity>();
	
	public Game() {
		// TODO : rien pour l'instant
	}
	
	public void update() {
		
	}

	public List<Entity> getEntities() {
		return entities;
	}
	
}