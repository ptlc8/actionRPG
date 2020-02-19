package fr.actionrpg3d.render;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import fr.actionrpg3d.math.Vector3f;

public class Model {
	
	private List<Shape> shapes;
	
	public Model(Shape... shapes) {
		this.shapes = new ArrayList<Model.Shape>();
		for (Shape shape : shapes) {
			this.shapes.add(shape);
		}
	}
	
	public Model(String pathname) {
		this.shapes = new ArrayList<Model.Shape>();
		InputStream resource = Model.class.getResourceAsStream(pathname);
		if (resource == null){
			System.err.println("Impossible de trouver la ressource : " + pathname);
			return;
		}
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(resource));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("#")) continue;
				String[] coords = line.split(" ");
				Vector3f[] vectors = new Vector3f[coords.length/3];
				for (int i = 0; i < coords.length/3; i++) {
					vectors[i] = new Vector3f(Integer.parseInt(coords[i*3]), Integer.parseInt(coords[i*3+1]), Integer.parseInt(coords[i*3+2]));
				}
				shapes.add(new Shape(vectors));
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<Shape> getShapes() {
		return shapes;
	}

	public class Shape {
		
		private Vector3f[] vectors;
		
		public Shape(Vector3f... vectors) {
			this.vectors = vectors;
		}

		public Vector3f[] getVectors() {
			return vectors;
		}
		
	}
	
}
