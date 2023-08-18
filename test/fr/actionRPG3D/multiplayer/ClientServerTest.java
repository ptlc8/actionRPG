package fr.actionRPG3D.multiplayer;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;
import java.util.Random;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import fr.actionrpg3d.Client;
import fr.actionrpg3d.Server;
import fr.actionrpg3d.inputs.Input;
import fr.actionrpg3d.inputs.InputName;

class ClientServerTest {
	
	static Server server;
	static Client[] clients = new Client[2];

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		server = new Server();
		for (int i = 0; i < clients.length; i++) {
			clients[i] = new Client(i != 0);
			clients[i].joinServer();
			Thread.sleep(200);
		}
		Thread.sleep(200);
		for (Client c : clients) {
			InputName input = new InputName[]{InputName.LEFT,InputName.RIGHT,InputName.FORWARD,InputName.BACKWARD}[new Random().nextInt(4)];
			c.getGameManager().receiveInput(c.getSelfId(), input, 1);
			Thread.sleep(200);
			c.getGameManager().receiveInput(c.getSelfId(), input, 0);
		}
		Thread.sleep(1000);
	}
	
	@Test 
	void testClientsConnected() {
		for (Client c : clients)
			assertTrue(c.getSocket().isConnected());
	}
	
	@Test 
	void testServerConnected() {
		assertEquals(clients.length, server.getClientsCount());
	}

	@Test
	void testPlayersCount() {
		assertEquals(clients.length, server.getGameManager().getGame().getPlayers().size());
		for (Client c : clients)
			assertEquals(clients.length, c.getGameManager().getGame().getPlayers().size());
	}

	@Test
	void testPlayersIds() {
		for (Client c : clients)
			for (Client o : clients)
				assertNotNull(c.getGameManager().getGame().getPlayers().get(o.getSelfId()));
	}
	
	@Test
	void testSyncInputs() {
		for (Client c : clients) {
			Iterator<Input> serverIt = server.getGameManager().getAllInputs().iterator();
			Iterator<Input> clientIt = c.getGameManager().getAllInputs().iterator();
			while (serverIt.hasNext())
				assertEquals(serverIt.next(), clientIt.next());
		}
	}
	
	@Test
	void testSameDungeon() {
		for (Client c : clients)
			assertEquals(server.getGameManager().getGame().getDungeon().getStartPoint(), c.getGameManager().getGame().getDungeon().getStartPoint());
	}
	
	@Test
	void testNoEntityOverride() {
		for (Client c : clients) {
			assertTrue(server.getGameManager().getGame().getEntities().values().contains(server.getGameManager().getGame().getPlayers().get(c.getSelfId())));
			for (Client o : clients)
				assertTrue(o.getGameManager().getGame().getEntities().values().contains(server.getGameManager().getGame().getPlayers().get(c.getSelfId())));
		}
	}
	
	@Test
	void testSyncPos() {
		for (Client c : clients)
			for (Client o : clients)
				assertEquals(
						server.getGameManager().getGame().getPlayers().get(o.getSelfId()).getPosition(),
						c.getGameManager().getGame().getPlayers().get(o.getSelfId()).getPosition()
				);
	}
	
	@Test @Disabled
	void testSyncAll() {
		for (Client c : clients)
			assertEquals(server.getGameManager().getGame(), c.getGameManager().getGame());
	}

}
