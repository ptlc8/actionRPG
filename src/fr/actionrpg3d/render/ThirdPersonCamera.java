package fr.actionrpg3d.render;

import fr.actionrpg3d.game.Controls;
import fr.actionrpg3d.game.entities.Creature;
import fr.actionrpg3d.math.Vector3f;

public class ThirdPersonCamera extends Camera {
	
	private Creature followed = null;
	
	private Vector3f relativePosition;
	
	public ThirdPersonCamera(Vector3f relativePosition) {
		super(new Vector3f());
		this.relativePosition = relativePosition;
		getRotation().set(90, 0, 0);
	}

	@Override
	public void update(Controls controls) {
		if (followed == null) return;
		getPosition().set(followed.getPosition()).add(relativePosition).mul(-1f);
	}
	
	public Creature getFollowed() {
		return followed;
	}
	
	public ThirdPersonCamera setFollowed(Creature followed) {
		this.followed = followed;
		return this;
	}
	
	public Vector3f getRelativePosition() {
		return relativePosition;
	}
	
	public ThirdPersonCamera setRelativePosition(Vector3f relativePosition) {
		this.relativePosition = relativePosition;
		return this;
	}
	
}
