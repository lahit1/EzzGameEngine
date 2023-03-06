package tm.Ezz.Game.Engine;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Adapter;
import android.widget.AdapterView.OnItemSelectedListener;
import java.util.Locale;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.graphics.Color;
import android.app.Dialog;
import android.graphics.Typeface;
import android.view.View.OnClickListener;

public class SettingsActivity extends Activity {
    
	String flang;
	LinearLayout bar,sl;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
		
		bar = findViewById(R.id.bar);
		bar.setClipToOutline(true);
		
        final String[] ls = {"English", "Türkçe", "Portugal", "हिन्दी", "إلى السيارة"};
		final String[] ll = {"en", "tr", "pt", "hi", "ar"};
			
        sl = findViewById(R.id.setLanguage);
        Tools.rippleRoundStroke(sl, Color.WHITE, "#BDBDBD", 0, 0, Color.WHITE);
		sl.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View p1) {
                    Dialog dl = new Dialog(SettingsActivity.this);
                    View v = getLayoutInflater().inflate(R.layout.dialog_language,null);
                    LinearLayout backL = v.findViewById(R.id.backL);
                    
                    LinearLayout lg_tr = v.findViewById(R.id.lg_tr); Tools.rippleRoundStroke(lg_tr, Color.WHITE, "#BDBDBD", 0, 0, Color.WHITE); lg_tr.setOnClickListener(new OnClickListener(){ @Override public void onClick(View p1) { LocaleHelper.setLocale(getApplication(), ll[1]);
								if(ll[1] != flang)
									Toast.makeText(SettingsActivity.this, App.Resources.getString(R.string.restart_app_for_change_language), 500).show(); } });
                    LinearLayout lg_en = v.findViewById(R.id.lg_en); Tools.rippleRoundStroke(lg_en, Color.WHITE, "#BDBDBD", 0, 0, Color.WHITE); lg_en.setOnClickListener(new OnClickListener(){ @Override public void onClick(View p1) { LocaleHelper.setLocale(getApplication(), ll[0]);
								if(ll[0] != flang)
									Toast.makeText(SettingsActivity.this, App.Resources.getString(R.string.restart_app_for_change_language), 500).show(); } });
                    LinearLayout lg_pt = v.findViewById(R.id.lg_pt); Tools.rippleRoundStroke(lg_pt, Color.WHITE, "#BDBDBD", 0, 0, Color.WHITE); lg_pt.setOnClickListener(new OnClickListener(){ @Override public void onClick(View p1) { LocaleHelper.setLocale(getApplication(), ll[2]);
								if(ll[2] != flang)
									Toast.makeText(SettingsActivity.this, App.Resources.getString(R.string.restart_app_for_change_language), 500).show(); } });
                    LinearLayout lg_hi = v.findViewById(R.id.lg_hi); Tools.rippleRoundStroke(lg_hi, Color.WHITE, "#BDBDBD", 0, 0, Color.WHITE); lg_hi.setOnClickListener(new OnClickListener(){ @Override public void onClick(View p1) { LocaleHelper.setLocale(getApplication(), ll[3]);
								if(ll[3] != flang)
									Toast.makeText(SettingsActivity.this, App.Resources.getString(R.string.restart_app_for_change_language), 500).show(); } });
                    LinearLayout lg_ar = v.findViewById(R.id.lg_ar); Tools.rippleRoundStroke(lg_ar, Color.WHITE, "#BDBDBD", 0, 0, Color.WHITE); lg_ar.setOnClickListener(new OnClickListener(){ @Override public void onClick(View p1) { LocaleHelper.setLocale(getApplication(), ll[4]);
								if(ll[4] != flang)
									Toast.makeText(SettingsActivity.this, App.Resources.getString(R.string.restart_app_for_change_language), 500).show(); } });
                    
                    TextView tr = v.findViewById(R.id.tr); tr.setTypeface(new Typeface().createFromAsset(getAssets(),"youtubesansregular.otf"));
                    TextView en = v.findViewById(R.id.en); en.setTypeface(new Typeface().createFromAsset(getAssets(),"youtubesansregular.otf"));
                    TextView pt = v.findViewById(R.id.pt); pt.setTypeface(new Typeface().createFromAsset(getAssets(),"youtubesansregular.otf"));
                    TextView hi = v.findViewById(R.id.hi); hi.setTypeface(new Typeface().createFromAsset(getAssets(),"youtubesansregular.otf"));
                    TextView ar = v.findViewById(R.id.ar); ar.setTypeface(new Typeface().createFromAsset(getAssets(),"youtubesansregular.otf"));
                    backL.setClipToOutline(true);
                    dl.setContentView(v);
                    dl.show();
                }
        });
    }
}
