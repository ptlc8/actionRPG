package fr.actionrpg3d.game.entities;

import fr.actionrpg3d.game.Dungeon;
import fr.actionrpg3d.game.collision.Collision;
import fr.actionrpg3d.game.collision.Shape;
import fr.actionrpg3d.math.Vector2f;
import fr.actionrpg3d.math.Vector3f;

public interface Moveable {
	
	Vector3f getPosition();
	
	Vector3f getSpeed();
	
	Vector3f getAcceleration();
	
	Vector3f getFriction();
	
	public default void updateMove(Dungeon dungeon) {
		getSpeed().add(getAcceleration()).div(getFriction());
		getAcceleration().set(0, 0, 0);
		if (this instanceof Tangible) {
			for (int i = Math.min(dungeon.getHeight(),Math.max(0,(int)(getPosition().getZ()/2)-1)); i < Math.min(dungeon.getHeight(), Math.max(0, (int)(getPosition().getZ()/2)+3)); i++) {
				for (int j = Math.min(dungeon.getWidth(),Math.max(0,(int)(getPosition().getX()/2)-1)); j < Math.min(dungeon.getWidth(),Math.max(0,(int)(getPosition().getX()/2)+3)); j++) {
					if (dungeon.isWall(j, i)) {
						if (Collision.has(getPosition().clone().addZ(getSpeed().getZ()).getVector2fFromXZ(), ((Tangible)this).getHitbox().getShape(), 0, new Vector2f(j*2, i*2), new Shape.Rectangle(2, 2), 0)) {
							getSpeed().setZ(0);
						}
						if (Collision.has(getPosition().clone().addX(getSpeed().getX()).getVector2fFromXZ(), ((Tangible)this).getHitbox().getShape(), 0, new Vector2f(j*2, i*2), new Shape.Rectangle(2, 2), 0)) {
							getSpeed().setX(0);
						}
					}
				}
			}
		}
		getPosition().add(getSpeed());
	}
	
	float getSolidAltitude();
	
}
