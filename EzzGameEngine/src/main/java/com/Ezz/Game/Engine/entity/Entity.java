package com.Ezz.Game.Engine.entity;

import android.annotation.NonNull;
import com.Ezz.Game.Engine.Universe;
import com.Ezz.Game.Engine.luascript.LuaScript;
import com.Ezz.Game.Engine.math.Vector;
import java.util.ArrayList;
import java.util.List;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.Globals;

public class Entity {

	private Universe universe;
	public String ID;
	public final Vector 
		pos = new Vector(),
		size = new Vector(100, 100);
	public float rotation;
	private List<Component> components = new ArrayList<Component>();
	private boolean isUI = false;
	protected LuaValue luaThis = CoerceJavaToLua.coerce(this);

	public Entity(@NonNull Universe universe) {
		this.universe = universe;
		ID = Integer.toHexString(hashCode());
		universe.getEntities().add(this);
	}

	public Universe getUniverse() {
		return universe;
	}

	public void rotate(float degrees) {
		rotation += degrees;
	}

	public List<Component> getComponents() {
		return components;
	}

	public Component findComponent(String typeName, int index) {
		typeName = typeName.toLowerCase();
		for (Component c: components)
			if (c.getClass().getSimpleName().toLowerCase().replace("component", "").equals(typeName.toLowerCase().replace("component", "")))
				if (index-- == 0)
					return c;
		return null;
	}

	public Component findComponent(String typeName) {
		return findComponent(typeName, 0);
	}

	public void addComponent(@NonNull Component component) {
		components.add(component);
		component.entity = this;
		if (component instanceof LuaScript) {
			Globals globals = ((LuaScript)component).getGlobals();
			globals.set("myEntity", luaThis);
			globals.set("myUniverse", universe.luaThis);
			globals.set("Camera", getUniverse().luaCamera);
		}
	}

	public void setUI(boolean v) {
		isUI = v;
	}

	public boolean isUI() {
		return isUI;
	}

	public void lookTo(Vector t) {
		rotation = pos.getAngle(t);
	}

	public float calculateRenderX() {
		return pos.x - (isUI ? 0 : universe.Camera.getPosition().x) - size.x / 2 + universe.Camera.getSize().x / 2;
	}

	public float calculateRenderY() {
		return -pos.y + universe.Camera.getSize().y / 2 - size.y / 2 - (isUI ? 0 : universe.Camera.getPosition().y);
	}

    @Override
    public Entity clone() {
        Entity ret = new Entity(universe);
        ret.ID = ID;
        ret.pos.set(pos);
        ret.size.set(size);
        ret.rotation = rotation;
        for (Component c: components)
			ret.addComponent(c.clone());
        ret.isUI = isUI;
		ret.universe = universe;
        return ret;
    }
}
