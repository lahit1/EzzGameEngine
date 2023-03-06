package com.Ezz.Game.Engine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.Toast;
import com.Ezz.Game.Engine.entity.AnimationComponent;
import com.Ezz.Game.Engine.entity.ButtonComponent;
import com.Ezz.Game.Engine.entity.Component;
import com.Ezz.Game.Engine.entity.Entity;
import com.Ezz.Game.Engine.entity.FontComponent;
import com.Ezz.Game.Engine.entity.MusicPlayerComponent;
import com.Ezz.Game.Engine.entity.ProgressBarComponent;
import com.Ezz.Game.Engine.entity.SwitchComponent;
import com.Ezz.Game.Engine.entity.TextureComponent;
import com.Ezz.Game.Engine.luascript.LuaScript;
import com.Ezz.Game.Engine.math.Vector;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import com.Ezz.Game.Engine.entity.JoystickComponent;

public class Universe {

	protected final ArrayList<Entity> entities = new ArrayList<Entity>();
	
	protected Vector gravity = new Vector(0, -45);
	protected boolean isResuming = true;
    public int backgroundColor = Color.parseColor("#ffffff");
	public final LuaValue luaThis = CoerceJavaToLua.coerce(this);
	public final Camera Camera = new Camera();
	public final LuaValue luaCamera = CoerceJavaToLua.coerce(Camera);

	private long lasttime = System.currentTimeMillis();
	private float deltatime;

    public Universe(String universePath, Context context) {
		initializeUniverse(universePath, context);
	}

	public Universe initializeUniverse(String universePath, Context context) {
		return setUniverseData(Engine.Assets.readString(universePath), context);
	}

	public Universe setUniverseData(String universeData, Context context) {

		entities.clear();

		try {
			JSONObject jo = new JSONObject(universeData);
            backgroundColor = jo.getInt("backgroundColor");
			gravity.set((float)jo.getDouble("grav.x"), (float)jo.getDouble("grav.y"));
			JSONObject cam = jo.getJSONObject("camera");
			Camera.position.set((float)cam.getDouble("pos.x"), (float)cam.getDouble("pos.y"));
			JSONArray jae = jo.getJSONArray("entities");
			for (int i=0; i < jae.length(); i++) {
				JSONObject joe = jae.getJSONObject(i);
				Entity e = new Entity(this);
				e.ID = joe.getString("ID");
				e.pos.set((float)joe.getDouble("pos.x"), (float)joe.getDouble("pos.y"));
				e.size.set((float)joe.getDouble("size.x"), (float)joe.getDouble("size.y"));
				e.rotation = (float) joe.getDouble("rotation");
				e.setUI(joe.getBoolean("isUI"));
				JSONArray jac = joe.getJSONArray("components");
				for (int _i = 0; _i < jac.length(); _i++) {
					JSONObject joc = jac.getJSONObject(_i);
					String tn = joc.getString("typeName");
					Component c = null;
					if (tn.equals("LuaScript")) c = new LuaScript();
					else if (tn.equals("TextureComponent")) c = new TextureComponent();
					else if (tn.equals("AnimationComponent")) c = new AnimationComponent();
					else if (tn.equals("ButtonComponent")) c = new ButtonComponent();
					else if (tn.equals("SwitchComponent")) c = new SwitchComponent();
					else if (tn.equals("ProgressBarComponent")) c = new ProgressBarComponent();
                    else if (tn.equals("FontComponent")) c = new FontComponent();
					else if (tn.equals("JoystickComponent")) c = new JoystickComponent();
					else if (tn.equals("MusicPlayerComponent")) c = new MusicPlayerComponent();
					if (c != null) {
						try {
							c.parse(joc);
						} catch (JSONException jex) {}
						e.addComponent(c);
					}
				}
			}
			int fp = cam.getInt("followed");
			if (fp != -1)
				Camera.follow(entities.get(fp));
		} catch (Exception e) {
			Toast.makeText(context, e.getMessage(), 500).show();
		} finally {
			onLoaded();
		}
		return this;
	}

	public void onLoaded() {};
	public void onScreenLoaded() {}

	protected void draw(Canvas canvas) {
		Vector campos = Camera.position;
		float zoom = Camera.zoom;
		canvas.scale(zoom, zoom, campos.x / 2f, campos.y / 2f);

		long currentTime = System.currentTimeMillis();
		deltatime = (currentTime - lasttime) / 1000f;
		lasttime = currentTime;

        Vector s = Camera.getSize();
        canvas.drawColor(backgroundColor);

		for (Entity e: entities) 
			if (!e.isUI()) {
				canvas.save();
				canvas.rotate(-e.rotation, e.calculateRenderX() + e.size.x / 2, e.calculateRenderY() + e.size.y / 2);
				for (Component component: e.getComponents())
					component.draw(canvas);
				canvas.restore();
			}
		for (Entity e: entities) 
			if (e.isUI()) {
				canvas.save();
				canvas.rotate(-e.rotation, e.calculateRenderX(), e.calculateRenderY() + e.size.y);
				for (Component component: e.getComponents())
					component.draw(canvas);
				canvas.restore();
			}
		if (isResuming)
			for (Entity e: entities)
				for (Component component: e.getComponents())
					if (isResuming) component.act(deltatime);

		Entity fe = Camera.followed;
		if (fe != null && isResuming) {
			Vector p = fe.pos;
			Camera.getPosition().set(p.x, -p.y);
		}

		float scale = 1 / Camera.zoom;
		canvas.scale(scale, scale, campos.x / 2f, campos.y / 2f);
	}

	public List<Entity> getEntities() {
		return entities;
	}

	public Entity findEntity(String ID) {
		for (Entity e: entities)
			if (e.ID.equals(ID))
				return e;
		return null;
	}

	public void setResuming(boolean b) {
		isResuming = b;
	}

	public boolean isResuming() {
		return isResuming;
	}

	public Vector getGravity() {
		return gravity;
	}

	public float getDeltatime() {
		return deltatime;
	}

	public Bitmap captureBitmap() {
		Bitmap bm = Bitmap.createBitmap((int)Camera.getSize().x, (int)Camera.getSize().y, Bitmap.Config.ARGB_4444);
		draw(new Canvas(bm));
		return bm;
	}
}
