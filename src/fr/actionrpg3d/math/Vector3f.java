package fr.actionrpg3d.math;

public class Vector3f {
	
	private float x, y, z;
	
	public Vector3f() {
		x = 0;
		y = 0;
		z = 0;
	}
	
	public Vector3f(Vector3f v) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}
	
	public Vector3f(float x, float y,  float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public float length() {
		return (float)Math.sqrt(x*x + y*y + z*z);
	}
	
	public Vector3f normalize() {
		this.x /= length();
		this.y /= length();
		this.z /= length();
		return this;
	}
	
	public Vector3f add(Vector3f v) {
		this.x += v.x;
		this.y += v.y;
		this.z += v.z;
		return this;
	}
	
	public Vector3f sub(Vector3f v) {
		this.x -= v.x;
		this.y -= v.y;
		this.z -= v.z;
		return this;
	}
	
	public Vector3f mul(Vector3f v) {
		this.x *= v.x;
		this.y *= v.y;
		this.z *= v.z;
		return this;
	}
	
	public Vector3f div(Vector3f v) {
		this.x /= v.x;
		this.y /= v.y;
		this.z /= v.z;
		return this;
	}
	
	// ---- X

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}
	
	public Vector3f addX(float x) {
		this.x += x;
		return this;
	}
	
	public Vector3f subX(float x) {
		this.x -= x;
		return this;
	}
	
	// ---- Y

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public Vector3f addY(float y) {
		this.y += y;
		return this;
	}
	
	public Vector3f subY(float y) {
		this.y -= y;
		return this;
	}
	
	// ---- Z

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}
	
	public Vector3f addZ(float z) {
		this.z += z;
		return this;
	}
	
	public Vector3f subZ(float z) {
		this.z -= z;
		return this;
	}
	
}
