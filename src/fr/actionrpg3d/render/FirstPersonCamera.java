package fr.actionrpg3d.render;

import fr.actionrpg3d.game.entities.Creature;
import fr.actionrpg3d.game.entities.Modelizable;
import fr.actionrpg3d.inputs.Controls;
import fr.actionrpg3d.math.Vector3f;

public class FirstPersonCamera extends Camera {
	
	private Creature followed = null;
	
	private float height;
	
	public FirstPersonCamera(float height) {
		super(new Vector3f());
		this.height = height;
	}

	@Override
	public void update(Controls controls) {
		if (followed == null) return;
		if (followed instanceof Modelizable) getRotation().set(((Modelizable)followed).getRotation()).mulY(-1f).addY(180);
		getPosition().set(followed.getPosition()).addY(height).mul(-1f);
	}
	
	public Creature getFollowed() {
		return followed;
	}
	
	public FirstPersonCamera setFollowed(Creature followed) {
		this.followed = followed;
		return this;
	}
	
	public float getHeight() {
		return height;
	}
	
	public FirstPersonCamera setHeight(float height) {
		this.height = height;
		return this;
	}
	
}
