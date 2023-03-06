package tm.Ezz.Game.Engine;
 
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import java.util.Locale;
import java.util.TimerTask;
import java.util.Timer;
import android.view.View;
import android.widget.LinearLayout;
import android.graphics.Color;
import android.widget.Toast;

public class MainActivity extends Activity {
    
	View img;
	LinearLayout linBack;
	
	SharedPreferences pref;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		img = findViewById(R.id.img);
		linBack = findViewById(R.id.linBack);
		
		img.setAlpha(0);
		
		pref = getSharedPreferences("theme",Activity.MODE_PRIVATE);
		
		if(pref.getString("THEME","").equals(null)){
			pref.edit().putString("THEME","WHITE");
		}

        if (Build.VERSION.SDK_INT >= 23) {
			if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
				||checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
				requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
			} else {
				init();
			} 
		} else {
			init();
		}
    }

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 1000) {
			init();
		}
	}

	public void init(){
		if (!FileUtil.isExistFile(FileUtil.getExternalStorageDir().concat("/.Ezz-WorkFlow")))
			FileUtil.makeDir(FileUtil.getExternalStorageDir().concat("/.Ezz-WorkFlow"));
		if (!FileUtil.isExistFile(FileUtil.getExternalStorageDir().concat("/.Ezz-WorkFlow/Projects")))
			FileUtil.makeDir(FileUtil.getExternalStorageDir().concat("/.Ezz-WorkFlow/Projects"));
		if (!FileUtil.isExistFile(FileUtil.getExternalStorageDir().concat("/.Ezz-WorkFlow/Releases"))) 
			FileUtil.makeDir(FileUtil.getExternalStorageDir().concat("/.Ezz-WorkFlow/Releases"));
		if (!FileUtil.isExistFile(FileUtil.getExternalStorageDir().concat("/.Ezz-WorkFlow/.Downloads"))) 
			FileUtil.makeDir(FileUtil.getExternalStorageDir().concat("/.Ezz-WorkFlow/.Downloads"));
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplication());
		LocaleHelper.setLocale(getApplication(),  preferences.getString("Locale.Helper.Selected.Language", Locale.getDefault().getLanguage()));
		
		TimerTask tt = new TimerTask(){
			@Override
			public void run() {
				runOnUiThread(new Runnable(){
						@Override
						public void run() {
							startActivity(new Intent(MainActivity.this, MenuActivity.class));
							finish();
						}
					});
			}
		};
		
		new Timer().schedule(tt,3000);
		
		TimerTask ts = new TimerTask(){
			@Override
			public void run() {
				runOnUiThread(new Runnable(){
						@Override
						public void run() {
							img.setAlpha(img.getAlpha() + 0.025f);
						}
					});
			}
		};
		
		Timer tm = new Timer();
		tm.schedule(ts,0,100);
		
		if(pref.getString("THEME","").equals("WHITE")){
			linBack.setBackgroundColor(Color.WHITE);
		} else {
			if(pref.getString("THEME","").equals("NIGHT")){
				linBack.setBackgroundColor(Color.parseColor("#263238"));
			}
		}
	}
}
