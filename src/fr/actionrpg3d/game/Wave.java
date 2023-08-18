package fr.actionrpg3d.game;

import java.util.ArrayList;

import fr.actionrpg3d.game.entities.Entity;
import fr.actionrpg3d.game.entities.Skeleton;
import fr.actionrpg3d.math.Vector3f;

public class Wave extends ArrayList<Entity> {
	
	private static final long serialVersionUID = 1L;
	
	private final int initialSize;
	
	public Wave(Game game, Room room, int size) {
		initialSize = size;
		for (int i = 0 ; i < size; i++) { // TODO : differentes créatures
			float x = room.getX()*2+1+(float)Math.random()*(room.getWidth()*2-2);
			float z = room.getZ()*2+1+(float)Math.random()*(room.getHeight()*2-2);
			Entity skeleton = new Skeleton(game.nextId(), new Vector3f(x, 0f, z));
			add(skeleton);
			game.addEntity(skeleton);
		}
	}
	
	public Wave(Wave original, Game game) {
		this.initialSize = original.initialSize;
	}
	
	public int getInitialSize() {
		return initialSize;
	}
	
	public Wave clone(Game game) {
		return new Wave(this, game);
	}
	
}
