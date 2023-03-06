package com.Ezz.Game.Engine.entity;
import android.graphics.Canvas;
import com.Ezz.Game.Engine.file.Texture;
import org.json.JSONException;
import org.json.JSONObject;
import com.Ezz.Game.Engine.Engine;

public class TextureComponent extends Component {
	
	private Texture texture = new Texture();
    
	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	public void setTexture(String path) {
		this.texture = Engine.Assets.loadTexture(path);
	}

	public Texture getTexture() {
		return texture;
	}

	@Override
	public void draw(Canvas canvas) {
		if(texture == null || texture.getDrawable() == null) return;
		texture.getDrawable().setBounds((int) entity.calculateRenderX(), (int) entity.calculateRenderY(), (int) entity.calculateRenderX() + (int) entity.size.x, (int) entity.calculateRenderY() + (int) entity.size.y);
		texture.getDrawable().draw(canvas);
	}

	@Override
	public JSONObject toJSON() throws JSONException {
		JSONObject jo = super.toJSON();
		jo.put("texturePath", texture.getDrawablePath());
		return jo;
	}

	@Override
	public void parse(JSONObject jo) throws JSONException {
		super.parse(jo);
		String s = jo.getString("texturePath");
		if(s == null)
			texture = new Texture();
		else
			texture = Engine.Assets.loadTexture(s);
	}

	@Override
	protected Component newInstance() {
		return new TextureComponent();
	}
}
