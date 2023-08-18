package fr.actionrpg3d.game.entities;

import fr.actionrpg3d.game.Controls;
import fr.actionrpg3d.game.Game;
import fr.actionrpg3d.math.Vector3f;

public interface Controlable extends Moveable {
	
	void updateControlable(Game game, Controls controls);
	
	Vector3f getRotation();
	
	float getSpeedValue();
	
	int getControlsId();
	
}
