package fr.actionrpg3d.game;

import java.util.List;
import fr.actionrpg3d.math.Vector2f;

public class Dungeon {
	
	public static final int WALL=1, ROOM=-1, CORRIDOR=-2, OPENABLE_DOOR=-3, BOX=2, CHEST=3, OUT=0, CLOSE_DOOR=-4, OPEN_DOOR=-5, CLEAR_ROOM=-7;
	
	private int width, height;
	private int[][] pattern;
	
	private List<Room> rooms;
	private Room startRoom, endRoom;
	
	public Dungeon(int width, int height, int[][] pattern, List<Room> rooms, Room startRoom, Room endRoom) {
		this.width = width;
		this.height = height;
		this.pattern = pattern;
		this.rooms = rooms;
		this.startRoom = startRoom;
		this.endRoom = endRoom;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int[][] getPattern() {
		return pattern;
	}
	
	public int get(int x, int y) {
		if (x<0 || y<0 || x>=width || y>=height) return 0;
		return pattern[y][x];
	}
	
	public boolean isWall(int x, int y) {
		return get(x, y) == WALL;
	}
	
	Room getRoom(float x, float z) {
		for (Room room : rooms)
			if (room.getX()<x && room.getZ()<z && x<room.getX()+room.getWidth() && z<room.getZ()+room.getHeight())
				return room;
		return null;
	}
	
	public Vector2f getStartPoint() {
		return new Vector2f(startRoom.getX()+startRoom.getWidth()/2, startRoom.getZ()+startRoom.getHeight()/2);
	}

	public Room getStartRoom() {
		return startRoom;
	}

	public Room getEndRoom() {
		return endRoom;
	}
	
}
