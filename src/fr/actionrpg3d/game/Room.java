package fr.actionrpg3d.game;

import java.util.List;

import fr.actionrpg3d.math.Vector2f;

public class Room {
	
	private int x, z, width, height;
	
	private List<Vector2f> doors;
	
	private boolean treasure = false;
	private int waves = 1;
	private int clearedWaves = 0;
	
	public Room(int x, int z, int width, int height, List<Vector2f> doors, boolean treasure, int waves) {
		this.x = x;
		this.z = z;
		this.width = width;
		this.height = height;
		this.doors = doors;
		this.treasure = treasure;
		this.waves = waves;
	}
	
	public int getX() {
		return x;
	}
	
	public int getZ() {
		return z;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public List<Vector2f> getDoors() {
		return doors;
	}

	public boolean isTreasure() {
		return treasure;
	}

	public void setTreasure(boolean treasure) {
		this.treasure = treasure;
	}

	public int getWavesNumber() {
		return waves;
	}

	public void setWavesNumber(int waves) {
		this.waves = waves;
	}

	public boolean isClear() {
		return waves == clearedWaves;
	}
	
	public Wave createWave(Game game) {
		return new Wave(game, this, 10); // TODO
	}
	
	public int getClearedWaves() {
		return clearedWaves;
	}
	
	public void clearWave() {
		clearedWaves++;
	}
}