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
			Vector3f kolor = new Vector3f(1, 1, 1);
			String line;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("#")) continue;
				if (line.startsWith("@c")) {
					String[] kolors = line.replaceFirst("@c", "").trim().split(" ");
					kolor = new Vector3f(Float.parseFloat(kolors[0]), Float.parseFloat(kolors[1]), Float.parseFloat(kolors[2]));
				} else {
					String[] coords = line.split(" ");
					Vector3f[] vectors = new Vector3f[coords.length/3];
					for (int i = 0; i < coords.length/3; i++) {
						vectors[i] = new Vector3f(Float.parseFloat(coords[i*3]), Float.parseFloat(coords[i*3+1]), Float.parseFloat(coords[i*3+2]));
					}
					shapes.add(new Shape(kolor, vectors));
				}
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
		private Vector3f color;
		
		public Shape(Vector3f color, Vector3f... vectors) {
			this.color = color;
			this.vectors = vectors;
		}

		public Vector3f[] getVectors() {
			return vectors;
		}
		
		public Vector3f getColor() {
			return color;
		}
		
	}
	
}
