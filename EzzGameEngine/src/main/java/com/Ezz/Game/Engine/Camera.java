package com.Ezz.Game.Engine;

import com.Ezz.Game.Engine.entity.Entity;
import com.Ezz.Game.Engine.math.Vector;

public class Camera {

	protected Vector position = new Vector();
	protected Vector size = new Vector();
	protected Entity followed = null;
	public float zoom = 1;

	public Vector getPosition() {
		return position;
	}

	public Vector getSize() {
		return size;
	}
	
	public void follow(Entity ent) {
		followed = ent;
	}
	
	public Entity getFollowed() {
		return followed;
	}
}
