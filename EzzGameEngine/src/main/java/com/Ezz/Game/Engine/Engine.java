package com.Ezz.Game.Engine;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import com.Ezz.Game.Engine.entity.Component;
import com.Ezz.Game.Engine.entity.Entity;
import com.Ezz.Game.Engine.util.Key;
import java.util.ArrayList;
import java.util.List;
import com.Ezz.Game.Engine.file.AssetLoader;
import com.Ezz.Game.Engine.util.EngineSystem;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import android.widget.Toast;

public class Engine {
    protected static Universe cUniverse = null;
	private static ProjectionView prView;
    public static Context context;
	public static AssetLoader Assets;
	public static LuaValue luaAssets;
	public static LogListener logListener = new LogListener() {

		@Override
		public void message(String msg) {
			System.out.println(msg);
		}

		@Override
		public void warn(String msg) {
			System.out.println(msg);
		}

		@Override
		public void error(String error) {
			System.out.println(error);
		}

	};

	public static final ArrayList<Component> components = new ArrayList<Component>();

	Engine() {}

    public static void init(Context context) {
        Engine.context = context;
        prView = (ProjectionView) newProjectionView(context);
        Assets = new AssetLoader(context);
		luaAssets = CoerceJavaToLua.coerce(Assets);
    }

	public static Universe startUniverse(Universe universe) {
		return (cUniverse = universe);
	}
	
	public static Universe startUniverse(String upath) {
		return (cUniverse = new Universe(upath, context));
	}

	public static Universe getUniverse() {
		return cUniverse;
	}
	
	public static View newProjectionView(Context context) {
		return new ProjectionView(context);
	}
	
	public interface LogListener {
		public void message(String msg);
		public void error(String error);
		public void warn(String warn);
	}
	
	public static void toast(String message, int duration) {
		Toast.makeText(context, message, duration).show();
	}
}

class ProjectionView extends View implements OnTouchListener {

	private Entity lastTouchedEntity;

	public ProjectionView(Context context) {
		super(context);
		setOnTouchListener(this);
	}

	@Override
	public boolean onTouch(View p1, MotionEvent p2) {
		if(!Engine.cUniverse.isResuming) return true;
		Input.isDown = p2.getAction() != MotionEvent.ACTION_UP;
		Input.action = p2.getAction();
		Input.pos.set(p2.getX() - getWidth() / 2, getHeight() / 2 - p2.getY());
		for (final Entity e: Engine.cUniverse.entities) {
			if (e.pos.x - e.size.x / 2 < Input.pos.x && Input.pos.x < e.pos.x + e.size.x / 2 && e.pos.y - e.size.y / 2 < Input.pos.y && Input.pos.y < e.pos.y + e.size.y / 2) {
				lastTouchedEntity = e;
				switch (p2.getAction()) {
					case MotionEvent.ACTION_UP:
						for (Component component: e.getComponents())
							component.onClick();
						break;
					default:
						for (Component component: e.getComponents())
							component.onTouch();
						break;
				}
			} else if (e == lastTouchedEntity) {
				lastTouchedEntity = null;
				for (Component component: e.getComponents())
					component.onTouchMove();
			}
		}
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Engine.cUniverse.draw(canvas);
		invalidate();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		Engine.getUniverse().Camera.size.set(w, h);
		if(oldw == 0 && oldh == 0) Engine.cUniverse.onScreenLoaded();
	}
}
