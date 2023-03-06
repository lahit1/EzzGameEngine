package com.Ezz.Game.Engine.entity;

import android.graphics.Canvas;
import android.graphics.Paint;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import android.graphics.Color;

public class SwitchComponent extends Component {

	private Paint selectedPaint = new Paint(),buttonPaint = new Paint();
	boolean checked;
    
    public SwitchComponent() {
        paint.setColor(Color.parseColor("#777777"));
        selectedPaint.setColor(Color.parseColor("#007700"));
    }

	@Override
	public void draw(Canvas canvas) {
		if (checked) {
			canvas.drawOval(entity.calculateRenderX(), entity.calculateRenderY(), entity.calculateRenderX() + entity.size.x / 2, entity.calculateRenderY() + entity.size.y, selectedPaint);
            canvas.drawOval(entity.calculateRenderX() + entity.size.x / 2, entity.calculateRenderY(), entity.calculateRenderX() + entity.size.x, entity.calculateRenderY() + entity.size.y, selectedPaint);
            canvas.drawRect(entity.calculateRenderX() + entity.size.x/4, entity.calculateRenderY(), entity.calculateRenderX() + entity.size.x/4*3, entity.calculateRenderY() + entity.size.y, selectedPaint);
            canvas.drawOval(entity.calculateRenderX() + entity.size.x / 2, entity.calculateRenderY(), entity.calculateRenderX() + entity.size.x, entity.calculateRenderY() + entity.size.y, buttonPaint);
		} else {
			canvas.drawOval(entity.calculateRenderX(), entity.calculateRenderY(), entity.calculateRenderX() + entity.size.x / 2, entity.calculateRenderY() + entity.size.y, paint);
            canvas.drawOval(entity.calculateRenderX() + entity.size.x / 2, entity.calculateRenderY(), entity.calculateRenderX() + entity.size.x, entity.calculateRenderY() + entity.size.y, paint);
            canvas.drawRect(entity.calculateRenderX() + entity.size.x/4, entity.calculateRenderY(), entity.calculateRenderX() + entity.size.x/4*3, entity.calculateRenderY() + entity.size.y, paint);
            canvas.drawOval(entity.calculateRenderX(), entity.calculateRenderY(), entity.calculateRenderX() + entity.size.x / 2, entity.calculateRenderY() + entity.size.y, buttonPaint);
		}
	}

    public Paint getSelectedPaint() {
        return selectedPaint;
    }
    
	public Paint getButtonPaint() {
		return buttonPaint;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public boolean isChecked() {
		return checked;
	}

	@Override
	public void onClick() {
		checked = !checked;
	}

	@Override
	public void parse(JSONObject jo) throws JSONException {
		super.parse(jo);
        selectedPaint.setColor(jo.getInt("selectedColor"));
		buttonPaint.setColor(jo.getInt("switchColor"));
		checked = jo.getBoolean("isChecked");
	}

	@Override
	public JSONObject toJSON() throws JSONException {
		JSONObject jo = super.toJSON();
        jo.put("selectedColor", selectedPaint.getColor());
		jo.put("switchColor", buttonPaint.getColor());
		jo.put("isChecked", checked);
		return jo;
	}

	@Override
	protected Component newInstance() {
		return new SwitchComponent();
	}
}
