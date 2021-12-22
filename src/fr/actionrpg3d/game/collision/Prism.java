package fr.actionrpg3d.game.collision;

public class Prism {
	
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
