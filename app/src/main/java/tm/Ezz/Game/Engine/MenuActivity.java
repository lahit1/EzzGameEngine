package tm.Ezz.Game.Engine;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;
import com.Ezz.Game.Engine.math.Vector;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.graphics.drawable.Drawable;
import android.graphics.Typeface;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;
import java.util.HashMap;
import com.google.firebase.database.DatabaseError;

public class MenuActivity extends Activity {
    
	Button b1;
	ImageView dc, yt;
	ImageView sets,account;
	ListView lw1;
	LinearLayout bar,otl;
	ArrayAdapter<String> aAdp;
	ArrayList<String> projects = new ArrayList<String>();
	final String[] NAK = {"*","/","\\","|","<",">","\"",":","?"};
	FirebaseDatabase firebase = FirebaseDatabase.getInstance();
	DatabaseReference userD = firebase.getReference("users");
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
		
        FirebaseApp.initializeApp(MenuActivity.this);
        
		bar = findViewById(R.id.bar);
		bar.setClipToOutline(true);
		bar.setElevation(5);

		otl = findViewById(R.id.othersLink);
		otl.setClipToOutline(true);
		
		b1 = findViewById(R.id.menuNewProject);
		b1.setText(App.Resources.getString(R.string.New_Project));
		b1.setOnClickListener(new OnClickListener(){
			
				Dialog dlg;
				EditText appName, pkgName, baseUniverse;
				Button smbt;
			
				@Override
				public void onClick(View p1) {
					dlg = new Dialog(MenuActivity.this);
					dlg.setContentView(R.layout.dialog_3edittext);
					dlg.setTitle(App.Resources.getString(R.string.New_Project));
					((TextView) dlg.findViewById(R.id.sedTitle)).setText(App.Resources.getString(R.string.New_Project));
					appName = dlg.findViewById(R.id.sedEditText1);
					pkgName = dlg.findViewById(R.id.sedEditText2);
					baseUniverse = dlg.findViewById(R.id.sedEditText3);
					smbt = dlg.findViewById(R.id.sedButton1);
					smbt.setText(App.Resources.getString(R.string.Create));
					smbt.setOnClickListener(new OnClickListener(){
						
							String fileName, universeName;
						
							@Override
							public void onClick(View p1) {
								if(appName.getText().toString() == ""){
									appName.setError(App.Resources.getString(R.string.blank_field));
									return;
								}else if(pkgName.getText().toString() == ""){
									pkgName.setError(App.Resources.getString(R.string.blank_field));
									return;
								}
								fileName = appName.getText().toString();
								universeName = baseUniverse.getText().toString().replace(" ", "");
								for(String s: NAK){
									fileName = fileName.replace(s, "");
									universeName = universeName.replace(s, "");
								}
								if(fileName == ""){
									appName.setError(App.Resources.getString(R.string.illegal_project_name));
									return;
								}
								for(String s: projects){
									if(Uri.parse(s).getLastPathSegment().equalsIgnoreCase(fileName)){
										appName.setError(App.Resources.getString(R.string.illegal_project_name));
										return;
									}
								}
								if(universeName == "") universeName = "BaseUniverse";
								if(isProject(fileName)){
									appName.setError(App.Resources.getString(R.string.project_already_exits));
									return;
								}
								JSONObject jo = new JSONObject();
								try {
									jo.put("p-ver", "1.0");
									jo.put("appName", appName.getText().toString());
									jo.put("packageName", pkgName.getText().toString());
									jo.put("base-universe", universeName);
								}
								catch (JSONException e) {}
								finally{
									try {
										createProject(fileName, jo);
									} catch (JSONException e) {}
								}
								dlg.dismiss();
							}
					});
					appName.setHint(App.Resources.getString(R.string.Project_Name));
					appName.setText(App.Resources.getString(R.string.my_game));
					pkgName.setHint(App.Resources.getString(R.string.package_name));
					pkgName.setText(App.Resources.getString(R.string.simple_package_name));
					baseUniverse.setHint(App.Resources.getString(R.string.base_universe));
					baseUniverse.setText(App.Resources.getString(R.string.simple_universe));
					dlg.show();
				}
		});
			
