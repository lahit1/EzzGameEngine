package com.Ezz.Game.Engine.entity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import com.Ezz.Game.Engine.Engine;
import org.json.JSONException;
import org.json.JSONObject;

public class MusicPlayerComponent extends Component {

	private MediaPlayer player = new MediaPlayer();
	private float vmax = 100;
	
	public boolean playonstart;

	private String dataPath;
	
	public boolean p;
	
	{
		player.setAudioStreamType(AudioManager.STREAM_MUSIC);
	}

	@Override
	public void act(float deltatime) {
		super.act(deltatime);
		if(playonstart) {
			play();
			playonstart = false;
		}
	}

	public void play() {
		player.start();
	}

	public void pause() {
		player.pause();
	}

	public void stop() {
		player.stop();
	}

	public boolean isPlaying() {
		return player.isPlaying();
	}

	public void reset() {
		player.reset();
	}

	public void setLooping(boolean looping) {
		player.setLooping(looping);
	}

	public boolean isLooping() {
		return player.isLooping();
	}

	public void setVolume(float level) {
		level = Math.max(0, Math.min(100, level));
		player.setVolume(level / vmax, level / vmax);
	}

	public void setVolume(float left, float right) {
		left = Math.max(0, Math.min(100, left));
		right = Math.max(0, Math.min(100, right));
		player.setVolume(left / vmax, right / vmax);
	}

	public void setMaxVolume(float Max) {
		vmax = Max;
	}

	public void setSource(String file) {
		try {
			Engine.Assets.setMediaDataSource(player, file);
			dataPath = file;
			player.prepare();
		} catch (Exception e) {}
	}

	public String getSourcePath() {
		return dataPath;
	}

	@Override
	public void parse(JSONObject jo) throws JSONException {
		super.parse(jo);
		if(jo.has("dataPath"))
			setSource(jo.getString("dataPath"));
		if(jo.has("isLooping"))
			setLooping(jo.getBoolean("isLooping"));
		if(jo.has("playOnStart"))
			playonstart = jo.getBoolean("playOnStart");
	}

	@Override
	public JSONObject toJSON() throws JSONException {
		JSONObject jo = super.toJSON();
		jo.put("dataPath", dataPath);
		jo.put("playOnStart", playonstart);
		jo.put("isLooping", isLooping());
		return jo;
	}
}
