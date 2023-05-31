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
import org.lwjgl.input.Keyboard;

import fr.actionrpg3d.inputs.PhysicInput.*;

public class PhysicInputsSource {
	
	public Map<String, List<PhysicInput>> physicInputs;
	
	public PhysicInputsSource() {
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
	
	public PhysicInputsSource(File file) {
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
	
	public float getInputValue(InputName name) {
		List<PhysicInput> inputs = this.physicInputs.get(name.getName());
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
	
}
