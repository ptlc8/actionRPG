package fr.actionrpg3d.game.collision;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import fr.actionrpg3d.math.Vector2f;

public class Shape extends ArrayList<Vector2f> implements Serializable {

	private static final long serialVersionUID = 1L;

	private float maxRadius = 0;

	public Shape(Collection<Vector2f> vertex) {
		addAll(vertex);
		for (Vector2f v : this) {
			if (v.length() > maxRadius)
				maxRadius = v.length();
		}
	}

	private Shape(float maxRadius) {
		this.maxRadius = maxRadius;
	}

	public float getMaxRadius() {
		return maxRadius;
	}

	public static class Rectangle extends Shape {

		private static final long serialVersionUID = 1L;

		public Rectangle(float w, float h) {
			super((float) Math.sqrt(w * w + h * h) / 2);
			add(new Vector2f(-w / 2, -h / 2));
			add(new Vector2f(w / 2, -h / 2));
			add(new Vector2f(w / 2, h / 2));
			add(new Vector2f(-w / 2, h / 2));
		}
	}

	public static class RegularPolygon extends Shape {

		private static final long serialVersionUID = 1L;

		public RegularPolygon(int n, float r, float startAngle) {
			super(r);
			startAngle = (float) (startAngle * Math.PI / 180);
			for (int i = 0; i < n; i++) {
				add(new Vector2f((float) Math.cos(2 * Math.PI / n * i + startAngle) * r,
						(float) Math.sin(2 * Math.PI / n * i + startAngle) * r));
			}
		}

		public RegularPolygon(int n, float r) {
			this(n, r, 180f / n);
		}
	}

	public static class Circle extends Shape {

		private static final long serialVersionUID = 1L;

		private float r;

		public Circle(Vector2f c, float r) {
			super(r);
			this.r = r;
			add(c);
		}

		public float getRadius() {
			return r;
		}

		public Vector2f getCenter() {
			return get(0);
		}
	}

}
