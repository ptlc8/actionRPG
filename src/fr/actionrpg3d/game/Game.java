package fr.actionrpg3d.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import fr.actionrpg3d.render.Model;

public class Game {
	
	private int tick = 0;
	private Dungeon dungeon;
	private final int seed;
	
	private Map<Integer, Player> players = new HashMap<>();
	private List<Entity> entities = new ArrayList<Entity>();
	private List<Entity> entitiesToAdd = new ArrayList<Entity>();
	private List<Entity> entitiesToRemove = new ArrayList<Entity>();
	private Wave currentWave = null;
	
	private boolean paused = false;
	private boolean debug = false;
	
	
	public Game(int seed) {
		this.seed = seed; 
		//new Random().nextInt();
		System.out.println("Seed : " + seed);
		// dungeon = DungeonGenerator.generate(seed, 101, 101, 8, 15, 7, 51, 0.4f);
		dungeon = DungeonGenerator.generate(seed, 101, 101, 16, 32, 7, 21, 0.4f);
		// TODO : rien pour l'instant
		Vector2f startPoint = dungeon.getStartPoint().mul(2); //new Vector2f(-2, -4);
		//entities.add(new Entity(this, new Vector3f(0, 5, 0), new Model("/models/cube.model")));
		entities.add(new Wizard(this, new Vector3f(startPoint.getX(), 1, startPoint.getY())));
		entities.add(new Statue(this, new Vector3f(startPoint.getX(), 1, startPoint.getY())));
		entities.add(new Skeleton(this, new Vector3f(startPoint.getX(), 1, startPoint.getY())));
	}
	
	public void update(Map<Integer, Controls> controls) {
		tick++;
		if (paused) return;
		for (Entity entity : entities) {
			if (entity instanceof Gravity)
				((Gravity)entity).updateGravity();
			if (entity instanceof Controlable) {
				Controls contr0ls = controls.get(((Controlable)entity).getControlsId());
				if (contr0ls != null)
					((Controlable)entity).updateControlable(contr0ls);
			}
			if (entity instanceof AI)
				((AI)entity).updateAI(entities);
			if (entity instanceof Moveable)
				((Moveable)entity).updateMove(dungeon);
		}
		for (Iterator<Entity> it = entitiesToAdd.iterator(); it.hasNext(); it.remove()) {
			Entity entity = it.next();
			entities.add(entity);
		}
		for (Iterator<Entity> it = entitiesToRemove.iterator(); it.hasNext(); it.remove()) {
			Entity entity = it.next();
			entities.remove(entity);
		}
		updateRoom();
	}
	
	private void updateRoom() {
		Room room = getCurrentRoom();
		if (room != null && !room.isClear() && (currentWave==null || currentWave.size()==0)) {
			if (currentWave!=null) room.clearWave();
			if (room.isClear()) {
				System.out.println("Salle terminée");
				currentWave = null;
			} else {
				System.out.println("Vague "+(room.getClearedWaves()+1)+"/"+room.getWavesNumber());
				currentWave = room.createWave(this);
			}
		}
	}
	
	public boolean addPlayer(int playerId) {
		if (players.containsKey(playerId))
			return false;
		Vector2f startPoint = dungeon.getStartPoint().mul(2);
		Player player = new Player(this, playerId, new Vector3f(startPoint.getX(), 1, startPoint.getY()), new Model("/models/robot.model"));
		entities.add(player);
		players.put(playerId, player);
		return true;
	}
	
	public boolean removePlayer(int playerId) {
		if (!players.containsKey(playerId))
			return false;
		entities.remove(players.get(playerId));
		players.remove(playerId);
		return true;
	}
	
	public int getTick() {
		return tick;
	}
	
	public Map<Integer, Player> getPlayers() {
		return Collections.unmodifiableMap(players);
	}
	
	public int getSeed() {
		return seed;
	}
	
	public Dungeon getDungeon() {
		return dungeon;
	}
	
	public Room getCurrentRoom() {
		for (Entity entity : entities)
			if (entity instanceof Player)
				return dungeon.getRoom(entity.getPosition().getX()/2, entity.getPosition().getZ()/2);
		return null;
	}
	
	public Wave getCurrentWave() {
		return currentWave;
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

	public boolean isPaused() {
		return paused;
	}

	public void pause() {
		paused = true;
	}
	
	public void resume() {
		paused = false;
	}
	
	public boolean isDebug() {
		return debug;
	}
	
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
}
