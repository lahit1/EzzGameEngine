package com.Ezz.Game.Engine.file;

import android.annotation.NonNull;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import java.io.InputStream;
import com.Ezz.Game.Engine.Engine;

public class Texture {
	
	private Drawable drawable = null;
	private String drawablePath;

	public Drawable getDrawable(){
		return drawable;
	}
	
	public void setDrawable(@NonNull Drawable drawable){
		this.drawable = drawable;
	}
	
	public void setBitmap(@NonNull Bitmap bitmap) {
		drawable = new BitmapDrawable(bitmap);
	}
	
	public String getDrawablePath() {
		return drawablePath;
	}
	
	public void setDrawablePath(String path) {
		this.drawablePath = path;
	}
	
	public Texture set(Texture ref) {
		drawable = ref.drawable;
		drawablePath = ref.drawablePath;
		return this;
	}
}
