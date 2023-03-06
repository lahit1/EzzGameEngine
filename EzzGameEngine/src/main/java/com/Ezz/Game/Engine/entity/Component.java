package com.Ezz.Game.Engine.entity;

import android.graphics.Canvas;
import android.graphics.Paint;
import org.json.JSONObject;
import org.json.JSONException;
import java.io.IOException;

public class Component {
	
	protected Entity entity;
	protected Paint paint = new Paint();
	private boolean isVisible = true;

	public void draw(Canvas canvas) {}
	public void act(float deltatime) {}
	public void onClick() {}
	public void onTouch() {}
	public void onTouchMove() {}

	public Paint getPaint(){
		return paint;
	}
	
	public void parse(JSONObject jo) throws JSONException{
		paint.setColor(jo.getInt("color"));
		if(jo.has("isVisible"))
			isVisible = jo.getBoolean("isVisible");
	}
	
	public JSONObject toJSON() throws JSONException{
		JSONObject jo = new JSONObject();
		jo.put("typeName", getClass().getSimpleName());
		jo.put("color", paint.getColor());
		jo.put("isVisible", isVisible);
		return jo;
	}
	
	public Entity getEntity() {
		return entity;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public boolean isVisible() {
		return isVisible;
	}

	@Override
	public Component clone() {
		Component ret = newInstance();
		try {
			ret.parse(toJSON());
		} catch (JSONException e) {}
		return ret;
	}
	
	protected Component newInstance() {
		return new Component();
	}
}
