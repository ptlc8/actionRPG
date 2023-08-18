package fr.actionrpg3d.game.collision;

import java.io.Serializable;

public class Prism implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Shape shape;
	private float height;
	
	public Prism(Shape shape, float height) {
		this.shape = shape;
		this.height = height;
	}
	
	public Shape getShape() {
		return shape;
	}
	
	public float getHeight() {
		return height;
	}
	
}
