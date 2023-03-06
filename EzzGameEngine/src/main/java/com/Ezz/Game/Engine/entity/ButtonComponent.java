package com.Ezz.Game.Engine.entity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import org.json.JSONException;
import org.json.JSONObject;

public class ButtonComponent extends Component {

	private boolean isTouched;
	private Paint tp = new Paint();
    
    public ButtonComponent() {
        paint.setColor(Color.parseColor("#888888"));
        tp.setColor(Color.parseColor("#aaaaaa"));
    }

	@Override
	public void draw(Canvas canvas) {
		if(isTouched)
            canvas.drawRect(entity.calculateRenderX(), entity.calculateRenderY(), entity.calculateRenderX() + entity.size.x, entity.calculateRenderY() + entity.size.y, tp);
		else
            canvas.drawRect(entity.calculateRenderX(), entity.calculateRenderY(), entity.calculateRenderX() + entity.size.x, entity.calculateRenderY() + entity.size.y, paint);
	}

	public Paint getTouchPaint(){
		return tp;
	}

	@Override
	public void onClick() {
		isTouched = false;
	}

	@Override
	public void onTouch() {
		isTouched = true;
	}

	@Override
	public void onTouchMove() {
		isTouched = false;
	}

    @Override
    public void parse(JSONObject jo) throws JSONException {
        super.parse(jo);
        tp.setColor(jo.getInt("touchColor"));
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject jo =  super.toJSON();
        jo.put("touchColor", tp.getColor());
        return jo;
    }

	@Override
	protected Component newInstance() {
		return new ButtonComponent();
	}
}
