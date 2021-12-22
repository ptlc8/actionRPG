package fr.actionrpg3d;


import java.io.File;

import org.lwjgl.LWJGLException;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import fr.actionrpg3d.game.Game;
import fr.actionrpg3d.math.Vector3f;
import fr.actionrpg3d.render.Camera;
import fr.actionrpg3d.render.Renderer;

public class Main {
	
	private static boolean running = true;
	
	private static Camera camera;
	private static Game game;
	
	public static void main(String[] args) {
		
		System.setProperty("org.lwjgl.librarypath", new File("native/linux").getAbsolutePath());
		try {
			Display.setDisplayMode(new DisplayMode(720, 480));
			Display.setTitle("ActionRPG3D");
			Display.setResizable(true);
			Display.create();
			
			glEnable(GL_DEPTH_TEST);
			glEnable(GL_CULL_FACE); // X-ray ?
			
			
			onStart();
			
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
					update();
					Display.update();
					ticks++;
					lastUpdate += updateTime;
				}
				if (System.nanoTime() - lastRender > renderTime) {
					render();
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
	
	
	private static void onStart() {
		camera = new Camera(new Vector3f()).setPerspectiveProjection(70f, 0.1f, 1000f);
		game = new Game();
	}
	
	private static void render() {
		if (Display.wasResized()) glViewport(0, 0, Display.getWidth(), Display.getHeight());
		GL11.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		camera.getPerspectiveProjection();
		camera.render();
		Renderer.render(game);
	}
	
	private static void update() {
		camera.freeCamMove();
		game.update();
	}
	
}
