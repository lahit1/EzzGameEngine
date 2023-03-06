package com.Ezz.Game.Engine.entity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import com.Ezz.Game.Engine.Input;
import com.Ezz.Game.Engine.math.Vector;
import org.json.JSONException;
import org.json.JSONObject;

public class JoystickComponent extends Component {

	final Paint knob = new Paint();
	
	public final Vector value = new Vector();
	
	boolean isOS;

	{
		paint.setColor(Color.BLACK);
		knob.setColor(Color.GRAY);
	}
	
	public Paint getKnobPaint() {
		return knob;
	}

	@Override
	public void act(float deltatime) {
		super.act(deltatime);
		if(!Input.isDown && isOS) {
			value.reset();
			isOS = false;
		}else if(isOS) {
			value.set(Input.pos);
			value.x -= entity.pos.x;
			value.y = entity.pos.y - value.y;
			float p = Math.max( value.distance(), entity.size.distance() / 2);
			value.x *= entity.size.x / 2 / p;
			value.y *= entity.size.y / 2 / p;
		}
	}

	@Override
	public void draw(Canvas canvas) {
		//super.draw(canvas);
		Vector s = entity.size;
		float x = entity.calculateRenderX(), y = entity.calculateRenderY();
		canvas.drawOval(x, y, x + s.x, y + s.y, paint);
		x += value.x;
		y += value.y;
		canvas.drawOval(x , y, x + s.x, y + s.y, knob);
	}

	@Override
	public void onTouch() {
		super.onTouchMove();
		isOS = true;
	}

	@Override
	public JSONObject toJSON() throws JSONException {
		JSONObject jo = super.toJSON();
		jo.put("knobColor", knob.getColor());
		return jo;
	}

	@Override
	public void parse(JSONObject jo) throws JSONException {
		super.parse(jo);
		knob.setColor(jo.getInt("knobColor"));
	}

}
