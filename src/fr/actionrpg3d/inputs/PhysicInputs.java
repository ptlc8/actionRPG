package fr.actionrpg3d.inputs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.lwjgl.input.Controllers;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class PhysicInputs {
	
	private static int controllerIndex = 0;
	
	public static void refreshControllerIndex() {
		Controllers.poll();
		for (int i = 0; i < Controllers.getControllerCount(); i++) {
			if (Controllers.getController(i).getAxisCount()>=4)
				controllerIndex = i;
		}
	}
	
	public static PhysicInput getLastInput() {
		PhysicInput input = null;
		long inputTime = 0;
		// Keyboard events
		int key = -1;
		long keyT = 0;
		while (Keyboard.next()) {
			key = Keyboard.getEventKey();
			keyT = Keyboard.getEventNanoseconds();
		}
		if (key != -1) {
			input = new KeyBoardInput(key);
			inputTime = keyT;
		}
		// Mouse events
		int mouseButton = -1;
		byte mouseAxis = -1;
		boolean mouseAxisPositive = true;
		long mouseT = 0;
		while (Mouse.next()) {
			mouseButton = Mouse.getEventButton();
			mouseAxis = (byte)(Mouse.getEventDX()!=0 ? 0 : Mouse.getEventDY()!=0 ? 1 : Mouse.getEventDWheel()!=0 ? 2 : -1);
			mouseAxisPositive = Mouse.getEventDX()!=0 ? Mouse.getEventDX()>0 : Mouse.getEventDY()!=0 ? Mouse.getEventDY()>0 : Mouse.getEventDWheel()!=0 ? Mouse.getEventDWheel()>0 : mouseAxisPositive;
			mouseT = Mouse.getEventNanoseconds();
		}
		if (mouseButton!=-1 && mouseT>inputTime) {
			input = new MouseButtonInput(mouseButton);
			inputTime = mouseT;
		}
		if (mouseAxis!=-1 && mouseT>inputTime) {
			input = new MouseAxisInput(mouseAxis, mouseAxisPositive);
			inputTime = mouseT;
		}
		// Controller events
		int controllerComponent = -1;
		boolean isControllerAxis = false;
		long controllerT = 0;
		while(Controllers.next()) {
			controllerComponent = Controllers.getEventControlIndex();
			isControllerAxis = Controllers.isEventAxis();
			controllerT = Controllers.getEventNanoseconds();
		}
		if (controllerComponent!=-1 && controllerT>inputTime) {
			if (isControllerAxis)
				input = new ControllerAxisInput(controllerComponent, Controllers.getController(controllerIndex).getAxisValue(controllerComponent)>0);
			else
				input = new ControllerButtonInput(controllerComponent);
			inputTime = controllerT;
		}
		if (input!=null)
			System.out.println(input.getName());
		return input;
	}
	
	public Map<String, List<PhysicInput>> physicInputs;
	private static float MouseDX=0, MouseDY=0, MouseDWheel=0;
	
	public PhysicInputs() {
		physicInputs = new HashMap<>();
		physicInputs.put("left", new ArrayList<>(Arrays.asList(new KeyBoardInput(Keyboard.KEY_Q), new ControllerAxisInput(0, false))));
		physicInputs.put("right", new ArrayList<>(Arrays.asList(new KeyBoardInput(Keyboard.KEY_D), new ControllerAxisInput(0, true))));
		physicInputs.put("forward", new ArrayList<>(Arrays.asList(new KeyBoardInput(Keyboard.KEY_Z), new ControllerAxisInput(1, false))));
		physicInputs.put("backward", new ArrayList<>(Arrays.asList(new KeyBoardInput(Keyboard.KEY_S), new ControllerAxisInput(1, true))));
		physicInputs.put("up", new ArrayList<>(Arrays.asList(new KeyBoardInput(Keyboard.KEY_SPACE), new ControllerButtonInput(8))));
		physicInputs.put("down", new ArrayList<>(Arrays.asList(new KeyBoardInput(Keyboard.KEY_LSHIFT), new ControllerButtonInput(6))));
		physicInputs.put("action", new ArrayList<>(Arrays.asList(new MouseButtonInput(0), new ControllerButtonInput(9))));
		physicInputs.put("cameraXneg", new ArrayList<>(Arrays.asList(new MouseAxisInput(1, false), new ControllerAxisInput(3, true))));
		physicInputs.put("cameraXpos", new ArrayList<>(Arrays.asList(new MouseAxisInput(1, true), new ControllerAxisInput(3, false))));
		physicInputs.put("cameraYneg", new ArrayList<>(Arrays.asList(new MouseAxisInput(0, false), new ControllerAxisInput(2, false))));
		physicInputs.put("cameraYpos", new ArrayList<>(Arrays.asList(new MouseAxisInput(0, true), new ControllerAxisInput(2, true))));
	}
	
	public PhysicInputs(File file) {
		this();
		if (!file.exists())
			return;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			String inputName = null;
			while ((line = reader.readLine()) != null) {
				if (line.endsWith("=")) {
					inputName = line.substring(0, line.length()-1);
					if (physicInputs.get(inputName)==null)
						physicInputs.put(inputName, new ArrayList<>());
					physicInputs.get(inputName).clear();
				} else {
					physicInputs.get(inputName).add(PhysicInput.unserialize(line));
				}
			}
			reader.close();
			save(new File("controls"));
		} catch (IOException e) {
			// do nothing more
		}
	}
	
	private float getPhysicInputValue(String name) {
		List<PhysicInput> inputs = this.physicInputs.get(name);
		float value = 0f;
		for (PhysicInput input : inputs)
			if (input.getValue() > value)
				value = input.getValue();
		return value;
	}
	
	public void save(File file) {
		try {
			if (!file.exists())
			file.createNewFile();
			PrintWriter writer = new PrintWriter(file);
			for (Entry<String, List<PhysicInput>> e : physicInputs.entrySet()) {
				writer.append(e.getKey()+"=\n");
				for (PhysicInput i : e.getValue())
					writer.append(i.serialize()+"\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Controls getControls() {
		MouseDX = Mouse.getDX()/6f;
		MouseDY = Mouse.getDY()/6f;
		MouseDWheel = Mouse.getDWheel()/10f;
		Controls controls = new Controls(
				getPhysicInputValue("right") - getPhysicInputValue("left"),
				getPhysicInputValue("forward") - getPhysicInputValue("backward"),
				getPhysicInputValue("up") - getPhysicInputValue("down"),
				getPhysicInputValue("cameraXpos") - getPhysicInputValue("cameraXneg"),
				getPhysicInputValue("cameraYpos") - getPhysicInputValue("cameraYneg"),
				getPhysicInputValue("action")>0);
		return controls;
	}
	
	private static abstract class PhysicInput {
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
	}
	
	static class KeyBoardInput extends PhysicInput {
		private int key;
		public KeyBoardInput(int key) {
			this.key = key;
		}
		@Override
		public float getValue() {
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
