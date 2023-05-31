package fr.actionrpg3d;

import fr.actionrpg3d.game.Game;

public class SoloGameManager extends GameManager {
	
	public SoloGameManager(Game game) {
		super(game);
		game.addPlayer(0);
	}
	
}
