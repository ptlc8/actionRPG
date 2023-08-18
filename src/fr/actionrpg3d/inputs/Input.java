package fr.actionrpg3d.inputs;

public class Input implements Comparable<Input> {
	
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
	
	public int getId() {
		return id;
	}
	
	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public int compareTo(Input o) {
		return id - o.id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof Input)) return false;
		Input other = (Input) obj;
		return other.id == id && other.tick == tick && other.player == player && other.inputName == inputName && other.value == value;
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
