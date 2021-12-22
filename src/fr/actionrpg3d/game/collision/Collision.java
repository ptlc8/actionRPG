package fr.actionrpg3d.game.collision;

import fr.actionrpg3d.math.Vector2f;
import fr.actionrpg3d.math.Vector3f;

public class Collision {
	
	public static boolean has(Vector3f pos1, Prism prism1, float yaw1, Vector3f pos2, Prism prism2, float yaw2) {
		if (pos1.getY()+prism1.getHeight()/2 < pos2.getY()-prism2.getHeight()/2 || pos2.getY()+prism2.getHeight()/2 < pos1.getY()-prism1.getHeight()/2)
			return false;
		else
			return has(pos1.getVector2fFromXZ(), prism1.getShape(), yaw1, pos2.getVector2fFromXZ(), prism2.getShape(), yaw2);
	}
	
	public static boolean has(Vector2f pos1, Shape shape1, float rot1, Vector2f pos2, Shape shape2, float rot2) {
		
		if (shape1.size() == 0 || shape2.size() == 0) return false;
		
		if (Math.abs(pos1.length()-pos2.length()) > shape1.getMaxRadius()+shape2.getMaxRadius()) return false;
		
		for (int i = 0; i < shape1.size(); i++) { // pour toutes les faces de vertex1
			final Vector2f v1 = shape1.get(i), v2 = shape1.get(i == 0 ? shape1.size()-1 : i-1);
			
			float normalAngle = (float) Math.toDegrees(Math.atan2(v1.getY()-v2.getY(), v1.getX()-v2.getX())) + rot1;
			
			float min1 = Float.MAX_VALUE;
			float max1 = -Float.MAX_VALUE;
			for (Vector2f vertex : shape1) {
				Vector2f v = vertex.clone().rotate(rot1).add(pos1).rotate(normalAngle);
				if (v.getX() < min1) min1 = v.getX();
				if (v.getX() > max1) max1 = v.getX();
			}
			
			float min2 = Float.MAX_VALUE;
			float max2 = -Float.MAX_VALUE;
			for (Vector2f vertex : shape2) {
				Vector2f v = vertex.clone().rotate(rot2).add(pos2).rotate(normalAngle);
				if (v.getX() < min2) min2 = v.getX();
				if (v.getX() > max2) max2 = v.getX();
			}
			if (max1 < min2 || max2 < min1) return false;
			
		}
		
		for (int i = 0; i < shape2.size(); i++) { // pour toutes les faces de vertex2
			final Vector2f v1 = shape2.get(i), v2 = shape2.get(i == 0 ? shape2.size()-1 : i-1);
			
			float normalAngle = (float) Math.toDegrees(Math.atan2(v1.getY()-v2.getY(), v1.getX()-v2.getX())) + rot2;
			
			float min1 = Float.MAX_VALUE;
			float max1 = -Float.MAX_VALUE;
			for (Vector2f vertex : shape1) {
				Vector2f v = vertex.clone().rotate(rot1).add(pos1).rotate(normalAngle);
				if (v.getX() < min1) min1 = v.getX();
				if (v.getX() > max1) max1 = v.getX();
			}
			
			float min2 = Float.MAX_VALUE;
			float max2 = -Float.MAX_VALUE;
			for (Vector2f vertex : shape2) {
				Vector2f v = vertex.clone().rotate(rot2).add(pos2).rotate(normalAngle);
				if (v.getX() < min2) min2 = v.getX();
				if (v.getX() > max2) max2 = v.getX();
			}

			if (max1 < min2 || max2 < min1) return false;
			
		}
		
		return true;
	}
	
}
