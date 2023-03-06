package tm.Ezz.Game.Engine;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.graphics.Color;

public class Tools {
    
    public static Drawable transparentDrawable = new BitmapDrawable();
    
    public static void rippleRoundStroke(final View _view, final int _focus, final String _pressed, final double _round, final double _stroke, final int _strokeclr) {
        android.graphics.drawable.GradientDrawable GG = new android.graphics.drawable.GradientDrawable();
        GG.setColor(_focus);
        GG.setCornerRadius((float)_round);
        GG.setStroke((int) _stroke,
                     _strokeclr);
        android.graphics.drawable.RippleDrawable RE = new android.graphics.drawable.RippleDrawable(new android.content.res.ColorStateList(new int[][]{new int[]{}}, new int[]{ Color.parseColor(_pressed)}), GG, null);
        _view.setBackground(RE);
    }
}
