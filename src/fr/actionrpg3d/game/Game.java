package fr.actionrpg3d.game;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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

public class Game implements Cloneable, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int seed;
	private int tick = 0;
	private transient Dungeon dungeon;
	private int entityKId = 0;
	
	private HashMap<Integer, Player> players = new HashMap<>();
	private HashMap<Integer, Entity> entities = new HashMap<>();
	private ArrayList<Entity> entitiesToAdd = new ArrayList<>();
	private ArrayList<Entity> entitiesToRemove = new ArrayList<>();
	private Wave currentWave = null;
	
	private boolean debug = false;
	
	public Game(int seed) {
		this.seed = seed;
		init();
		Vector2f startPoint = dungeon.getStartPoint().mul(2); //new Vector2f(-2, -4);
		//addEntity(new Entity(this, new Vector3f(0, 5, 0), new Model("/models/cube.model")));
		addEntity(new Wizard(nextId(), new Vector3f(startPoint.getX(), 1, startPoint.getY())));
		addEntity(new Statue(nextId(), new Vector3f(startPoint.getX(), 1, startPoint.getY())));
		addEntity(new Skeleton(nextId(), new Vector3f(startPoint.getX(), 1, startPoint.getY())));
	}
	
	private void init() {
		System.out.println("Seed : " + seed);
		// dungeon = DungeonGenerator.generate(seed, 101, 101, 8, 15, 7, 51, 0.4f);
		dungeon = new DungeonGenerator(seed).generate(101, 101, 16, 32, 7, 21, 0.4f);
		// TODO : rien pour l'instant
		System.out.println("Start " + dungeon.getStartPoint());
	}
	
	@Override
	public Game clone() {
		synchronized (this) {
			Game clone = new Game(seed);
			clone.entities.clear(); // tmp : don't add entities in constructor
			clone.tick = tick;
			for (Entity entity : entities.values()) {
				Entity clonedEntity = entity.clone();
				if (!clonedEntity.getClass().equals(entity.getClass()))
					throw new RuntimeException(entity.getClass().getCanonicalName() + " musts implement its own clone method");
				clone.entities.put(clonedEntity.getId(), clonedEntity);
				if (clonedEntity instanceof Player)
					players.put(((Player) clonedEntity).getPlayerId(), ((Player) clonedEntity));
			}
			for (Entity entity : entitiesToAdd)
				clone.entitiesToAdd.add(entity.clone());
			for (Entity entity : entitiesToRemove)
				clone.entitiesToRemove.add(entity.clone());
			if (currentWave != null)
				clone.currentWave = currentWave.clone(clone);
			clone.debug = debug;
			return this;
		}
	}
	
	public String toBase64() {
		try {
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bytes);
			oos.writeObject(this);
			oos.close();
			return Base64.getEncoder().encodeToString(bytes.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Game fromBase64(String base64) {
		try {
			return (Game) new ObjectInputStream(new ByteArrayInputStream(Base64.getDecoder().decode(base64))).readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		seed = stream.readInt();
		tick = stream.readInt();
		entityKId = stream.readInt();
		players = new HashMap<>();
		entities = new HashMap<>();
		for (Entity entity : (HashSet<Entity>) stream.readObject()) {
			if (entity instanceof Player)
				players.put(((Player) entity).getPlayerId(), (Player) entity);
			entities.put(entity.getId(), entity);
		}
		entitiesToAdd = (ArrayList<Entity>) stream.readObject();
		entitiesToRemove = (ArrayList<Entity>) stream.readObject();
		currentWave = (Wave) stream.readObject();
		init();
	}
	
	private void writeObject(ObjectOutputStream stream) throws IOException {
		stream.writeInt(seed);
		stream.writeInt(tick);
		stream.writeInt(entityKId);
		stream.writeObject(new HashSet<>(entities.values()));
		stream.writeObject(entitiesToAdd);
		stream.writeObject(entitiesToRemove);
		stream.writeObject(currentWave);
	}
	
	public int nextId() {
		return ++entityKId;
	}
	
	public void update(Map<Integer, Controls> controls) {
		//for (Entity entity : players.values())
		synchronized (this) {
			tick++;
			for (Entity entity : entities.values()) {
				if (entity instanceof Gravity)
					((Gravity)entity).updateGravity();
				if (entity instanceof Controlable) {
					Controls contr0ls = controls.get(((Controlable)entity).getControlsId());
					if (contr0ls != null)
						((Controlable)entity).updateControlable(this, contr0ls);
				}
				if (entity instanceof AI)
					((AI)entity).updateAI(this);
				if (entity instanceof Moveable)
					((Moveable)entity).updateMove(dungeon);
			}
			for (Iterator<Entity> it = entitiesToAdd.iterator(); it.hasNext(); it.remove()) {
				Entity entity = it.next();
				entities.put(entity.getId(), entity);
			}
			for (Iterator<Entity> it = entitiesToRemove.iterator(); it.hasNext(); it.remove()) {
				Entity entity = it.next();
				entities.remove(entity.getId());
			}
			updateRoom();
		}
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
		Player player = new Player(nextId(), playerId, new Vector3f(startPoint.getX(), 1, startPoint.getY()), "robot");
		addEntity(player);
		players.put(playerId, player);
		return true;
	}
	
	public boolean removePlayer(int playerId) {
		if (!players.containsKey(playerId))
			return false;
		removeEntity(players.get(playerId));
		players.remove(playerId);
		return true;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof Game)) return false;
		Game other = (Game) obj;
		return other.tick == tick && other.seed == seed && other.entities.equals(entities) && other.entitiesToAdd.equals(entitiesToAdd) && other.entitiesToRemove.equals(entitiesToRemove);
		// TODO: test wave
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
		for (Entity entity : entities.values())
			if (entity instanceof Player)
				return dungeon.getRoom(entity.getPosition().getX()/2, entity.getPosition().getZ()/2);
		return null;
	}
	
	public Wave getCurrentWave() {
		return currentWave;
	}
	
	public Map<Integer, Entity> getEntities() {
		return Collections.unmodifiableMap(entities);
	}
	
	public Entity getEntity(int id) {
		return entities.get(id);
	}
	
	public void addEntity(Entity entity) {
		entitiesToAdd.add(entity);
	}
	
	public void removeEntity(Entity entity) {
		entitiesToRemove.add(entity);
	}
	
	public boolean isDebug() {
		return debug;
	}
	
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
}
