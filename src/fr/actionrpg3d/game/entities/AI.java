package fr.actionrpg3d.game.entities;

import java.util.List;

public interface AI extends Moveable {
	
	void updateAI(List<Entity> entities);
	
}
