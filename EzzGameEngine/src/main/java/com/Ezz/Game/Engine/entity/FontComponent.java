package com.Ezz.Game.Engine.entity;

import android.graphics.Canvas;
import org.json.JSONException;
import org.json.JSONObject;

public class FontComponent extends Component {

    private String text = "Font";
    private float fontsize = 25;

    @Override
    public void draw(Canvas canvas) {
		paint.setTextSize(fontsize);
        canvas.drawText(text,entity.calculateRenderX(),entity.calculateRenderY() + entity.size.y, paint);
    }

    public void setSize(float size){
        this.fontsize = size;
    }
    
    public float getSize() {
        return fontsize;
    }

    public void setText(String text){
        this.text = text;
    }
    
    public String getText() {
        return text;
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject jo = super.toJSON();
        jo.put("text", text);
        jo.put("fontSize", fontsize);
        return jo;
    }

    @Override
    public void parse(JSONObject jo) throws JSONException {
        super.parse(jo);
        text = jo.getString("text");
        fontsize = jo.getInt("fontSize");
    }

	@Override
	protected Component newInstance() {
		return new FontComponent();
	}
}
