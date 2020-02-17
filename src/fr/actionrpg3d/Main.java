package fr.actionrpg3d;

import org.lwjgl.LWJGLException;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import fr.actionrpg3d.game.DungeonGenerator;
import fr.actionrpg3d.math.Vector3f;
import fr.actionrpg3d.render.Camera;

public class Main {
	
	private static boolean running = true;
	
	public static void main(String[] args) {
		try {
			Display.setDisplayMode(new DisplayMode(720, 480));
			Camera camera = new Camera(new Vector3f()).setPerspectiveProjection(70f, 0.1f, 1000f);
			Display.setTitle("ActionRPG3D");
			Display.setResizable(true);
			Display.create();
			
			glEnable(GL_DEPTH_TEST);
			glEnable(GL_CULL_FACE); // X-ray ?
			
			
			int FRAME_CAP = 60;
			long lastUpdate = System.nanoTime();
			long lastRender = System.nanoTime();
			double updateTime = 1_000_000_000.0 / 60.0; // TPS
			double renderTime = 1_000_000_000.0 / FRAME_CAP;
			int ticks = 0, frames = 0;
			long timer = System.currentTimeMillis();
			
			onStart();
			
			while (running) {
				boolean rendered = false;
				if (Display.isCloseRequested()) running = false;
				if (System.nanoTime() - lastUpdate > updateTime) {
					update(camera);
					Display.update();
					ticks++;
					lastUpdate += updateTime;
				}
				if (System.nanoTime() - lastRender > renderTime) {
					render(camera);
					frames++;
					lastRender += renderTime;
					rendered = true;
				}
				if (System.currentTimeMillis() - timer > 1000) {
					System.out.println("TPS : " + ticks + " ; FPS : " + frames);
					ticks = frames = 0;
					timer = System.currentTimeMillis();
				}
				if (rendered) {
					try {
						Thread.sleep((int)(1000.0/FRAME_CAP));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
			
			Display.destroy();
			System.exit(0);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	private static int[][] map;
	
	private static void onStart() {
		map = DungeonGenerator.generate(101, 101, 8, 15, 7, 51, 0);
	}
	
	private static void render(Camera camera) {
		if (Display.wasResized()) glViewport(0, 0, Display.getWidth(), Display.getHeight());
		GL11.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		camera.getPerspectiveProjection();
		camera.render();
		
		for(int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				if (map[i][j] > 0) continue;
				glBegin(GL_QUADS);
				glColor3f(1, 1, 1);
				glVertex3f(j, 0f, i+1);
				glVertex3f(j+1, 0f, i+1);
				glVertex3f(j+1, 0f, i);
				glVertex3f(j, 0f, i);
				glEnd();
			}
		}
		
		glBegin(GL_QUADS);
		glColor3f(1, .5f, 0);
		glVertex3f(-1, -.5f, -0);
		glColor3f(1, 0f, 0);
		glVertex3f(1, -.5f, -0);
		glColor3f(1, .5f, 1);
		glVertex3f(1, -.5f, -10);
		glColor3f(0, .5f, 0);
		glVertex3f(-1, -.5f, -10);
		glEnd();
	}
	
	private static void update(Camera camera) {
		camera.freeCamMove();
		// plein de choses !!!
	}
	
}
