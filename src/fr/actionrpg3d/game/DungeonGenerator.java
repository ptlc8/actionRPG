	package fr.actionrpg3d.game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import fr.actionrpg3d.math.Vector2f;

public class DungeonGenerator {
	
	private static int seed = 65;
	private static double random() {
		double x = Math.sin(seed++) * 10000;
		return x - Math.floor(x);
	}
	
	/**
	 * Cette fonction permet de générer de façon procédurale un donjon (ou étage de donjon),
	 * elle génère des salles (-1) selon les arguments donnés et les relient grâce à des couloirs (-2),
	 * ces deux derniers sont séparés par des portes (-3), le reste constitue des murs (1)
	 * @author PTLC_ / Kévin Frick
	 * @param seed La graine de génération du donjon, une même graine et des même paramètres donneront le même donjon
	 * @param width La longueur du donjon
	 * @param height La largeur du donjon
	 * @param roomsNMin Le nombre de salle minimum, il peut être ignoré si la largeur et la hauteur son trop petits
	 * @param roomsNMax Le nombre de salle maximum
	 * @param roomSizeMin La taille minimale d'une salle
	 * @param roomSizeMax La taille maximale d'une salle
	 * @param treasureProp La probabilité de trouver un trésor dans une salle
	 * @return Un Dungeon avec dedans un tableau d'entiers à deux dimensions.
	 * 1 représente un mur, -1 une salle, -2 un couloir, -3 une porte, 2 une boîte, 3 un coffre
	 * @see Dungeon
	 */
	public static Dungeon generate(int seed, int width, int height, int roomsNMin, int roomsNMax, int roomSizeMin, int roomSizeMax, float treasureProp) {
		DungeonGenerator.seed = seed;
		int[][] pattern = new int[height][width];
		for (int i = 0; i < height; i++) {
			pattern[i] = new int[width];
			for (int j = 0; j < width; j++)
				pattern[i][j] = 1;
		}
		
		// generate rooms
		List<PreRoom> preRooms = new ArrayList<>();
		for (int i = 0; i < roomsNMin+(int)(random()*(roomsNMax-roomsNMin)); i++) {
			PreRoom room;
			int j = 0;
			do {
				int rWidth = roomSizeMin+(int)(random()*(roomSizeMax-roomSizeMin)/2)*2;
				int rHeight = roomSizeMin+(int)(random()*(roomSizeMax-roomSizeMin)/2)*2;
				room = new PreRoom((int)(random()*(width-rWidth-1)/2)*2+1, (int)(random()*(height-rHeight-1)/2)*2+1, rWidth, rHeight);
				if (++j > 100) break;
			} while (!canCreateRoom(preRooms, room));
			if (j > 100) break;
			if (random() < treasureProp) room.treasure = true;
			room.wavesNumber = (int) (random() * 3);
			preRooms.add(room);
			// ajout sur le pattern
			for (j = room.y; j < room.y+room.height; j++)
				for (int k = room.x; k < room.x+room.width; k++)
					pattern[j][k] = -1;
			if (room.treasure) pattern[room.y+room.height/2][room.x+room.width/2] = 3;
		}
		
		// generate corridors a path
		List<RoomPeer> roomsPeers = new ArrayList<RoomPeer>();
		for (int i = 0; i < preRooms.size(); i++)
			for (int j = i+1; j < preRooms.size(); j++)
				roomsPeers.add(new RoomPeer(preRooms.get(i), preRooms.get(j)));
		roomsPeers.sort((p1, p2) -> (p1.dx+p1.dy)-(p2.dx+p2.dy));
		for (PreRoom room : preRooms) {
			RoomPeer peer = null;
			for (RoomPeer p : roomsPeers)
				if (p.r1==room || p.r2==room) {
					peer = p;
					break;
				}
			if (peer == null) continue;
			peer.r1.corridorsNumber++;
			peer.r2.corridorsNumber++;
			Point start = new Point(peer.r1.x+(int)(peer.r1.width/4)*2, peer.r1.y+(int)(peer.r1.height/4)*2);
			Point end = new Point(peer.r2.x+(int)(peer.r2.width/4)*2, peer.r2.y+(int)(peer.r2.height/4)*2);
			for (int i = Math.min(start.x, end.x); i < Math.max(start.x, end.x)+1; i++)
				if (pattern[start.y][i] == 1) pattern[start.y][i] = pattern[start.y][i+1]==-1 || pattern[start.y][i-1]==-1 ? -3 : -2;
			for (int i = Math.min(start.y, end.y); i < Math.max(start.y, end.y); i++)
				if (pattern[i][end.x] == 1) pattern[i][end.x] = pattern[i+1][end.x]==-1 || pattern[i-1][end.x]==-1 ? -3 : -2;
			roomsPeers.remove(peer);
		}
		preRooms.sort((r1, r2) -> r2.corridorsNumber - r1.corridorsNumber);
		List<Room> rooms = new ArrayList<>();
		for (PreRoom preRoom : preRooms)
			rooms.add(preRoom.toRoom());
		return new Dungeon(width, height, pattern, rooms, rooms.get(1), rooms.get(0));
	}
	
	private static boolean canCreateRoom(List<PreRoom> rooms, PreRoom room) {
		for (PreRoom other : rooms)
			if ((room.x < other.x+other.width && other.x < room.x+room.width) && (room.y < other.y+other.height && other.y < room.y+room.height))
				return false;
		return true;
	}
	
	private static class PreRoom {
		public int corridorsNumber;
		int x, y, width, height;
		boolean treasure = false;
		int wavesNumber = 1;
		List<Vector2f> doors = new ArrayList<>();
		private PreRoom(int x, int y, int width, int height) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}
		private Room toRoom() {
			return new Room(x, y, width, height, doors, treasure, wavesNumber);
		}
	}
	
	private static class RoomPeer {
		PreRoom r1, r2;
		int dx, dy;
		public RoomPeer(PreRoom r1, PreRoom r2) {
			this.r1 = r1;
			this.r2 = r2;
			dx = Math.min(Math.abs(r1.x+r1.width-r2.x), Math.abs(r2.x+r2.width-r1.x));
			dy = Math.min(Math.abs(r1.y+r1.height-r2.y), Math.abs(r2.y+r2.height-r1.y));
		}
	}
	
}
