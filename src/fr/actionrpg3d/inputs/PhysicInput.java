package fr.actionrpg3d.inputs;

import org.lwjgl.input.Controllers;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public abstract class PhysicInput {
	
	private static float MouseDX = 0, MouseDY = 0, MouseDWheel = 0;
	
	private static int controllerIndex = 0;
	
	public static void refreshControllerIndex() {
		Controllers.poll();
		for (int i = 0; i < Controllers.getControllerCount(); i++) {
			if (Controllers.getController(i).getAxisCount()>=4)
				controllerIndex = i;
		}
	}
	
	public static void refresh() {
		MouseDX = Mouse.getDX()/6f;
		MouseDY = Mouse.getDY()/6f;
		MouseDWheel = Mouse.getDWheel()/10f;
	}
	
	abstract float getValue();
	
	abstract String getName();
	
	abstract String serialize();
	
	static PhysicInput unserialize(String str) {
		String prefix = str.substring(0,2);
		boolean positive = str.endsWith("+");
		int value = Integer.parseInt(str.substring(2,str.length()-(str.endsWith("+")||str.endsWith("-")?1:0)));
		switch (prefix) {
		case "kb":
			return new KeyBoardInput(value);
		case "mb":
			return new MouseButtonInput(value);
		case "ma":
			return new MouseAxisInput(value, positive);
		case "ca":
			return new ControllerAxisInput(value, positive);
		case "cb":
			return new ControllerButtonInput(value);
		default:
			return null;
		}
	}
	
	static class KeyBoardInput extends PhysicInput {
		private int key;
		public KeyBoardInput(int key) {
			this.key = key;
		}
		@Override
		public float getValue() {
			if (!Keyboard.isCreated()) return 0;
			return Keyboard.isKeyDown(key) ? 1 : 0;
		}
		@Override
		public String getName() {
			return Keyboard.getKeyName(key);
		}
		@Override
		public String serialize() {
			return "kb"+key;
		}
	}
	
	static class MouseButtonInput extends PhysicInput {
		private int button;
		public MouseButtonInput(int button) {
			this.button = button;
		}
		@Override
		public float getValue() {
			if (!Mouse.isCreated()) return 0;
			return Mouse.isButtonDown(button) ? 1 : 0;
		}
		@Override
		public String getName() {
			return Mouse.getButtonName(button);
		}
		@Override
		public String serialize() {
			return "mb"+button;
		}
	}
	
	static class MouseAxisInput extends PhysicInput {
		private int axis;
		private boolean positive;
		public MouseAxisInput(int axis, boolean positive) {
			this.axis = axis;
			this.positive = positive;
		}
		@Override
		public float getValue() {
			float axisValue = axis==0 ? MouseDX : axis==1 ? MouseDY : axis==2 ? MouseDWheel : 0;
			return positive ? Math.max(0, axisValue) : Math.max(0, -axisValue);
		}
		@Override
		public String getName() {
			return (axis==2 ? "Mouse wheel" : axis==0 ? "Mouse X" : axis==1 ? "Mouse Y" : ("Mouse axis"+axis))+(positive?"+":"-");
		}
		@Override
		public String serialize() {
			return "ma"+axis+(positive?"+":"-");
		}
	}
	
	static class ControllerAxisInput extends PhysicInput {
		private int index;
		private boolean positive;
		public ControllerAxisInput(int index, boolean positive) {
			this.index = index;
			this.positive = positive;
		}
		@Override
		public float getValue() {
			if (Controllers.getControllerCount()<=controllerIndex || Controllers.getController(controllerIndex).getAxisCount()<=index)
				return 0;
			return positive ? Math.max(0, Controllers.getController(controllerIndex).getAxisValue(index)) : Math.max(0, -Controllers.getController(controllerIndex).getAxisValue(index));
		}
		@Override
		public String getName() {
			return (Controllers.getControllerCount()>controllerIndex ? Controllers.getController(controllerIndex).getAxisName(index) : "Gamepad axis "+index)+(positive?"+":"-");
		}
		@Override
		public String serialize() {
			return "ca"+index+(positive?"+":"-");
		}
	}
	
	static class ControllerButtonInput extends PhysicInput {
		private int index;
		public ControllerButtonInput(int index) {
			this.index = index;
		}
		@Override
		public float getValue() {
			if (Controllers.getControllerCount()<=controllerIndex || Controllers.getController(controllerIndex).getButtonCount()<=index)
				return 0;
			return Controllers.getController(controllerIndex).isButtonPressed(index) ? 1 : 0;
		}
		@Override
		public String getName() {
			return Controllers.getControllerCount()>controllerIndex ? Controllers.getController(controllerIndex).getButtonName(index) : "Gamepad button "+index;
		}
		@Override
		public String serialize() {
			return "cb"+index;
		}
	}
	
}
