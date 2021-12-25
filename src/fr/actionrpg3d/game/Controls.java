package fr.actionrpg3d.game;

import org.lwjgl.input.Controllers;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class Controls {
	
	public Input leftInput = new KeyBoardInput(Keyboard.KEY_Q);
	public Input rightInput = new KeyBoardInput(Keyboard.KEY_D);
	public Input leftInput2 = new ControllerAxisInput(0, false);
	public Input rightInput2 = new ControllerAxisInput(0, true);
	public Input forwardInput = new KeyBoardInput(Keyboard.KEY_Z);
	public Input backwardInput = new KeyBoardInput(Keyboard.KEY_S);
	public Input forwardInput2 = new ControllerAxisInput(1, false);
	public Input backwardInput2 = new ControllerAxisInput(1, true);
	public Input upInput = new KeyBoardInput(Keyboard.KEY_SPACE);
	public Input downInput = new KeyBoardInput(Keyboard.KEY_LSHIFT);
	public Input upInput2 = new ControllerButtonInput(8);
	public Input downInput2 = new ControllerButtonInput(6);
	public Input actionInput = new MouseInput(0);
	public Input actionInput2 = new ControllerButtonInput(9);
	public Input cameraXnegInput = new MouseAxis(1, false);
	public Input cameraXposInput = new MouseAxis(1, true);
	public Input cameraXnegInput2 = new ControllerAxisInput(3, true);
	public Input cameraXposInput2 = new ControllerAxisInput(3, false);
	public Input cameraYnegInput = new MouseAxis(0, false);
	public Input cameraYposInput = new MouseAxis(0, true);
	public Input cameraYnegInput2 = new ControllerAxisInput(2, false);
	public Input cameraYposInput2 = new ControllerAxisInput(2, true);
	
	private float leftRightAxis=0, backwardForwardAxis=0, downUpAxis=0, cameraXAxis=0, cameraYAxis=0;
	private boolean action=false;
	
	private float MouseDX=0, MouseDY=0, MouseDWheel=0;
	
	public Controls() {
		poll();
	}
	
	public void poll() {
		MouseDX = Mouse.getDX()/6;
		MouseDY = Mouse.getDY()/6;
		MouseDWheel = Mouse.getDWheel()/10;
		leftRightAxis = Math.max(rightInput.getValue(),rightInput2.getValue()) - Math.max(leftInput.getValue(),leftInput2.getValue());
		backwardForwardAxis = Math.max(forwardInput.getValue(),forwardInput2.getValue()) - Math.max(backwardInput.getValue(),backwardInput2.getValue());
		downUpAxis = Math.max(upInput.getValue(),upInput2.getValue()) - Math.max(downInput.getValue(),downInput2.getValue());
		cameraXAxis = Math.max(cameraXposInput.getValue(),cameraXposInput2.getValue()) - Math.max(cameraXnegInput.getValue(),cameraXnegInput2.getValue());
		cameraYAxis = Math.max(cameraYposInput.getValue(),cameraYposInput2.getValue()) - Math.max(cameraYnegInput.getValue(),cameraYnegInput2.getValue());
		action = actionInput.getValue()>0 || actionInput2.getValue()>0;
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
	
	
	private interface Input {
		float getValue();
		String getName();
	}
	
	private class KeyBoardInput implements Input {
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
	}
	
	private class MouseInput implements Input {
		private int button;
		public MouseInput(int button) {
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
	}
	
	private class MouseAxis implements Input {
		private int axis;
		private boolean positive;
		public MouseAxis(int axis, boolean positive) {
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
			return axis==2 ? "MouseWheel" : ("MouseMoveAxis"+axis);
		}
	}
	
	private class ControllerAxisInput implements Input {
		private int index;
		private boolean positive;
		public ControllerAxisInput(int index, boolean positive) {
			this.index = index;
			this.positive = positive;
		}
		@Override
		public float getValue() {
			if (Controllers.getControllerCount()==0 || Controllers.getController(0).getAxisCount()<=index)
				return 0;
			return positive ? Math.max(0, Controllers.getController(0).getAxisValue(index)) : Math.max(0, -Controllers.getController(0).getAxisValue(index));
		}
		@Override
		public String getName() {
			return Controllers.getControllerCount()>0 ? Controllers.getController(0).getAxisName(index) : "GamepadAxis "+index;
		}
	}
	
	private class ControllerButtonInput implements Input {
		private int index;
		public ControllerButtonInput(int index) {
			this.index = index;
		}
		@Override
		public float getValue() {
			if (Controllers.getControllerCount()==0 || Controllers.getController(0).getButtonCount()<=index)
				return 0;
			return Controllers.getController(0).isButtonPressed(index) ? 1 : 0;
		}
		@Override
		public String getName() {
			return Controllers.getControllerCount()>0 ? Controllers.getController(0).getButtonName(index) : "GamepadButton "+index;
		}
	}
	
}
