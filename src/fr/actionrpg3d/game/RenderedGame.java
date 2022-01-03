package fr.actionrpg3d.game;

import fr.actionrpg3d.game.entities.Player;
import fr.actionrpg3d.render.Camera;
import fr.actionrpg3d.render.FirstPersonCamera;
import fr.actionrpg3d.render.ThirdPersonCamera;

public class RenderedGame {
	
	private final Camera camera;
	private final Game game;
	private int playerId;
	
	private boolean debug = false;
	
	public RenderedGame(Camera camera, Game game, int playerId) {
		this.camera = camera;
		this.game = game;
		this.playerId = playerId;
		if (camera instanceof FirstPersonCamera)
			((FirstPersonCamera)camera).setFollowed(getPlayerEntity());
		if (camera instanceof ThirdPersonCamera)
			((ThirdPersonCamera)camera).setFollowed(getPlayerEntity());
	}
	
	public Camera getCamera() {
		return camera;
	}
	
	public Game getGame() {
		return game;
	}
	
	public Player getPlayerEntity() {
		return game.getPlayers().get(playerId);
	}
	
	public boolean isDebug() {
		return debug;
	}
	
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
}
