package fr.actionrpg3d.render;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.GLU;

import fr.actionrpg3d.math.Vector3f;

public class Camera {
	
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
		glLoadIdentity();
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
	
	private float speed = .1f;
	
	public void freeCamMove() {
		if (Mouse.isButtonDown(0)) {
			rotation.addX(-Mouse.getDY());
			rotation.addY(Mouse.getDX());
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
			pos.addZ((float)(speed*Math.cos(Math.toRadians(rotation.getY()))));
			pos.addX((float)(-speed*Math.sin(Math.toRadians(rotation.getY()))));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			pos.addZ((float)(-speed*Math.cos(Math.toRadians(rotation.getY()))));
			pos.addX((float)(speed*Math.sin(Math.toRadians(rotation.getY()))));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			pos.addX((float)(speed*Math.cos(Math.toRadians(rotation.getY()))));
			pos.addZ((float)(speed*Math.sin(Math.toRadians(rotation.getY()))));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			pos.addX((float)(-speed*Math.cos(Math.toRadians(rotation.getY()))));
			pos.addZ((float)(-speed*Math.sin(Math.toRadians(rotation.getY()))));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			pos.addY(-speed);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			pos.addY(speed);
		}
	}

	public Vector3f getPos() {
		return pos;
	}

	public void setPos(Vector3f pos) {
		this.pos = pos;
	}
	
}
