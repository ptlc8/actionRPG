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
	
	public Vector3f set(Vector3f v) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
		return this;
	}
	
	public Vector3f set(float x, float y,  float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	
	public float length() {
		return (float)Math.sqrt(x*x + y*y + z*z);
	}
	
	public Vector3f normalize() {
		float l = length();
		this.x /= l;
		this.y /= l;
		this.z /= l;
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
	
	public Vector3f mul(float f) {
		this.x *= f;
		this.y *= f;
		this.z *= f;
		return this;
	}
	
	public Vector3f div(Vector3f v) {
		this.x /= v.x;
		this.y /= v.y;
		this.z /= v.z;
		return this;
	}
	
	public Vector3f rotate(Vector3f rot) {
		// https://en.wikipedia.org/wiki/Rotation_matrix
		Vector3f v = new Vector3f(this);
		float cosRx = (float)Math.cos(Math.toRadians(rot.x)), cosRy = (float)Math.cos(Math.toRadians(rot.y)), cosRz = (float)Math.cos(Math.toRadians(rot.z));
		float sinRx = (float)Math.sin(Math.toRadians(rot.x)), sinRy = (float)Math.sin(Math.toRadians(rot.y)), sinRz = (float)Math.sin(Math.toRadians(rot.z));
		v.setX(cosRz*cosRy*x+(cosRz*sinRy*sinRx-sinRz*cosRx)*y+(cosRz*sinRy*cosRx+sinRz*sinRx)*z);
		v.setY(sinRz*cosRy*x+(sinRz*sinRy*sinRx+cosRz*cosRx)*y+(sinRz*sinRy*cosRx-cosRz*sinRx)*z);
		v.setZ(-sinRy*x+cosRy*sinRx*y+cosRy*cosRx*z);
		setX(v.x);
		setY(v.y);
		setZ(v.z);
		return this;
	}
	
	public boolean isZero() {
		return x == 0 && y == 0 && z == 0;
	}
	
	public Vector2f getVector2fFromXZ() {
		return new Vector2f(x, z);
	}
	
	public Vector3f clone() {
		return new Vector3f(x, y, z);
	}
	
	// ---- X

	public float getX() {
		return x;
	}

	public Vector3f setX(float x) {
		this.x = x;
		return this;
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

	public Vector3f setY(float y) {
		this.y = y;
		return this;
	}
	
	public Vector3f addY(float y) {
		this.y += y;
		return this;
	}
	
	public Vector3f subY(float y) {
		this.y -= y;
		return this;
	}
	
	public Vector3f mulY(float y) {
		this.y *= y;
		return this;
	}
	
	// ---- Z

	public float getZ() {
		return z;
	}

	public Vector3f setZ(float z) {
		this.z = z;
		return this;
	}
	
	public Vector3f addZ(float z) {
		this.z += z;
		return this;
	}
	
	public Vector3f subZ(float z) {
		this.z -= z;
		return this;
	}
	
	@Override
	public String toString() {
		return "("+x+","+y+","+z+")";
	}
	
}
