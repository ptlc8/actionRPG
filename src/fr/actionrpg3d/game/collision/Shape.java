package fr.actionrpg3d.game.collision;

import java.util.List;
import java.util.ArrayList;

import fr.actionrpg3d.math.Vector2f;

@SuppressWarnings("serial")
public class Shape extends ArrayList<Vector2f> {
	
	private float maxRadius = 0;
	//private int minRadius = 0;
	
	public Shape(List<Vector2f> vertex) {
		addAll(vertex);
		for (Vector2f v : this) {
			if (v.length() > maxRadius) maxRadius = v.length();
		}
	}
	
	private Shape(float maxRadius) {
		this.maxRadius = maxRadius;
	}
	
	public float getMaxRadius() {
		return maxRadius;
	}
	
	public static class Rectangle extends Shape {
		public Rectangle(float w, float h) {
			super((float)Math.sqrt(w*w+h*h)/2);
			add(new Vector2f(-w/2, -h/2));
			add(new Vector2f(w/2, -h/2));
			add(new Vector2f(w/2, h/2));
			add(new Vector2f(-w/2, h/2));
		}
	}
	
	public static class RegularPolygon extends Shape {
		public RegularPolygon(int n, float r, float startAngle) {
			super(r);
			startAngle = (float) (startAngle*Math.PI/180);
			for (int i = 0; i < n; i++) {
				add(new Vector2f((float)Math.cos(2*Math.PI/n*i+startAngle)*r, (float)Math.sin(2*Math.PI/n*i+startAngle)*r));
			}
		}
	}
	
	public static class Circle extends Shape {
		private float r;
		public Circle(Vector2f c, float r) {
			super(r);
			this.r = r;
			add(c);
		}
		public float getRadius() {
			return r;
		}
	}
	
}
