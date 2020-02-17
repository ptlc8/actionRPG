package fr.actionrpg3d.game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class DungeonGenerator {
	
	private static int seed = 65;
	private static double random() {
		double x = Math.sin(seed++) * 10000;
		return x - Math.floor(x);
	}
	
	public static int[][] generate(int width, int height, int roomsNMin, int roomsNMax, int roomSizeMin, int roomSizeMax, int seed_) {
		/*if (seed_ != null)*/ seed = seed_;
		int[][] pattern = new int[height][width];
		for (int i = 0; i < height; i++) {
			pattern[i] = new int[width];
			for (int j = 0; j < width; j++)
				pattern[i][j] = 1;
		}
		
		// generate rooms
		List<Room> rooms = new ArrayList<Room>();
		for (int i = 0; i < roomsNMin+(int)(random()*(roomsNMax-roomsNMin)); i++) {
			Room room;
			int j = 0;
			do {
				int rWidth = roomSizeMin+(int)(random()*(roomSizeMax-roomSizeMin)/2)*2;
				int rHeight = roomSizeMin+(int)(random()*(roomSizeMax-roomSizeMin)/2)*2;
				room = new Room((int)(random()*(width-rWidth-1)/2)*2+1, (int)(random()*(height-rHeight-1)/2)*2+1, rWidth, rHeight);
				if (++j > 100) break;
			} while (!canCreateRoom(rooms, room));
			if (j > 100) break;
			rooms.add(room);
			for (j = room.y; j < room.y+room.height; j++)
				for (int k = room.x; k < room.x+room.width; k++)
					pattern[j][k] = -1;
		}
		
		// generate corridors a path
		List<RoomPeer> roomsPeers = new ArrayList<RoomPeer>();
		for (int i = 0; i < rooms.size(); i++)
			for (int j = i+1; j < rooms.size(); j++)
				roomsPeers.add(new RoomPeer(rooms.get(i), rooms.get(j)));
		//console.log(r = roomsPeers);
		roomsPeers.sort((p1, p2) -> (p1.dx+p1.dy)-(p2.dx+p2.dy));
		//console.log(roomsPeers);
		for (Room room : rooms) {
			RoomPeer peer = null;
			for (RoomPeer p : roomsPeers)
				if (p.r1==room || p.r2==room) {
					peer = p;
					break;
				}
			if (peer == null) continue;
			Point start = new Point(peer.r1.x+(int)(peer.r1.width/4)*2, peer.r1.y+(int)(peer.r1.height/4)*2);
			Point end = new Point(peer.r2.x+(int)(peer.r2.width/4)*2, peer.r2.y+(int)(peer.r2.height/4)*2);
			for (int i = Math.min(start.x, end.x); i < Math.max(start.x, end.x)+1; i++)
				if (pattern[start.y][i] == 1) pattern[start.y][i] = pattern[start.y][i+1]==-1 || pattern[start.y][i-1]==-1 ? -3 : -2;
			for (int i = Math.min(start.y, end.y); i < Math.max(start.y, end.y); i++)
				if (pattern[i][end.x] == 1) pattern[i][end.x] = pattern[i+1][end.x]==-1 || pattern[i-1][end.x]==-1 ? -3 : -2;
			roomsPeers.remove(peer);
		}
		return pattern;
	}
	
	private static boolean canCreateRoom(List<Room> rooms, Room room) {
		for (Room other : rooms)
			if ((room.x < other.x+other.width && other.x < room.x+room.width) && (room.y < other.y+other.height && other.y < room.y+room.height))
				return false;
		return true;
	}
	
	private static class Room {
		int x, y, width, height;
		public Room(int x, int y, int width, int height) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}
	}
	
	private static class RoomPeer {
		Room r1, r2;
		int dx, dy;
		public RoomPeer(Room r1, Room r2) {
			this.r1 = r1;
			this.r2 = r2;
			dx = Math.min(Math.abs(r1.x+r1.width-r2.x), Math.abs(r2.x+r2.width-r1.x));
			dy = Math.min(Math.abs(r1.y+r1.height-r2.y), Math.abs(r2.y+r2.height-r1.y));
		}
	}
	
}
