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
			for (int i = Math.min(dungeon.getHeight(),Math.max(0,(int)(getPosition().getZ()/2)-1)); i < Math.min(dungeon.getHeight(), Math.max(0, (int)(getPosition().getZ()/2)+2)); i++) {
				for (int j = Math.min(dungeon.getWidth(),Math.max(0,(int)(getPosition().getX()/2)-1)); j < Math.min(dungeon.getWidth(),Math.max(0,(int)(getPosition().getX()/2)+2)); j++) {
					if (dungeon.isWall(j, i)) {
						if (Collision.has(getPosition().clone().add(getSpeed())/*.add(((Tangible)this).getHitboxCenter())*/.getVector2fFromXZ(), ((Tangible)this).getHitbox().getShape(), ((Tangible)this).getRotation().getY(), new Vector2f(j*2, i*2), new Shape.Rectangle(2, 2), 0))
							return;
					}
				}
			}
		}
		getPosition().add(getSpeed());
	}
	
	float getSolidAltitude();
	
}
