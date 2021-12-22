package fr.actionrpg3d.render;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.GLU;

import fr.actionrpg3d.math.Vector3f;

public abstract class Camera {
	
	private Vector3f pos;
	private Vector3f rotation;
	private float fov;
	private float near;
	private float far;
	
	public Camera(Vector3f pos) {
		this.pos = pos;
		rotation = new Vector3f();
	}
	
	public Camera setPerspectiveProjection(float fov, float near, float far) {
		this.fov = fov;
		this.near = near;
		this.far = far;
		return this;
	}
	
	public void getPerspectiveProjection() {
		glEnable(GL_PROJECTION);
		GLU.gluPerspective(fov, (float)Display.getWidth()/(float)Display.getHeight(), near, far);
		glEnable(GL_MODELVIEW);
	}
	
	public void render() {
		glPushAttrib(GL_TRANSFORM_BIT);		// inutile ?
		glRotatef(rotation.getX(), 1, 0, 0);
		glRotatef(rotation.getY(), 0, 1, 0);
		glRotatef(rotation.getZ(), 0, 0, 1);
		glTranslatef(pos.getX(), pos.getY(), pos.getZ());
		glPopMatrix();						// inutile ?
	}
	
	public abstract void update();

	public Vector3f getPosition() {
		return pos;
	}

	public void setPosition(Vector3f pos) {
		this.pos = pos;
	}
	
	public Vector3f getRotation() {
		return rotation;
	}
	
}
	