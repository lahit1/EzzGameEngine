package com.Ezz.Game.Engine.file;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import java.io.IOException;
import java.io.InputStream;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import java.util.HashMap;
import android.media.MediaPlayer;

public class AssetLoader {
    
    private android.content.res.AssetManager aam;
	protected double quality = 0.5;

    public AssetLoader(Context context) {
        aam = context.getAssets();
    }
    
    public String readString(String asset) {
        try {
            InputStream is = aam.open(asset);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            return new String(buffer);
        } catch (IOException e) {}
        return null;
    }

    public Texture loadTexture(String path) {
		Texture ret = new Texture();
		try {
			Bitmap bitmap = BitmapFactory.decodeStream(aam.open(path));
			ret.setDrawable(new BitmapDrawable(Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getHeight() * quality), (int)(bitmap.getWidth() * quality), false)));
			ret.setDrawablePath(path);
		} catch (IOException e) {}
        return ret;
    }

	public void setMediaDataSource(MediaPlayer mp, String path) {
		try {
			mp.setDataSource(aam.openFd(path));
		} catch(Exception e) {}
	}
	public void setQuality(double quality) {
		this.quality = quality;
	}
	
	public double getQuality() {
		return quality;
	}
}
