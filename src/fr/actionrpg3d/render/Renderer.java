package fr.actionrpg3d.render;

import static org.lwjgl.opengl.GL11.*;
import fr.actionrpg3d.game.Entity;
import fr.actionrpg3d.game.Game;
import fr.actionrpg3d.game.Modelizable;
import fr.actionrpg3d.math.Vector3f;
import fr.actionrpg3d.render.Model.Shape;

public class Renderer {
	
	public static void render(Game game) {
		
		for (Entity entity : game.getEntities()) {
			if (entity instanceof Modelizable)
				render(((Modelizable)entity).getModel(), entity.getPosition().getX(), entity.getPosition().getY(), entity.getPosition().getZ());
		}
		
		// TODO : temporaire 
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
		
		
		int [][] map = game.getMap();
		for(int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				if (map[i][j] < 0) {
					renderFloor(j, i);
					if (i<=0 || map[i-1][j] > 0) renderWall(j, i, Direction.SOUTH);
					if (i>=map.length-1 || map[i+1][j] > 0) renderWall(j, i, Direction.NORTH);
					if (j<=0 || map[i][j-1] > 0) renderWall(j, i, Direction.WEST);
					if (j>=(map.length==0?0:map[0].length-1) || map[i][j+1] > 0) renderWall(j, i, Direction.EAST);
				}
			}
		}
	}
	
	private static void render(Model model, float x, float y, float z) {
		for (Shape shape : model.getShapes()) {
			glBegin(GL_POLYGON);
			for (Vector3f vertex : shape.getVectors()) {
				glVertex3f(x+vertex.getX(), y+vertex.getY(), z+vertex.getZ());
			}
			glEnd();
		}
	}
	
	private static void renderFloor(int x, int z) {
		glBegin(GL_QUADS);
		glColor3f(1, 1, 1);
		glVertex3f(x-.5f, 0f, z+.5f);
		glVertex3f(x+.5f, 0f, z+.5f);
		glVertex3f(x+.5f, 0f, z-.5f);
		glVertex3f(x-.5f, 0f, z-.5f);
		glEnd();
	}
	
	
	private enum Direction {NORTH, SOUTH, EAST, WEST}
	private static void renderWall(int x, int z, Direction d) {
		glBegin(GL_QUADS);
		if (d==Direction.NORTH||d==Direction.SOUTH) glColor3f(.9f, .9f, .9f);
		else glColor3f(.8f, .8f, .8f);
		glVertex3f(x+(d==Direction.SOUTH||d==Direction.EAST?.5f:-.5f), 0f, z+(d==Direction.NORTH||d==Direction.EAST?.5f:-.5f));
		glVertex3f(x+(d==Direction.SOUTH||d==Direction.EAST?.5f:-.5f), 1f, z+(d==Direction.NORTH||d==Direction.EAST?.5f:-.5f));
		glVertex3f(x+(d==Direction.NORTH||d==Direction.EAST?.5f:-.5f), 1f, z+(d==Direction.NORTH||d==Direction.WEST?.5f:-.5f));
		glVertex3f(x+(d==Direction.NORTH||d==Direction.EAST?.5f:-.5f), 0f, z+(d==Direction.NORTH||d==Direction.WEST?.5f:-.5f));
		glEnd();
	}
	
}