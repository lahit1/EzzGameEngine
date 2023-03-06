package com.Ezz.Game.Engine.util;

public class Key {

	public String ID;

	public Key(String ID) {
		this.ID = ID;
	}

	protected boolean isTouched;
	public boolean isPressed() {return isTouched;};
	public void set(boolean v) {isTouched=v;}
}
