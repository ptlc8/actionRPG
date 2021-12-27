package fr.actionrpg3d.game;

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

public class Controls {
	
	private static int controllerIndex = 0;
	
	public static void refreshControllerIndex() {
		Controllers.poll();
		for (int i = 0; i < Controllers.getControllerCount(); i++) {
			if (Controllers.getController(i).getAxisCount()>=4)
				controllerIndex = i;
		}
	}
	
	public static Input getLastInput() {
		Input input = null;
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
	
	public Map<String, List<Input>> inputs;
	
	private float leftRightAxis=0, backwardForwardAxis=0, downUpAxis=0, cameraXAxis=0, cameraYAxis=0;
	private boolean action=false;
	
	private static float MouseDX=0, MouseDY=0, MouseDWheel=0;
	
	public Controls() {
		inputs = new HashMap<>();
		inputs.put("left", new ArrayList<>(Arrays.asList(new KeyBoardInput(Keyboard.KEY_Q), new ControllerAxisInput(0, false))));
		inputs.put("right", new ArrayList<>(Arrays.asList(new KeyBoardInput(Keyboard.KEY_D), new ControllerAxisInput(0, true))));
		inputs.put("forward", new ArrayList<>(Arrays.asList(new KeyBoardInput(Keyboard.KEY_Z), new ControllerAxisInput(1, false))));
		inputs.put("backward", new ArrayList<>(Arrays.asList(new KeyBoardInput(Keyboard.KEY_S), new ControllerAxisInput(1, true))));
		inputs.put("up", new ArrayList<>(Arrays.asList(new KeyBoardInput(Keyboard.KEY_SPACE), new ControllerButtonInput(8))));
		inputs.put("down", new ArrayList<>(Arrays.asList(new KeyBoardInput(Keyboard.KEY_LSHIFT), new ControllerButtonInput(6))));
		inputs.put("action", new ArrayList<>(Arrays.asList(new MouseButtonInput(0), new ControllerButtonInput(9))));
		inputs.put("cameraXneg", new ArrayList<>(Arrays.asList(new MouseAxisInput(1, false), new ControllerAxisInput(3, true))));
		inputs.put("cameraXpos", new ArrayList<>(Arrays.asList(new MouseAxisInput(1, true), new ControllerAxisInput(3, false))));
		inputs.put("cameraYneg", new ArrayList<>(Arrays.asList(new MouseAxisInput(0, false), new ControllerAxisInput(2, false))));
		inputs.put("cameraYpos", new ArrayList<>(Arrays.asList(new MouseAxisInput(0, true), new ControllerAxisInput(2, true))));
		poll();
	}
	
	public Controls(File file) {
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
					if (inputs.get(inputName)==null)
						inputs.put(inputName, new ArrayList<>());
					inputs.get(inputName).clear();
				} else {
					inputs.get(inputName).add(Input.unserialize(line));
				}
			}
			reader.close();
			save(new File("controls"));
		} catch (IOException e) {
			// do nothing more
		}
	}
	
	public void poll() {
		MouseDX = Mouse.getDX()/6f;
		MouseDY = Mouse.getDY()/6f;
		MouseDWheel = Mouse.getDWheel()/10f;
		leftRightAxis = getInputValue("right") - getInputValue("left");
		backwardForwardAxis = getInputValue("forward") - getInputValue("backward");
		downUpAxis = getInputValue("up") - getInputValue("down");
		cameraXAxis = getInputValue("cameraXpos") - getInputValue("cameraXneg");
		cameraYAxis = getInputValue("cameraYpos") - getInputValue("cameraYneg");
		action = getInputValue("action")>0;
	}
	
	private float getInputValue(String name) {
		List<Input> inputs = this.inputs.get(name);
		float value = 0f;
		for (Input input : inputs)
			if (input.getValue() > value)
				value = input.getValue();
		return value;
	}
	
	public void save(File file) {
		try {
			if (!file.exists())
			file.createNewFile();
			PrintWriter writer = new PrintWriter(file);
			for (Entry<String, List<Input>> e : inputs.entrySet()) {
				writer.append(e.getKey()+"=\n");
				for (Input i : e.getValue())
					writer.append(i.serialize()+"\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public float getLeftRightAxis() {
		return leftRightAxis;
	}
	public float getLeft() {
		return Math.max(0, -leftRightAxis);
	}
	public float getRight() {
		return Math.max(0, leftRightAxis);
	}
	
	public float getBackwardForwardAxis() {
		return backwardForwardAxis;
	}
	public float getForward() {
		return Math.max(0, backwardForwardAxis);
	}
	public float getBackward() {
		return Math.max(0, -backwardForwardAxis);
	}
	
	public float getDownUpAxis() {
		return downUpAxis;
	}
	public float getUp() {
		return Math.max(0, downUpAxis);
	}
	public float getDown() {
		return Math.max(0, -downUpAxis);
	}
	
	public float getCameraXAxis() {
		return cameraXAxis;
	}
	
	public float getCameraYAxis() {
		return cameraYAxis;
	}
	
	public boolean getAction() {
		return action;
	}
	
	
	private static abstract class Input {
		abstract float getValue();
		abstract String getName();
		abstract String serialize();
		static Input unserialize(String str) {
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
	
	static class KeyBoardInput extends Input {
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
	
	static class MouseButtonInput extends Input {
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
	
	static class MouseAxisInput extends Input {
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
	
	static class ControllerAxisInput extends Input {
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
	
	static class ControllerButtonInput extends Input {
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
