package fr.actionrpg3d;

import fr.actionrpg3d.inputs.InputName;

public class Input {
	
	/*public static Input unserialize() {
		
		return new Input(id, tick, player, action);
	}*/
	
	private int id;
	private int tick;
	private int player;
	private InputName inputName;
	private float value;
	
	public Input(int id, int tick, int player, InputName inputName, float value) {
		this.id = id;
		this.tick = tick;
		this.player = player;
		this.inputName = inputName;
		this.value = value;
	}
	
	public String serialize() {
		return id+" "+tick+" "+player+" "+inputName.getName();
	}
	
	public int getId() {
		return id;
	}
	
	@Override
	public int hashCode() {
		return id;
	}
	
	public int getTick() {
		return tick;
	}
	
	public void setTick(int tick) {
		this.tick = tick;
	}
	
	public int getPlayer() {
		return player;
	}
	
	public InputName getName() {
		return inputName;
	}
	
	public float getValue() {
		return value;
	}
	
}
