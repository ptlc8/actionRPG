package fr.actionrpg3d.render;

import static org.lwjgl.opengl.GL11.*;
import fr.actionrpg3d.game.Entity;
import fr.actionrpg3d.game.Game;

public class Renderer {
	
	public static void render(Game game) {
		
		for (Entity entity : game.getEntities()) {
			glBegin(GL_TRIANGLES);	// TODO : les entités sont actuellement signalées par des triangles rouges
			glColor3f(1, 0, 0);
			glVertex3f(entity.getPosition().getX()+1, .1f, entity.getPosition().getZ());
			glVertex3f(entity.getPosition().getX()-.7f, .1f, entity.getPosition().getZ()-.9f);
			glVertex3f(entity.getPosition().getX()-.7f, .1f, entity.getPosition().getZ()+.9f);
			glEnd();
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
		
	}
	
}
