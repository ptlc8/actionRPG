package fr.actionrpg3d.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import fr.actionrpg3d.game.entities.AI;
import fr.actionrpg3d.game.entities.Controlable;
import fr.actionrpg3d.game.entities.Entity;
import fr.actionrpg3d.game.entities.Gravity;
import fr.actionrpg3d.game.entities.Moveable;
import fr.actionrpg3d.game.entities.Player;
import fr.actionrpg3d.game.entities.Skeleton;
import fr.actionrpg3d.game.entities.Statue;
import fr.actionrpg3d.game.entities.Wizard;
import fr.actionrpg3d.math.Vector2f;
import fr.actionrpg3d.math.Vector3f;
import fr.actionrpg3d.render.Camera;
import fr.actionrpg3d.render.FirstPersonCamera;
import fr.actionrpg3d.render.Model;
import fr.actionrpg3d.render.ThirdPersonCamera;

public class Game {
	
	private Dungeon dungeon;
	
	private List<Entity> entities = new ArrayList<Entity>();
	private List<Entity> entitiesToAdd = new ArrayList<Entity>();
	private List<Entity> entitiesToRemove = new ArrayList<Entity>();
	private List<Entity> currentWave = null;
	
	private final Camera camera;
	
	private boolean paused = false;
	
	public Game(Camera camera) {
		this.camera = camera;
		int seed = -1984888624; //new Random().nextInt();
		System.out.println("Seed : " + seed);
		// dungeon = DungeonGenerator.generate(seed, 101, 101, 8, 15, 7, 51, 0.4f);
		dungeon = DungeonGenerator.generate(seed, 101, 101, 16, 32, 7, 21, 0.4f);
		// TODO : rien pour l'instant
		Player player;
		Vector2f startPoint = dungeon.getStartPoint().mul(2); //new Vector2f(-2, -4);
		entities.add(player = new Player(this, new Vector3f(startPoint.getX(), 1, startPoint.getY()), new Model("/models/robot.model"), new Controls()));
		if (camera instanceof FirstPersonCamera) ((FirstPersonCamera)camera).setFollowed(player);
		if (camera instanceof ThirdPersonCamera) ((ThirdPersonCamera)camera).setFollowed(player);
		//entities.add(new Entity(this, new Vector3f(0, 5, 0), new Model("/models/cube.model")));
		entities.add(new Wizard(this, new Vector3f(startPoint.getX(), 1, startPoint.getY())));
		entities.add(new Statue(this, new Vector3f(startPoint.getX(), 1, startPoint.getY())));
		entities.add(new Skeleton(this, new Vector3f(startPoint.getX(), 1, startPoint.getY())));
	}
	
	private boolean echaping = false; // tmp ?
	
	public void update() {
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && !echaping){
			echaping = true;
			if (paused) resume();
			else pause();
		} else if (echaping && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			echaping = false;
		}
		if (paused) return;
		for (Entity entity : entities) {
			if (entity instanceof Gravity) ((Gravity)entity).updateGravity();
			if (entity instanceof Controlable) ((Controlable)entity).updateControlable();
			if (entity instanceof AI) ((AI)entity).updateAI(entities);
			if (entity instanceof Moveable) ((Moveable)entity).updateMove(dungeon);
			if (entity instanceof Player) {
				Room room = dungeon.getRoom(entity.getPosition().getX()/2, entity.getPosition().getZ()/2);
				if (room != null && !room.isClear() && (currentWave==null || currentWave.size()==0)) { // new wave
					if (room.getWavesNumber() == 0) {
						System.out.println("Salle termin�e");
						currentWave = null;
						room.setClear(true);
					} else {
						System.out.println("Vague "+room.getWavesNumber());
						currentWave = room.getWave();
					}
				}
			}
		}
		for (Iterator<Entity> it = entitiesToAdd.iterator(); it.hasNext(); it.remove()) {
			Entity entity = it.next();
			entities.add(entity);
		}
		for (Iterator<Entity> it = entitiesToRemove.iterator(); it.hasNext(); it.remove()) {
			Entity entity = it.next();
			entities.remove(entity);
		}
		//((Modelizable)entities.get(1)).getRotation().add(new Vector3f(2f,1f,3f)); // TODO : debug only
	}
	
	public Dungeon getDungeon() {
		return dungeon;
	}

	public List<Entity> getEntities() {
		return Collections.unmodifiableList(entities);
	}
	
	public void addEntity(Entity entity) {
		entitiesToAdd.add(entity);
	}
	
	public void removeEntity(Entity entity) {
		entitiesToRemove.add(entity);
	}

	public Camera getCamera() {
		return camera;
	}

	public boolean isPaused() {
		return paused;
	}

	public void pause() {
		paused = true;
		Mouse.setGrabbed(false);
	}
	
	public void resume() {
		paused = false;
		Mouse.setCursorPosition(Display.getWidth()/2, Display.getHeight()/2);
		Mouse.setGrabbed(true);
	}
	
}
