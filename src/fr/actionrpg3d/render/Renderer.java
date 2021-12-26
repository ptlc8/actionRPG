package fr.actionrpg3d.render;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.Display;

import fr.actionrpg3d.game.Dungeon;
import fr.actionrpg3d.game.Game;
import fr.actionrpg3d.game.collision.Prism;
import fr.actionrpg3d.game.entities.Creature;
import fr.actionrpg3d.game.entities.Entity;
import fr.actionrpg3d.game.entities.Modelizable;
import fr.actionrpg3d.game.entities.Player;
import fr.actionrpg3d.game.entities.Tangible;
import fr.actionrpg3d.math.Vector2f;
import fr.actionrpg3d.math.Vector3f;
import fr.actionrpg3d.render.Model.Shape;

public class Renderer {
	
	public static void renderGUI(Game game, Camera camera) {
		float aspect = (float)Display.getWidth()/(float)Display.getHeight();
		if (camera instanceof FirstPersonCamera || camera instanceof ThirdPersonCamera) {
			Creature creature = camera instanceof FirstPersonCamera ? ((FirstPersonCamera)camera).getFollowed() : ((ThirdPersonCamera)camera).getFollowed();
			float health = (float) creature.getHealth() / creature.getMaxHealth();
			renderPlayerHealth(health, aspect);
			/*if (creature instanceof Player) {
				float cooldown = (float) ((Player)creature).getCooldown() / ((Player)creature).getWeapon().getCooldown();
				renderPlayerCooldown(cooldown, aspect);
			}*/
			glColor3f(.7f, .7f, .7f); // blanc cass�
			glBegin(GL_LINES);
			glVertex2f(-.02f, 0);
			glVertex2f(.02f, 0);
			glEnd();
			glBegin(GL_LINES);
			glVertex2f(0, -.02f*aspect);
			glVertex2f(0, .02f*aspect);
			glEnd();
		}
	}
	
	private static void renderPlayerHealth(float health, float aspect) {
		glColor3f(.8f, .2f, .2f); // rouge
		glBegin(GL_QUADS);
		glVertex2f(-.95f, -.85f);
		glVertex2f(-.95f, -.95f);
		glVertex2f(-.95f+.675f*health, -.95f);
		glVertex2f(-.95f+.625f*health, -.85f);
		glEnd();
		glColor3f(.7f, .7f, .7f); // blanc cass�
		glBegin(GL_QUADS);
		glVertex2f(-1, -.8f);
		glVertex2f(-1, -1);
		glVertex2f(-.2f, -1);
		glVertex2f(-.3f, -.8f);
		glEnd();
	}
	
	/*private static void renderPlayerCooldown(float cooldown, float aspect) {
		glColor3f(.7f, .7f, .1f); // jaune
		glBegin(GL_QUADS);
		glVertex2f(.95f, -.975f);
		glVertex2f(.95f, -.875f);
		glVertex2f(.95f-.625f*cooldown, -.875f);
		glVertex2f(.95f-.675f*cooldown, -.975f);
		glEnd();
		glColor3f(.7f, .7f, .7f); // blanc cass�
		glBegin(GL_QUADS);
		glVertex2f(1, -1);
		glVertex2f(1, -.85f);
		glVertex2f(.3f, -.85f);
		glVertex2f(.2f, -1);
		glEnd();
	}*/
	
