package fr.actionrpg3d.game;

import fr.actionrpg3d.math.Vector3f;
import fr.actionrpg3d.render.Model;

public interface Modelizable {

	public Model getModel();
	
	public Vector3f getRotation();
	
}
