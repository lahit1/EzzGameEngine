package com.Ezz.Game.Engine.util;

import com.Ezz.Game.Engine.Engine;
import android.content.Context;
import android.app.Activity;

public class EngineSystem {
	
	public void write(String msg) {
		Engine.logListener.message(msg);
	}
	
	public void writeln(String msg) {
		write(msg + '\n');
	}
	
	public void startUniverse(String upath) {
		Engine.startUniverse(upath);
	}
}
