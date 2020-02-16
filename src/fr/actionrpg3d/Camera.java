package fr.actionrpg3d;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private Vector3f pos;
	private float fov;
	private float near;
	private float far;
	
	public Camera(Vector3f pos) {
		this.pos = pos;
	}
	
	public Camera setPerspectiveProjection(float fov, float near, float far) {
		this.fov = fov;
		this.near = near;
		this.far = far;
		return this;
	}
	
	public void getPerspectiveProjection() {
		glEnable(GL_PROJECTION);
		glLoadIdentity();
		GLU.gluPerspective(fov, (float)Display.getWidth()/(float)Display.getHeight(), near, far);
		glEnable(GL_MODELVIEW);
	}

	public Vector3f getPos() {
		return pos;
	}

	public void setPos(Vector3f pos) {
		this.pos = pos;
	}
	
}
