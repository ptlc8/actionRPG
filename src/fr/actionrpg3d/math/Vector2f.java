package fr.actionrpg3d.math;

public class Vector2f {
	
	private float x, y;
	
	public Vector2f() {
		x = 0;
		y = 0;
	}
	
	public Vector2f(Vector2f v) {
		this.x = v.x;
		this.y = v.y;
	}
	
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2f set(Vector2f v) {
		this.x = v.x;
		this.y = v.y;
		return this;
	}
	
	public Vector2f set(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	public float length() {
		return (float)Math.sqrt(x*x + y*y);
	}
	
	public Vector2f normalize() {
		this.x /= length();
		this.y /= length();
		return this;
	}
	
	public Vector2f add(Vector2f v) {
		this.x += v.x;
		this.y += v.y;
		return this;
	}
	
	public Vector2f sub(Vector2f v) {
		this.x -= v.x;
		this.y -= v.y;
		return this;
	}
	
	public Vector2f mul(Vector2f v) {
		this.x *= v.x;
		this.y *= v.y;
		return this;
	}
	
	public Vector2f mul(float f) {
		this.x *= f;
		this.y *= f;
		return this;
	}
	
	public Vector2f div(Vector2f v) {
		this.x /= v.x;
		this.y /= v.y;
		return this;
	}
	
	public Vector2f div(float f) {
		this.x /= f;
		this.y /= f;
		return this;
	}
	
	@Deprecated
	public Vector2f rotate(Vector2f rot) {
		// https://en.wikipedia.org/wiki/Rotation_matrix
		Vector2f v = new Vector2f(this);
		float cosRx = (float)Math.cos(Math.toRadians(rot.x)), cosRy = (float)Math.cos(Math.toRadians(rot.y));
		float sinRx = (float)Math.sin(Math.toRadians(rot.x)), sinRy = (float)Math.sin(Math.toRadians(rot.y));
		v.setX(cosRy*x+(sinRy*sinRx-cosRx)*y+(sinRy*cosRx+sinRx));
		v.setY(cosRy*x+(sinRy*sinRx+cosRx)*y+(sinRy*cosRx-sinRx));
		setX(v.x);
		setY(v.y);
		return this;
	}
	
	public Vector2f rotate(float rot) {
		Vector2f v = new Vector2f(this);
		float cosR = (float) Math.cos(Math.toRadians(rot));
		float sinR = (float) Math.sin(Math.toRadians(rot));
		v.setX(x*cosR - y*sinR);
		v.setY(x*sinR + y*cosR);
		set(v.x, v.y);
		return this;
	}
	
	public Vector2f rotate(float rot, Vector2f center) {
		Vector2f v = new Vector2f(this);
		float cosR = (float) Math.cos(Math.toRadians(rot));
		float sinR = (float) Math.sin(Math.toRadians(rot));
		sub(center);
		v.setX(x*cosR - y*sinR);
		v.setY(x*sinR + y*cosR);
		set(v.x, v.y);
		add(center);
		return this;
	}
	
	public boolean isZero() {
		return x == 0 && y == 0;
	}
	
	public Vector2f clone() {
		return new Vector2f(x, y);
	}
	
	// ---- X

	public float getX() {
		return x;
	}

	public Vector2f setX(float x) {
		this.x = x;
		return this;
	}
	
	public Vector2f addX(float x) {
		this.x += x;
		return this;
	}
	
	public Vector2f subX(float x) {
		this.x -= x;
		return this;
	}
	
	// ---- Y

	public float getY() {
		return y;
	}

	public Vector2f setY(float y) {
		this.y = y;
		return this;
	}
	
	public Vector2f addY(float y) {
		this.y += y;
		return this;
	}
	
	public Vector2f subY(float y) {
		this.y -= y;
		return this;
	}
	
	public Vector2f mulY(float y) {
		this.y *= y;
		return this;
	}
	
	@Override
	public String toString() {
		return "("+x+","+y+")";
	}
	
}
