package com.Ezz.Game.Engine.luascript;

import com.Ezz.Game.Engine.Engine;
import com.Ezz.Game.Engine.entity.Component;
import org.json.JSONException;
import org.json.JSONObject;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.BiConsumer;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.json.JSONArray;
import com.Ezz.Game.Engine.math.Vector;


public class LuaScript extends Component {

	protected Globals globals = EzzGlobals.standardGlobals();
	private LuaValue create, update;
	protected boolean created;
	protected String scriptPath;

    public void init(String sPath) {
		created = false;
		scriptPath = sPath;
	}

	@Override
	public void act(float deltatime) {
		try {
			if (!created) {
				globals.load(Engine.Assets.readString(scriptPath))
					.call();
				create = globals.get("create");
				update = globals.get("update");
				if (create != null && create != LuaValue.NIL) {
					create.call();
				}
				created = true;
			}
			if (update != null && update != LuaValue.NIL) update.call(LuaValue.valueOf(deltatime));
		} catch (Exception ex) {
			Engine.logListener.error(ex.getMessage() + '\n');
		}
	}

	public Globals getGlobals() {
		return globals;
	}

	public String getScriptPath() {
		return scriptPath;
	}

	@Override
	public JSONObject toJSON() throws JSONException {
		JSONObject jo = super.toJSON();
		jo.put("scriptPath", scriptPath);
		return jo;
	}

	@Override
	public void parse(JSONObject jo) throws JSONException {
		super.parse(jo);
		if (jo.has("scriptPath"))
			init(jo.getString("scriptPath"));
	}

	@Override
	protected Component newInstance() {
		return new LuaScript();
	}
}
