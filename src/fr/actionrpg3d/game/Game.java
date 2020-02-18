package fr.actionrpg3d.game;

import java.util.ArrayList;
import java.util.List;

public class Game {
	
	private static int[][] map;
	
	private List<Entity> entities = new ArrayList<Entity>();
	
	public Game() {
		map = DungeonGenerator.generate(101, 101, 8, 15, 7, 51, 1);
		// TODO : rien pour l'instant
	}
	
	public void update() {
		
	}
	
	public int[][] getMap() {
		return map;
	}

	public List<Entity> getEntities() {
		return entities;
	}
	
}