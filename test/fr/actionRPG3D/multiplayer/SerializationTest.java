package fr.actionRPG3D.multiplayer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.jupiter.api.Test;

import fr.actionrpg3d.game.Game;

class SerializationTest {
	
	@Test
	void testSerializable() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream outObj = new ObjectOutputStream(out);
		outObj.writeInt(8);
		outObj.close();
		ObjectInputStream inObj = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
		assertEquals(8, inObj.readInt());
	}
	
	@Test
	void testGameSerializable() throws IOException, ClassNotFoundException {
		Game game = new Game(6);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream outObj = new ObjectOutputStream(out);
		outObj.writeObject(game);
		outObj.close();
		ObjectInputStream inObj = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
		Game copy = (Game) inObj.readObject();
		assertEquals(game, copy);
	}
	
}
