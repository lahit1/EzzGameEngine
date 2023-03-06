package com.Ezz.Game.Engine.entity;

import android.graphics.Canvas;
import android.graphics.Paint;
import com.Ezz.Game.Engine.math.Vector;
import org.json.JSONException;
import org.json.JSONObject;

public class ProgressBarComponent extends Component {

    Paint progressPaint = new Paint();
    int value = 50, maxValue = 100;
    
    public ProgressBarComponent(){
        //paint.setStyle(Paint.Style.STROKE);
        //paint.setStrokeWidth(stroke);
	}
    
    @Override
    public void draw(Canvas canvas) {
        float posx = entity.calculateRenderX();
		float posy = entity.calculateRenderY();
		Vector size = entity.size;

		float xp = value * size.x / maxValue;
		
        canvas.drawRect(posx, posy, posx + size.x, posy + size.y, paint);
        canvas.drawRect(posx, posy, posx + xp, posy + size.y, progressPaint);
        
        if(value >= maxValue){
            value = maxValue;
        }
    }
    
    public void setProgress(int value){
        this.value = value;
    }

	public int getProgress() {
		return value;
	}
    
    public void setMax(int value){
        this.maxValue = value;
    }

	public int getMax() {
		return maxValue;
	}

    public Paint getProgressPaint(){
        return progressPaint;
    }

	@Override
	public void parse(JSONObject jo) throws JSONException {
		value = jo.getInt("currvalue");
		maxValue = jo.getInt("maxvalue");
	}

	@Override
	public JSONObject toJSON() throws JSONException {
		JSONObject jo = super.toJSON();
		jo.put("currvalue", value);
		jo.put("maxvalue", maxValue);
		return jo;
	}

	@Override
	protected Component newInstance() {
		return new ProgressBarComponent();
	}
}