		dc = findViewById(R.id.menuDiscord);
        rippleRoundStroke(dc, Color.WHITE, "#BDBDBD", 10, 0, Color.WHITE);
		dc.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1) {
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://discord.gg/krtJUxy62D")));
				}
		});
		yt = findViewById(R.id.menuYoutube);
        rippleRoundStroke(yt, Color.WHITE, "#BDBDBD", 10, 0, Color.WHITE);
		yt.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View p1) {
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtube.com/channel/UCpvfXzi_JUTCI3onfYquNng")));
				}
			
		});
		sets = findViewById(R.id.menuSettings);
        rippleRoundStroke(sets, Color.parseColor("#7777ff"), "#BDBDBD", 10, 0, Color.parseColor("#7777ff"));
		sets.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1) {
					startActivity(new Intent(MenuActivity.this, SettingsActivity.class));
				}
		});
		
		
		
        account = findViewById(R.id.menuAccount);
        rippleRoundStroke(account, Color.parseColor("#7777ff"), "#BDBDBD", 10, 0, Color.parseColor("#7777ff"));
        account.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View p1) {
                    Toast.makeText(getApplicationContext(), "Bu Özellik Beta'dan Sonra Kullanılabilir Olacak!", Toast.LENGTH_LONG).show();
                }
        });
        
		lw1 = findViewById(R.id.menuProjects);
		aAdp = new ArrayAdapter<String>(this, 0){
			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				View view = getLayoutInflater().inflate(R.layout.projects,parent, false);
				TextView name,packaget;
                LinearLayout cl = view.findViewById(R.id.cl);
				name = view.findViewById(R.id.name);
				packaget = view.findViewById(R.id.packaget);
				name.setText(Uri.parse(projects.get(position)).getLastPathSegment());
				try {
					JSONObject jno = new JSONObject(FileUtil.readFile(projects.get(position) + "/data/EzzInfo.json"));
					packaget.setText(App.Resources.getString(R.string.package_name) + " " + jno.getString("packageName") + "     " + App.Resources.getString(R.string.versioni) + " " + jno.getString("p-ver"));
				} catch (JSONException e) {
					Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
				}
				ImageView othersS = view.findViewById(R.id.others);
                rippleRoundStroke(othersS, Color.WHITE, "#BDBDBD", 10, 0, Color.WHITE);
                rippleRoundStroke(cl, Color.WHITE, "#BDBDBD", 0, 0, Color.WHITE);
				othersS.setOnClickListener(new View.OnClickListener(){
						@Override
						public void onClick(View p1) {
							View v = getLayoutInflater().inflate(R.layout.projects_settings,null);
							final PopupWindow pw = new PopupWindow(v,300,RelativeLayout.LayoutParams.WRAP_CONTENT, true);
							pw.setHeight(110*3);
							pw.setWidth(110*3);
							LinearLayout lc = v.findViewById(R.id.lc);
							LinearLayout delete = v.findViewById(R.id.projectsDelete);
							LinearLayout pp = v.findViewById(R.id.publish);
                            TextView dt = v.findViewById(R.id.projectsDeleteText); dt.setTypeface(new Typeface().createFromAsset(getAssets(),"youtubesansregular.otf"));
                            TextView pt = v.findViewById(R.id.publishText); pt.setTypeface(new Typeface().createFromAsset(getAssets(),"youtubesansregular.otf"));
                            TextView st = v.findViewById(R.id.settingsPrText); st.setTypeface(new Typeface().createFromAsset(getAssets(),"youtubesansregular.otf"));
							lc.setClipToOutline(true);
							pw.showAsDropDown(p1,-153,0);
							delete.setOnClickListener(new OnClickListener(){
								@Override
								public void onClick(View p1) {
									FileUtil.deleteFile(projects.get(position));
									projects.remove(position);
									aAdp.remove(aAdp.getItem(position));
									aAdp.notifyDataSetChanged();
									pw.dismiss(); 
								}
							});
							
							pp.setOnClickListener(new OnClickListener(){
									@Override
									public void onClick(View p1) {
										publish(projects.get(position).toString());
									}
								});
						}
					});
				return view;
			}
		};
		lw1.setAdapter(aAdp);
		lw1.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
					if(!isProject(projects.get(p3))) return;
					Intent intent = new Intent(MenuActivity.this, EditorActivity.class);
					intent.putExtra("project", projects.get(p3));
					try {
						JSONObject jo = new JSONObject(FileUtil.readFile(projects.get(p3).concat("/data/EzzInfo.json")));
						intent.putExtra("universe", jo.get("base-universe").toString());
					} catch (Exception e) {
						Toast.makeText(MenuActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();;
					}
					finally {
						startActivity(intent);
					}
				}
			});
		ArrayList<String> al = new ArrayList<String>();
		FileUtil.listDir(FileUtil.getExternalStorageDir().concat("/.Ezz-WorkFlow/Projects/"), al);
		for(String fn: al) if(isProject(fn)) {
				projects.add(fn);
				aAdp.add(Uri.parse(fn).getLastPathSegment());
			}
		aAdp.notifyDataSetChanged();
	}
	
	boolean isProject(String path){
		return FileUtil.isExistFile(path.concat("/data/EzzInfo.json"));
	}
	
	void createProject(String fileName, JSONObject data) throws JSONException{
		String pPath = FileUtil.getExternalStorageDir().concat("/.Ezz-WorkFlow/Projects/").concat(fileName);
		FileUtil.makeDir(pPath);
		FileUtil.makeDir(pPath.concat("/Assets/"));
		FileUtil.writeFile(pPath.concat("/Assets/").concat((String) data.get("base-universe")).concat(".universe"), "{\"grav.x\": 0.0, \"grav.y\": -45.0, \"backgroundColor\":" + Color.WHITE + ", \"entities\": [], \"camera\": {\"pos.x\": 0.0, \"pos.y\": 0.0, \"followed\":-1}}");
		FileUtil.makeDir(pPath.concat("/data/"));
		FileUtil.writeFile(pPath.concat("/data/EzzInfo.json"), data.toString());
		projects.add(pPath);
		aAdp.add(fileName);
		aAdp.notifyDataSetChanged();
	}

   	void rippleRoundStroke(final View _view, final int _focus, final String _pressed, final double _round, final double _stroke, final int _strokeclr) {
        android.graphics.drawable.GradientDrawable GG = new android.graphics.drawable.GradientDrawable();
        GG.setColor(_focus);
        GG.setCornerRadius((float)_round);
        GG.setStroke((int) _stroke,
                     _strokeclr);
        android.graphics.drawable.RippleDrawable RE = new android.graphics.drawable.RippleDrawable(new android.content.res.ColorStateList(new int[][]{new int[]{}}, new int[]{ Color.parseColor(_pressed)}), GG, null);
        _view.setBackground(RE);
    }
	
	void publish(String path) {
		try {
			JSONObject jo = new JSONObject(FileUtil.readFile(path.concat("/data/EzzInfo.json")));
			String targetpath = FileUtil.getExternalStorageDir().concat("/.Ezz-WorkFlow/Releases/").concat(jo.getString("appName")).concat(".zip");
			FileUtil.zip(path, targetpath);
			
			
			
			//'targetpath' zip dosyasının yolu, zipi firebase ye yuklersin
		} catch (Exception e) {}
	}
}
