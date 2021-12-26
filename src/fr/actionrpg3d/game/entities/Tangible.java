package fr.actionrpg3d.game.entities;

import fr.actionrpg3d.game.collision.Prism;
import fr.actionrpg3d.math.Vector3f;

public interface Tangible {
	
	Prism getHitbox();
	
	Vector3f getRotation();
	
}