	public static void render(Game game) {
		
		for (Entity entity : game.getEntities()) {
			if (entity instanceof Modelizable) {
				if (!(game.getCamera() instanceof FirstPersonCamera && ((FirstPersonCamera)game.getCamera()).getFollowed()==entity))
					render(((Modelizable)entity).getModel(), entity.getPosition(), ((Modelizable)entity).getRotation());
				if (game.isDebug() && entity instanceof Tangible)
					render(((Tangible)entity).getHitbox(), entity.getPosition());
				if (entity instanceof Player && ((Player)entity).getWeapon()!=null) {
					Player player = (Player)entity;
					render(player.getWeapon().getModel(), player.getHandPosition().add(player.getPosition()), player.getRotation().clone().setX(0).setZ(0));
				}
			}
		}		
		
		int [][] map = game.getDungeon().getPattern();
		for(int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				renderTile(j, i, map[i][j], i>=map.length-1?0:map[i+1][j], j>=(map.length==0?0:map[0].length-1)?0:map[i][j+1], i<=0?0:map[i-1][j], j<=0?0:map[i][j-1]);
			}
		}
	}
	
	private static void render(Model model, Vector3f pos, Vector3f rot) {
		for (Shape shape : model.getShapes()) {
			glColor3f(shape.getColor().getX(), shape.getColor().getY(), shape.getColor().getZ());
			glBegin(GL_POLYGON);
			for (Vector3f vertex : shape.getVectors()) {
				Vector3f v = new Vector3f(vertex).rotate(rot);
				glVertex3f(pos.getX()+v.getX(), pos.getY()+v.getY(), pos.getZ()+v.getZ());
			}
			glEnd();
		}
	}
	
	private static void render(Prism hitbox, Vector3f pos) {
		glColor3f(1, 1, 1);
		for (Vector2f vertex : hitbox.getShape()) {
			glBegin(GL_LINES);
			glVertex3f(pos.getX()+vertex.getX(), pos.getY(), pos.getZ()+vertex.getY());
			glVertex3f(pos.getX()+vertex.getX(), pos.getY()+hitbox.getHeight(), pos.getZ()+vertex.getY());
			glEnd();
		}
		for (int i = 0; i < 2; i++) {
			glBegin(GL_LINES);
			for (Vector2f vertex : hitbox.getShape()) {
				glVertex3f(pos.getX()+vertex.getX(), pos.getY()+(i==0?0:hitbox.getHeight()), pos.getZ()+vertex.getY());
			}
			glEnd();
		}
	}
	
	private static void renderTile(int x, int z, int tile, int north, int east, int south, int west) {
		if (tile != Dungeon.WALL) {
			renderFloor(x, z);
			if (north==Dungeon.WALL) renderWall(x, z, Direction.NORTH);
			if (east==Dungeon.WALL) renderWall(x, z, Direction.EAST);
			if (south==Dungeon.WALL) renderWall(x, z, Direction.SOUTH);
			if (west==Dungeon.WALL) renderWall(x, z, Direction.WEST);
		}
		switch (tile) {
		case Dungeon.CHEST:
			render(treasure, new Vector3f(2*x, 0, 2*z), new Vector3f());
			break;
		case Dungeon.OPENABLE_DOOR:
		case Dungeon.OPEN_DOOR:
		case Dungeon.CLOSE_DOOR:
			Vector3f position = new Vector3f(x*2, 0, z*2), rotation = new Vector3f();
			if (tile == Dungeon.OPEN_DOOR) rotation.addY(90);
			if (north == Dungeon.ROOM) render(door, position, rotation.clone().addY(-90));
			if (east == Dungeon.ROOM) render(door, position, rotation.clone());
			if (south == Dungeon.ROOM) render(door, position, rotation.clone().addY(90));
			if (west == Dungeon.ROOM) render(door, position, rotation.clone().addY(180));
			break;
		}
	}
	
	private static void renderFloor(int x, int z) {
		glBegin(GL_QUADS);
		if ((x+z)%2==0) glColor3f(.8f, .8f, .8f);
		else glColor3f(.7f, .7f, .7f);
		glVertex3f(2*x-1f, 0f, 2*z+1f);
		glVertex3f(2*x+1f, 0f, 2*z+1f);
		glVertex3f(2*x+1f, 0f, 2*z-1f);
		glVertex3f(2*x-1f, 0f, 2*z-1f);
		glEnd();
	}
	
	private enum Direction {NORTH, SOUTH, EAST, WEST}
	
	private static void renderWall(int x, int z, Direction d) {
		glBegin(GL_QUADS);
		if (d==Direction.NORTH||d==Direction.SOUTH) glColor3f(.7f, .7f, .7f);
		else glColor3f(.6f, .6f, .6f);
		glVertex3f(2*x+(d==Direction.SOUTH||d==Direction.EAST?1f:-1f), 0f, 2*z+(d==Direction.NORTH||d==Direction.EAST?1f:-1f));
		glVertex3f(2*x+(d==Direction.SOUTH||d==Direction.EAST?1f:-1f), 2f, 2*z+(d==Direction.NORTH||d==Direction.EAST?1f:-1f));
		glVertex3f(2*x+(d==Direction.NORTH||d==Direction.EAST?1f:-1f), 2f, 2*z+(d==Direction.NORTH||d==Direction.WEST?1f:-1f));
		glVertex3f(2*x+(d==Direction.NORTH||d==Direction.EAST?1f:-1f), 0f, 2*z+(d==Direction.NORTH||d==Direction.WEST?1f:-1f));
		glEnd();
	}
	
	private static Model door = new Model("/models/door.model");
	
	private static Model treasure = new Model("/models/chest.model"); // TODO : peu mieux faire
	
}