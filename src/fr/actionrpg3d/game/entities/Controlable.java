package fr.actionrpg3d.game.entities;

import fr.actionrpg3d.inputs.Controls;
import fr.actionrpg3d.math.Vector3f;

public interface Controlable extends Moveable {
	
	void updateControlable(Controls controls);
	
	Vector3f getRotation();
	
	float getSpeedValue();
	
	int getControlsId();
	
}
