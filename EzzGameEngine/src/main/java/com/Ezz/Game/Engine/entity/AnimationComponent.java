package com.Ezz.Game.Engine.entity;

import android.graphics.Canvas;
import com.Ezz.Game.Engine.file.Texture;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import com.Ezz.Game.Engine.Engine;

public class AnimationComponent extends Component {

    private ArrayList<Texture> frames = new ArrayList<Texture>();
	private float rename;
	private float changeTime = 0.15f;

	private boolean isPlaying = true;

	@Override
	public void act(float deltatime) {
		if(isPlaying)
			rename += deltatime / changeTime;
		rename %= frames.size();
	}

	@Override
	public void draw(Canvas canvas) {
		if(frames.isEmpty()) return;
		Texture t = frames.get((int)(rename));
		if(t == null || t.getDrawable() == null) return;
		t.getDrawable().setBounds((int) entity.calculateRenderX(), (int) entity.calculateRenderY(), (int) entity.calculateRenderX() + (int) entity.size.x, (int) entity.calculateRenderY() + (int) entity.size.y);
		t.getDrawable().draw(canvas);
	}

	public void setChangeTime(float sec){
		changeTime = sec;
	}

	public float getChangeTime(){
		return changeTime;
	}
	
	public ArrayList<Texture> getFrames() {
		return frames;
	}
	
	public void addFrames(Texture... frames) {
		for(Texture t: frames)
			this.frames.add(t);
	}

	public void play() {
		isPlaying = true;
		
	}

	public void pause() {
		isPlaying = false;
	}

	public void stop() {
		isPlaying = false;
		changeTime = 0;
	}

	@Override
	protected Component newInstance() {
		return new AnimationComponent();
	}

	@Override
	public void parse(JSONObject jo) throws JSONException {
		super.parse(jo);
		changeTime = (float)jo.getDouble("changeTime");
		JSONArray fl = jo.getJSONArray("frames");
		for(int i=0; i < fl.length(); i++)
			frames.add(Engine.Assets.loadTexture(fl.getString(i)));
	}

	@Override
	public JSONObject toJSON() throws JSONException {
		JSONObject jo = super.toJSON();
		jo.put("changeTime", changeTime);
		JSONArray fl = new JSONArray();
		
		for(Texture t: frames)
			fl.put(t.getDrawablePath());
		
		jo.put("frames", fl);
		return jo;
	}
	
	
}
