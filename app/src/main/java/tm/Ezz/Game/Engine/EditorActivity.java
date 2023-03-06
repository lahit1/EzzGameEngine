package tm.Ezz.Game.Engine;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import com.Ezz.Game.Engine.Engine;
import com.Ezz.Game.Engine.Universe;
import com.Ezz.Game.Engine.entity.AnimationComponent;
import com.Ezz.Game.Engine.entity.ButtonComponent;
import com.Ezz.Game.Engine.entity.Component;
import com.Ezz.Game.Engine.entity.Entity;
import com.Ezz.Game.Engine.entity.FontComponent;
import com.Ezz.Game.Engine.entity.MusicPlayerComponent;
import com.Ezz.Game.Engine.entity.ProgressBarComponent;
import com.Ezz.Game.Engine.entity.SwitchComponent;
import com.Ezz.Game.Engine.entity.TextureComponent;
import com.Ezz.Game.Engine.file.AssetLoader;
import com.Ezz.Game.Engine.file.Texture;
import com.Ezz.Game.Engine.luascript.LuaScript;
import com.Ezz.Game.Engine.math.Vector;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import android.widget.Adapter;
import android.app.AlertDialog;
import java.net.Socket;
import com.Ezz.Game.Engine.luascript.LNet;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.media.ThumbnailUtils;
import android.util.Size;
import com.Ezz.Game.Engine.Input;
import android.view.View.OnLongClickListener;
import com.Ezz.Game.Engine.entity.JoystickComponent;

public class EditorActivity extends Activity {
	
	String projectPath;
	String currentUniverse;
	
	String universeData;

	boolean isCanSave = true;

	Universe universe;
	Entity selectedEntity;
	Component selectedComponent;
	LinearLayout leftDrawer, rightDrawer, bottomDrawer, BE, CEADrawer, CEA;
	ImageButton addEntity, addComponent, ecAnmAddFrame;
	ListView lwEntities, lwComponents;
	ArrayAdapter<Entity> adEnt;
	ArrayAdapter<Component> adCom;
	ImageView
	entitiesOpen, settingsOpen, cameraOpen,
	rightDrawerOpen, CEAClose, save, envPP,
	envS, envFS, toolFE, toolFC, ecTextureSlct;

	TextView bdFilesOpen, bdOutputOpen, profiler;

	EditText ID;
	CheckBox UIb;

    LinearLayout
	ldentities, ldsettings, ldcamsets,
    bdFiles, bdOutput;

    EditText setGX, setGY, setCX, setCY;

	LinearLayout
		ecTransform, ecTexture,
		ecAnimation,
		ecButton, ecSwitch,
		ecProggressbar,
		ecFont, ecLuaScript,
		ecMusicPlayer, ecJoystick;

	Switch switchIsChecked, ecMusicIsPlaying, ecMusicIsLooping;

	EditText ecTrPosX, ecTrPosY,
	ecTrSizeX, ecTrSizeY,
	ecTrRotation, ecPbMax,
    ecPbCur, ecFnTx,
    ecFnSz, ecAnmChangeTime;

	TextView ecTexturePath, ecLuaScriptPath, ecMusicPath;

    View ldBcp, ecBtnC, ecBtnTC, ecSwBC, ecSwSBC, ecSwHC, ecPbBC, ecPbPC,  ecFnCp, ecJSBC, ecJSKC;

    int cld, cbd;

	float camX, camY;
	
	ArrayList<String> files = new ArrayList<String>();
	
	Spinner camFes;
	
	ListView ecAnmFrames;
	
	public AssetLoader al;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Engine.init(this);
		setContentView(R.layout.activity_editor);
		((TextView)findViewById(R.id.string_addComponent)).setText(App.Resources.getString(R.string.add_component));
		projectPath = getIntent().getExtras().getString("project");
		currentUniverse = getIntent().getExtras().getString("universe");
		al = Engine.Assets = new AssetLoader(this) {
			@Override
			public String readString(String filePath) {
				return FileUtil.readFile(projectPath.concat("/Assets/").concat(filePath));
			}
			
			@Override
			public Texture loadTexture(String filePath) {
				Texture ret = new Texture();
				Bitmap bitmap = BitmapFactory.decodeFile(projectPath.concat("/Assets/").concat(filePath));
				ret.setDrawable(new BitmapDrawable(Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth() * quality), (int)(bitmap.getHeight() * quality), false)));
				ret.setDrawablePath(filePath);
				return ret;
			}
			
			@Override
			public void setMediaDataSource(MediaPlayer mp, String file) {
				try {
					mp.setDataSource(projectPath.concat("/Assets/").concat(file));
				} catch (IllegalStateException e) {} catch (SecurityException e) {} catch (IllegalArgumentException e) {} catch (IOException e) {}
			}
		};
		final Paint red = new Paint(),
			blue = new Paint(),
			gray = new Paint();

		red.setColor(Color.RED);
		blue.setColor(Color.BLUE);
		gray.setColor(Color.parseColor("#333333"));
		final View pv = Engine.newProjectionView(this);
		Engine.luaAssets = CoerceJavaToLua.coerce(Engine.Assets);
		Engine.startUniverse(universe = new Universe(currentUniverse.concat(".universe"), this) {
			float time;
			
			float ltx, lty;
			
				@Override
				protected void draw(Canvas canvas) {
					time += getDeltatime();
					if(time > 0.5f) {
						profiler.setText(String.format("fps: %d\nms: %d", (int)(1 / getDeltatime()), (int)(getDeltatime() * 1000)));
						time -= 0.5f;
					}
					super.draw(canvas);
					
					
					if(isCanSave) {
						for(int s=0; s < (int)(11 / pv.getScaleX()); s++) {
							float x = s * 250f - Camera.getPosition().x % 250f + Camera.getSize().x / 2f - 1250f;
							canvas.drawLine(x, 0, x, Camera.getSize().y, gray);
						}
						for(int c=0; c < (int)(6 / pv.getScaleY()); c++) {
							float y = c * 250f - Camera.getPosition().y % 250f + Camera.getSize().y / 2f - 750f;
							canvas.drawLine(0, y, Camera.getSize().x, y, gray);
						}
					}
					if(isCanSave && selectedEntity != null) {
						float x = selectedEntity.calculateRenderX() + selectedEntity.size.x / 2f,
								y = selectedEntity.calculateRenderY() + selectedEntity.size.y / 2f;
						canvas.drawRect(x, y - 10, x + 130, y + 10, red);
						canvas.drawRect(x - 10, y, x + 10, y - 130, blue);
					}
				}

				@Override
				public void onScreenLoaded() {
					profiler.setX(40 * App.density);
					rightDrawer.setX(Camera.getSize().x);
					CEADrawer.setX(Camera.getSize().x);
					bottomDrawer.setY(Camera.getSize().y - 25 * App.density);
					profiler.setY(bottomDrawer.getY() - 50 * App.density);
				}
				@Override
				public Bitmap captureBitmap() {
					Bitmap bm = Bitmap.createBitmap((int)Camera.getSize().x, (int)Camera.getSize().y, Bitmap.Config.ARGB_4444);
					super.draw(new Canvas(bm));
					return bm;
				}
		});
        universe.setResuming(false);
		camX = universe.Camera.getPosition().x;
		camY = universe.Camera.getPosition().y;
		
		final OnTouchListener otl = (View.OnTouchListener) pv;
		pv.setOnTouchListener(new OnTouchListener() {

				float ox, oy;

				@Override
				public boolean onTouch(View v, MotionEvent me) {
					if (isCanSave)
						switch (me.getAction()) {
							case MotionEvent.ACTION_DOWN:
								ox = me.getX();
								oy = me.getY();
								break;
							case MotionEvent.ACTION_UP:
								break;
							default:
								float x = me.getX();
								float y = me.getY();
								Vector pos = universe.Camera.getPosition();
								pos.set(pos.x - x + ox, pos.y - y + oy);
								ox = x;
								oy = y;
						}
					return otl.onTouch(v, me);
				}
			});
		RelativeLayout editorspace = findViewById(R.id.editorSpace);
		editorspace.addView(pv);
		leftDrawer = findViewById(R.id.editorLeftDrawer);
		rightDrawer = findViewById(R.id.editorRightDrawer);
		bottomDrawer = findViewById(R.id.editorBottomDrawer);
		CEADrawer = findViewById(R.id.editorCEADrawer);
		BE = findViewById(R.id.editorBE);
		CEA = findViewById(R.id.editorCEA);
		addComponent = findViewById(R.id.editorAddComponent);
		lwComponents = findViewById(R.id.editorComponents);
		entitiesOpen = findViewById(R.id.editorEntitiesOpen);
        settingsOpen = findViewById(R.id.editorSettingsOpen);
		cameraOpen = findViewById(R.id.editorCamSetsOpen);
		rightDrawerOpen = findViewById(R.id.editorRightDrawerOpen);
		CEAClose = findViewById(R.id.editorCEAClose);
		bdFilesOpen = findViewById(R.id.editorFilesOpen);
		bdOutputOpen = findViewById(R.id.editorOutputOpen);
		profiler = findViewById(R.id.editorFPS);
		envPP = findViewById(R.id.editorEnvPP);
		envS = findViewById(R.id.editorEnvS);
		envFS = findViewById(R.id.editorEnvFS);
		toolFE = findViewById(R.id.editorToolFocusEntity);
		toolFC = findViewById(R.id.editorToolFocusCenter);
		save = findViewById(R.id.editorSave);

		findViewById(R.id.editorBack).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View p1) {
					finish();
				}
		});

		ID = findViewById(R.id.editorID);
		UIb = findViewById(R.id.editorUIb);
        {
            ldentities = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.ld_entities, leftDrawer, false);
            lwEntities = ldentities.findViewById(R.id.editorEntities);
            addEntity = ldentities.findViewById(R.id.editorAddEntity);
			((TextView)ldentities.findViewById(R.id.string_addEntity))
				.setText(App.Resources.getString(R.string.add_entity));
        }
		{
            ldsettings = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.ld_settings, leftDrawer, false);
            ldBcp = ldsettings.findViewById(R.id.lds_bc);
			((TextView)ldsettings.findViewById(R.id.string_setsGravity))
				.setText(App.Resources.getString(R.string.gravity));
			((TextView)ldsettings.findViewById(R.id.string_setsBColor))
				.setText(App.Resources.getString(R.string.background_color));

            ldBcp.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View p1) {
                        ColorPickerDialog cpd = new ColorPickerDialog(EditorActivity.this);
                        cpd.setOnColorPickedListener(new ColorPickerDialog.onColorPickedListener() {
                                @Override
                                public void onColorPicked(String hex, double a, double r, double g, double b) {
                                    universe.backgroundColor = Color.argb((int)a, (int)r, (int)g, (int)b);
                                    ldBcp.setBackgroundColor(universe.backgroundColor);
                                }
                            });
                        cpd.show("#" + Integer.toHexString(universe.backgroundColor).toUpperCase());
                    }
                });
			ldBcp.setBackgroundColor(universe.backgroundColor);
            setGX = ldsettings.findViewById(R.id.lds_gX);
            setGY = ldsettings.findViewById(R.id.lds_gY);
			setGX.setOnEditorActionListener(new OnEditorActionListener() {

                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                        if ((actionId & EditorInfo.IME_MASK_ACTION) == EditorInfo.IME_ACTION_DONE) {
                            universe.getGravity().x = Float.parseFloat(v.getText().toString());
                            int slc = setGX.getSelectionStart();
                            v.setText(Float.toString(universe.getGravity().x));
                            setGX.setSelection(slc);
                            return true;
                        }
                        return false;
                    }
				});
			setGX.setText(Float.toString(universe.getGravity().x));
			setGY.setOnEditorActionListener(new OnEditorActionListener() {

                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                        if ((actionId & EditorInfo.IME_MASK_ACTION) == EditorInfo.IME_ACTION_DONE) {
                            universe.getGravity().y = Float.parseFloat(v.getText().toString());
                            int slc = setGY.getSelectionStart();
                            v.setText(Float.toString(universe.getGravity().y));
                            setGY.setSelection(slc);
                            return true;
                        }
                        return false;
                    }
				});
			setGY.setText(Float.toString(universe.getGravity().y));
        }
		{
			ldcamsets = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.ld_camera, leftDrawer, false);
			((TextView)ldcamsets.findViewById(R.id.string_camSPos))
				.setText(App.Resources.getString(R.string.position));
			((TextView)ldcamsets.findViewById(R.id.string_camFEnt))
				.setText(App.Resources.getString(R.string.follow_entity));
			setCX = ldcamsets.findViewById(R.id.ldc_pX);
			setCY = ldcamsets.findViewById(R.id.ldc_pY);
			setCX.setOnEditorActionListener(new OnEditorActionListener() {

                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                        if ((actionId & EditorInfo.IME_MASK_ACTION) == EditorInfo.IME_ACTION_DONE) {
                            v.setText(Float.toString(camX = Float.parseFloat(v.getText().toString())));
                            return true;
						}
                        return false;
                    }
				});
			setCX.setText(Float.toString(camX));
            setCY.setOnEditorActionListener(new OnEditorActionListener() {

                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
						if ((actionId & EditorInfo.IME_MASK_ACTION) == EditorInfo.IME_ACTION_DONE) {
                            v.setText(Float.toString(camY = Float.parseFloat(v.getText().toString())));
							return true;
						}
                        return false;
                    }
				});
			setCY.setText(Float.toString(camY));
			camFes = ldcamsets.findViewById(R.id.ldc_fnent);
			camFes.setAdapter(new SpinnerAdapter() {

					@Override
					public void registerDataSetObserver(DataSetObserver p1) {
					}

					@Override
					public void unregisterDataSetObserver(DataSetObserver p1) {
					}

					@Override
					public int getCount() {
						return universe.getEntities().size() + 1;
					}

					@Override
					public Object getItem(int p1) {
						return null;
					}

					@Override
					public long getItemId(int p1) {
						return 0;
					}

					@Override
					public boolean hasStableIds() {
						return false;
					}

					@Override
					public View getView(int p, View cv, ViewGroup vg) {
						if (cv == null) cv = LayoutInflater.from(EditorActivity.this).inflate(android.R.layout.simple_list_item_1, vg, false);
						if (p == 0) {
							TextView tv = (TextView) cv;
							tv.setText(App.Resources.getString(R.string.none_of_them));
						} else {
							TextView tv = (TextView)cv;
							tv.setText(universe.getEntities().get(p - 1).ID);
						}
						return cv;
					}

					@Override
					public int getItemViewType(int p1) {
						return 0;
					}

					@Override
					public int getViewTypeCount() {
						return 1;
					}

					@Override
					public boolean isEmpty() {
						return false;
					}

					@Override
					public CharSequence[] getAutofillOptions() {
						return null;
					}

					@Override
					public View getDropDownView(int p, View cv, ViewGroup vg) {
						if (cv == null) cv = LayoutInflater.from(EditorActivity.this).inflate(android.R.layout.simple_list_item_1, vg, false);
						if (p == 0) {
							TextView tv = (TextView) cv;
							tv.setText(App.Resources.getString(R.string.none_of_them));
						} else {
							TextView tv = (TextView)cv;
							tv.setText(universe.getEntities().get(p - 1).ID);
						}
						return cv;
					}
				});
			camFes.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> p1, View p2, int p, long p4) {
						if (p == 0)
							universe.Camera.follow(null);
						else
							universe.Camera.follow(universe.getEntities().get(p - 1));
					}

					@Override
					public void onNothingSelected(AdapterView<?> p1) {
					}
				});
			Entity e = universe.Camera.getFollowed();
			if (e == null)
				camFes.setSelection(0);
			else
				camFes.setSelection(universe.getEntities().indexOf(e) + 1);
		}
		{
			bdFiles = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.bd_files, bottomDrawer, false);
			final GridView gv = bdFiles.findViewById(R.id.bd_fList);
			FileUtil.listDir(projectPath.concat("/Assets/"), files);
			final String af = App.Resources.getString(R.string.add_file);
            final BaseAdapter adp = new BaseAdapter() {
				@Override
				public int getCount() {
					return files.size() + 1;
				}

				@Override
				public Object getItem(int index) {
					return files.get(index);
				}

				@Override
				public long getItemId(int p1) {
					return 0;
				}
				@Override
				public View getView(int index, View convertview, ViewGroup parent) {
					if (convertview == null)
						convertview = LayoutInflater.from(EditorActivity.this).inflate(R.layout.ListFileItem, parent, false);
					TextView ft = convertview.findViewById(R.id.LFIName);
					ImageView img = convertview.findViewById(R.id.lfiImg);
                    if (index == files.size()) {
                        ft.setText(af);
                        img.setImageDrawable(App.Resources.getDrawable(R.drawable.ic_add));
					} else {
                        String fileName = Uri.parse(files.get(index)).getLastPathSegment();
                        ft.setText(fileName);
                        if (fileName.endsWith(".universe"))
                            img.setImageDrawable(App.Resources.getDrawable(R.drawable.app_icon));
                        else if (fileName.endsWith(".lua"))
                            img.setImageDrawable(App.Resources.getDrawable(R.drawable.ic_script));
						else {
							String[] imgExs = {".png", ".jpg", ".jpeg"};
							for(String ex: imgExs)
								if (fileName.endsWith(ex)) { 
									Bitmap bm = BitmapFactory.decodeFile(files.get(index));
									if(bm.getWidth() > 256 || bm.getHeight() > 256)
										bm = Bitmap.createScaledBitmap(bm, 256, (int)(256f / bm.getWidth() * bm.getHeight()), false);
									img.setImageBitmap(bm);
								}
						}
					}
					return convertview;
				}
			};
			gv.setAdapter(adp);
			gv.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> p1, View p2, int index, long p4) {
						if (index == files.size()) {
							final Dialog dlg = new Dialog(EditorActivity.this);
							dlg.setContentView(R.layout.dialog_new_file);
							dlg.setTitle(R.string.add_file);
							Button smb = dlg.findViewById(R.id.nfSmb);
							final Spinner spn = dlg.findViewById(R.id.nfSpn1);
							final EditText et = dlg.findViewById(R.id.nfEdt1);
							et.setHint(App.Resources.getString(R.string.file_name));
							spn.setAdapter(new ArrayAdapter<String>(EditorActivity.this, android.R.layout.simple_list_item_1, new String[]{"Universe", "LuaScript"}));
							smb.setText(App.Resources.getString(R.string.add));
							smb.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View p1) {
										if (et.getText().toString().isEmpty()) {
											et.setError(App.Resources.getString(R.string.blank_field));
										} else if (et.getText().toString().contains(" ")) {
											et.setError(App.Resources.getString(R.string.illegal_project_name));
										} else {
											String du=null;
											String data=null;
											switch (spn.getSelectedItemPosition()) {
												case 0:
													du = ".universe";
													data = "{\"grav.x\": 0.0, \"grav.y\": -45.0, \"backgroundColor\":" + Color.WHITE + ", \"entities\": [], \"camera\": {\"pos.x\": 0.0, \"pos.y\": 0.0, \"followed\":-1}}";
													break;
												case 1:
													du = ".lua";
													data = "--require 'ezz'\nrequire('ezz.log', 'ezz.vector')\n\nfunction create()\n  --log.m 'Hello World!'\n  log.m('Hello', 'World!')\n  t = Vector.new(500, 0)\nend\n\nfunction update(deltatime)\n  myEntity:rotate(360 * deltatime)\n  myEntity:getPosition():lerp(t, deltatime)\nend";
													break;
											}
											String fpath = projectPath.concat("/Assets/").concat(et.getText().toString()).concat(du);
											if (FileUtil.isExistFile(fpath)) {
												et.setError(App.Resources.getString(R.string.file_already_exits));
											} else {
												FileUtil.writeFile(fpath, data);
                                                files.clear();
                                                FileUtil.listDir(projectPath.concat("/Assets/"), files);
												adp.notifyDataSetChanged();
                                                dlg.dismiss();
											}
										}
									}
								});
							dlg.show();
						} else {
                            String name = Uri.parse(files.get(index)).getLastPathSegment();
                            if (name.contains(".universe")) {
								if (!isCanSave) {
									Toast.makeText(EditorActivity.this, R.string.sgbsu, 500).show();
									return;
								}
                                currentUniverse = name.split("\\.")[0];
                                isCanSave = true;
                                universe.setResuming(false);
                                envPP.setImageDrawable(App.Resources.getDrawable(R.drawable.ic_play));
                                rightDrawer.setX(universe.Camera.getSize().x);
                                adEnt.clear();
                                universe.initializeUniverse(files.get(index).substring(projectPath.concat("/Assets/").length()), EditorActivity.this);
                                for (Entity e: universe.getEntities())
                                    adEnt.add(e);
                            } else if (name.contains(".lua")) {
                                startActivity(new Intent(EditorActivity.this, ScriptEditorActivity.class).putExtra("fpath", files.get(index)));
                            }
                        }
					}
				});
            gv.setOnItemLongClickListener(new OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> av, View v, final int p, long p4) {
                        PopupMenu pm = new PopupMenu(EditorActivity.this, v);
						pm.inflate(R.menu.filesoptions);
						pm.setOnMenuItemClickListener(new OnMenuItemClickListener() {

								@Override
								public boolean onMenuItemClick(MenuItem item) {
									switch (item.getItemId()) {
										case R.id.optFilesDelete:
											FileUtil.deleteFile(files.get(p));
											break;
									}
									return false;
								}

							});
						pm.show();
						return true;
                    }
                });
		}
		{
			bdOutput = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.bd_output, bottomDrawer, false);
			ListView lw = bdOutput.findViewById(R.id.bd_oList);
			final ArrayList<String> logs = new ArrayList<String>();
			final BaseAdapter aAdapter = new BaseAdapter() {

				@Override
				public int getCount() {
					return logs.size();
				}

				@Override
				public Object getItem(int p1) {
					return null;
				}

				@Override
				public long getItemId(int p1) {
					return 0;
				}

				@Override
				public View getView(int p, View cv, ViewGroup p3) {
					if(cv == null)
						cv = new TextView(EditorActivity.this);
					cv.setClickable(false);
					TextView tv = (TextView) cv;
					tv.setSingleLine();
					tv.setTextSize(5 * App.density);
					String log = logs.get(p);
					tv.setText(String.format("%d: %s", p + 1 , log.length() > 60 ? log.substring(0, 60).concat("...") : log));
					return cv;
				}
			};
			lw.setAdapter(aAdapter);
			final AlertDialog mdlg = new AlertDialog.Builder(EditorActivity.this).create();
			
			
			lw.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> p1, View p2, int p, long p4) {
						String log = logs.get(p);
						if(log.startsWith("(Error) ")) {
							mdlg.setTitle("Error");
							mdlg.setMessage(log.substring(8));
						} else if(log.startsWith("(Warn ) ")) {
							mdlg.setTitle("Warn");
							mdlg.setMessage(log.substring(8));
						} else {
							mdlg.setTitle("Message");
							mdlg.setMessage(log);
						}
						mdlg.show();
					}
				
			});
			Engine.logListener = new Engine.LogListener() {
				@Override
				public void message(String msg) {
					logs.add(msg);
					aAdapter.notifyDataSetChanged();
				}
				
				@Override
				public void warn(String error) {
					logs.add("(Warn ) ".concat(error));
					aAdapter.notifyDataSetChanged();
				}
				
				@Override
				public void error(String error) {
					logs.add("(Error) ".concat(error));
					Toast.makeText(EditorActivity.this, "Error: ".concat(error.toString()), Toast.LENGTH_LONG).show();
					envPP.performClick();
					aAdapter.notifyDataSetChanged();
				}
			};
		}
		{
			ecTexture = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.component_texture, CEA, false);
			ecTextureSlct = ecTexture.findViewById(R.id.textureSelect);
			ecTexturePath = ecTexture.findViewById(R.id.textureTPath);
			
			((TextView)ecTexture.findViewById(R.id.string_texPath)).setText(App.Resources.getString(R.string.path));
			OnClickListener onclick = new OnClickListener() {
					@Override
					public void onClick(View p1) {
						final GridViewDialog dlg = new GridViewDialog(EditorActivity.this);
						dlg.setTitle("Select Texture");
						dlg.setList(files);
						dlg.getGridView().setOnItemClickListener(new OnItemClickListener() {
								@Override
								public void onItemClick(AdapterView<?> p1, View p2, int p, long p4) {
									try{
										TextureComponent tc = (TextureComponent) selectedComponent;
										tc.setTexture(Engine.Assets.loadTexture(files.get(p).substring(projectPath.concat("/Assets/").length())));
										ecTextureSlct.setImageBitmap(((BitmapDrawable)tc.getTexture().getDrawable()).getBitmap());
									}catch(Exception e) {}
									dlg.dismiss();
								}
							});
						dlg.show();
					}
			};
			ecTextureSlct.setOnClickListener(onclick);
			ecTexturePath.setOnClickListener(onclick);
		}
		{
			ecAnimation = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.component_animation, CEA, false);
			
			((TextView)ecAnimation.findViewById(R.id.string_animSpeed)).setText(App.Resources.getString(R.string.speed));
			((TextView)ecAnimation.findViewById(R.id.string_animAF)).setText(App.Resources.getString(R.string.add_frame));
			ecAnmAddFrame = ecAnimation.findViewById(R.id.animationAddFrame);
			ecAnmFrames = ecAnimation.findViewById(R.id.animationFrames);
			ecAnmChangeTime = ecAnimation.findViewById(R.id.animationChangeRate);
			ecAnmAddFrame.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View p1) {
						final GridViewDialog dlg = new GridViewDialog(EditorActivity.this);
						dlg.setTitle("Select Texture");
						dlg.setList(files);
						dlg.getGridView().setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> p1, View p2, int p, long p4) {
									Texture tex = Engine.Assets.loadTexture(files.get(p).substring(projectPath.concat("/Assets/").length()));
									((AnimationComponent)selectedComponent).getFrames().add(tex);
									ArrayAdapter<Texture> adp = (ArrayAdapter<Texture>) ecAnmFrames.getAdapter();
									adp.add(tex);
									adp.notifyDataSetChanged();
									dlg.dismiss();
								}

						});
						dlg.show();
					}
			});
			ecAnmFrames.setAdapter(new ArrayAdapter<Texture>(this, 0) {
					@Override
					public View getView(int p, View cv, ViewGroup vg) {
						if(cv == null)
							cv = LayoutInflater.from(EditorActivity.this).inflate(R.layout.ListFrame, vg, false);
						Texture t = getItem(p);
						Drawable dr = t.getDrawable();
						if(dr == null)
							dr = Tools.transparentDrawable;
						String path = String.format("%d: %s", p, t.getDrawablePath());
						if(path == null)
							path = "";
						((TextView)cv.findViewById(R.id.lfPath)).setText(path);
						return cv;
					}
			});
			ecAnmFrames.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> b, View lv, int p, long p4) {
						final GridViewDialog dlg = new GridViewDialog(EditorActivity.this);
						dlg.setTitle("Select Texture");
						dlg.setList(files);
						dlg.getGridView().setOnItemClickListener(new OnItemClickListener() {
								@Override
								public void onItemClick(AdapterView<?> p1, View p2, int p, long p4) {
									ArrayAdapter<Texture> adp = (ArrayAdapter<Texture>) ecAnmFrames.getAdapter();
									Texture nTex = Engine.Assets.loadTexture(files.get(p).substring(projectPath.concat("/Assets/").length()));
									adp.getItem(p).set(nTex);
									Toast.makeText(EditorActivity.this, Integer.toString(p), 500).show();
									dlg.dismiss();
								}
							});
						dlg.show();
					}
				});
			ecAnmFrames.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> p1, View p2, int p, long p4) {
						((AnimationComponent)selectedComponent).getFrames().remove(p);
						ArrayAdapter<Texture> adp = (ArrayAdapter<Texture>)ecAnmFrames.getAdapter();
						adp.remove(adp.getItem(p));
						return false;
					}
				
			});
			ecAnmChangeTime.setOnEditorActionListener(new OnEditorActionListener() {

                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                        if ((actionId & EditorInfo.IME_MASK_ACTION) == EditorInfo.IME_ACTION_DONE) {
                            AnimationComponent ac = (AnimationComponent)selectedComponent;
							ac.setChangeTime(Float.parseFloat(v.getText().toString()));
                            int slc = ecAnmChangeTime.getSelectionStart();
                            v.setText(Float.toString(ac.getChangeTime()));
                            ecAnmChangeTime.setSelection(slc);
                            return true;
                        }
                        return false;
                    }
				});
		}
        {
            ecButton = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.component_button, CEA, false);
            ecBtnC = ecButton.findViewById(R.id.buttonColor);
            ecBtnTC = ecButton.findViewById(R.id.buttonTouchColor);
			((TextView)ecButton.findViewById(R.id.string_butColor))
				.setText(App.Resources.getString(R.string.color));
			((TextView)ecButton.findViewById(R.id.string_butTouchColor))
				.setText(App.Resources.getString(R.string.touch_color));
            ecBtnC.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View p1) {
                        ColorPickerDialog cpd = new ColorPickerDialog(EditorActivity.this);
                        cpd.setOnColorPickedListener(new ColorPickerDialog.onColorPickedListener() {
                                @Override
                                public void onColorPicked(String hex, double a, double r, double g, double b) {
                                    selectedComponent.getPaint().setARGB((int)a, (int)r, (int)g, (int)b);
                                    ecBtnC.setBackgroundColor(selectedComponent.getPaint().getColor());
                                }
                            });
                        cpd.show("#" + Integer.toHexString(selectedComponent.getPaint().getColor()).toUpperCase());
                    }
                });
            ecBtnTC.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View p1) {
                        ColorPickerDialog cpd = new ColorPickerDialog(EditorActivity.this);
                        cpd.setOnColorPickedListener(new ColorPickerDialog.onColorPickedListener() {

                                @Override
                                public void onColorPicked(String hex, double a, double r, double g, double b) {
                                    ((ButtonComponent)selectedComponent).getTouchPaint().setARGB((int)a, (int)r, (int)g, (int)b);
                                    ecBtnTC.setBackgroundColor(((ButtonComponent)selectedComponent).getTouchPaint().getColor());
                                }
                            });
                        cpd.show("#" + Integer.toHexString(((ButtonComponent)selectedComponent).getTouchPaint().getColor()).toUpperCase());
                    }
                });
        }
		{
			ecSwitch = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.component_switch, CEA, false);
			switchIsChecked = ecSwitch.findViewById(R.id.switchIsChecked);
			switchIsChecked.setText(App.Resources.getString(R.string.switch_is_checked));
            ecSwBC = ecSwitch.findViewById(R.id.switchBackgroundColor);
            ecSwSBC = ecSwitch.findViewById(R.id.switchSelectedBackgroundColor);
            ecSwHC = ecSwitch.findViewById(R.id.switchHandleColor);
			
			((TextView)ecSwitch.findViewById(R.id.string_switchBColor))
				.setText(App.Resources.getString(R.string.background_color));
			((TextView)ecSwitch.findViewById(R.id.string_switchSBColor))
				.setText(App.Resources.getString(R.string.selected_background_color));
			((TextView)ecSwitch.findViewById(R.id.string_switchHandleColor))
				.setText(App.Resources.getString(R.string.handle_color));

			switchIsChecked.setOnCheckedChangeListener(new OnCheckedChangeListener(){
					@Override
					public void onCheckedChanged(CompoundButton p1, boolean p2) {
						((SwitchComponent)selectedComponent).setChecked(p2);
					}
				});
            ecSwBC.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View p1) {
                        ColorPickerDialog cpd = new ColorPickerDialog(EditorActivity.this);
                        cpd.setOnColorPickedListener(new ColorPickerDialog.onColorPickedListener() {
                                @Override
                                public void onColorPicked(String hex, double a, double r, double g, double b) {
                                    selectedComponent.getPaint().setARGB((int)a, (int)r, (int)g, (int)b);
                                    ecSwBC.setBackgroundColor(selectedComponent.getPaint().getColor());
                                }
                            });
                        cpd.show("#" + Integer.toHexString(selectedComponent.getPaint().getColor()).toUpperCase());
                    }
                });
            ecSwSBC.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View p1) {
                        ColorPickerDialog cpd = new ColorPickerDialog(EditorActivity.this);
                        cpd.setOnColorPickedListener(new ColorPickerDialog.onColorPickedListener() {

                                @Override
                                public void onColorPicked(String hex, double a, double r, double g, double b) {
                                    ((SwitchComponent)selectedComponent).getSelectedPaint().setARGB((int)a, (int)r, (int)g, (int)b);
                                    ecSwSBC.setBackgroundColor(((SwitchComponent)selectedComponent).getSelectedPaint().getColor());
                                }
                            });
                        cpd.show("#" + Integer.toHexString(((SwitchComponent)selectedComponent).getSelectedPaint().getColor()).toUpperCase());
                    }
                });
            ecSwHC.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View p1) {
                        ColorPickerDialog cpd = new ColorPickerDialog(EditorActivity.this);
                        cpd.setOnColorPickedListener(new ColorPickerDialog.onColorPickedListener() {

                                @Override
                                public void onColorPicked(String hex, double a, double r, double g, double b) {
                                    ((SwitchComponent)selectedComponent).getButtonPaint().setARGB((int)a, (int)r, (int)g, (int)b);
                                    ecSwHC.setBackgroundColor(((SwitchComponent)selectedComponent).getButtonPaint().getColor());
                                }
                            });
                        cpd.show("#" + Integer.toHexString(((SwitchComponent)selectedComponent).getButtonPaint().getColor()).toUpperCase());
                    }
                });
		}
        {
            ecProggressbar = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.component_proggressbar, CEA, false);
            ecPbMax = ecProggressbar.findViewById(R.id.proggressbarMax);
            ecPbCur = ecProggressbar.findViewById(R.id.proggressbarCurr);
            ecPbBC = ecProggressbar.findViewById(R.id.proggressbarBC);
            ecPbPC = ecProggressbar.findViewById(R.id.proggressbarC);

			((TextView)ecProggressbar.findViewById(R.id.string_proggress))
				.setText(App.Resources.getString(R.string.proggress));
			((TextView)ecProggressbar.findViewById(R.id.string_proggressColor))
				.setText(App.Resources.getString(R.string.proggress_color));
			((TextView)ecProggressbar.findViewById(R.id.string_pbBColor))
				.setText(App.Resources.getString(R.string.background_color));

            ecPbMax.setOnEditorActionListener(new OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if ((actionId & EditorInfo.IME_MASK_ACTION) == EditorInfo.IME_ACTION_DONE) {
                            ((ProgressBarComponent)selectedComponent).setMax(Integer.parseInt(v.getText().toString()));
                            return true;
                        }
                        return false;
                    }
                });
            ecPbCur.setOnEditorActionListener(new OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if ((actionId & EditorInfo.IME_MASK_ACTION) == EditorInfo.IME_ACTION_DONE) {
                            ((ProgressBarComponent)selectedComponent).setProgress(Integer.parseInt(v.getText().toString()));
                            return true;
                        }
                        return false;
                    }
                });
            ecPbBC.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View p1) {
                        ColorPickerDialog cpd = new ColorPickerDialog(EditorActivity.this);
                        cpd.setOnColorPickedListener(new ColorPickerDialog.onColorPickedListener() {

                                @Override
                                public void onColorPicked(String hex, double a, double r, double g, double b) {
                                    selectedComponent.getPaint().setARGB((int)a, (int)r, (int)g, (int)b);
                                    ecPbBC.setBackgroundColor(selectedComponent.getPaint().getColor());
                                }
                            });
                        cpd.show("#" + Integer.toHexString(selectedComponent.getPaint().getColor()).toUpperCase());
                    }
                });
            ecPbPC.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View p1) {
                        ColorPickerDialog cpd = new ColorPickerDialog(EditorActivity.this);
                        cpd.setOnColorPickedListener(new ColorPickerDialog.onColorPickedListener() {

                                @Override
                                public void onColorPicked(String hex, double a, double r, double g, double b) {
                                    ((ProgressBarComponent)selectedComponent).getProgressPaint().setARGB((int)a, (int)r, (int)g, (int)b);
                                    ecPbPC.setBackgroundColor(((ProgressBarComponent)selectedComponent).getProgressPaint().getColor());
                                }
                            });
                        cpd.show("#" + Integer.toHexString(((ProgressBarComponent)selectedComponent).getProgressPaint().getColor()).toUpperCase());
                    }
                });
        }
		{
			ecTransform = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.component_transform, CEA, false);
			ecTrPosX = ecTransform.findViewById(R.id.transform_posX);
			ecTrPosY = ecTransform.findViewById(R.id.transform_posY);
			ecTrSizeX = ecTransform.findViewById(R.id.transform_sizeX);
            ecTrSizeY = ecTransform.findViewById(R.id.transform_sizeY);
			ecTrRotation = ecTransform.findViewById(R.id.transform_rotation);
			
			((TextView)ecTransform.findViewById(R.id.string_trPosition))
				.setText(App.Resources.getString(R.string.position));
			((TextView)ecTransform.findViewById(R.id.string_trSize))
				.setText(App.Resources.getString(R.string.size));
			((TextView)ecTransform.findViewById(R.id.string_trRotation))
				.setText(App.Resources.getString(R.string.rotation));

			ecTrPosX.setOnEditorActionListener(new OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

						if ((actionId & EditorInfo.IME_MASK_ACTION) == EditorInfo.IME_ACTION_DONE) {
							selectedEntity.pos.x = Float.parseFloat(v.getText().toString());
							int slc = ecTrPosX.getSelectionStart();
							v.setText(Float.toString(selectedEntity.pos.x));
							ecTrPosX.setSelection(slc);
							return true;
						}
						return false;
					}
				});
			ecTrPosY.setOnEditorActionListener(new OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

						if ((actionId & EditorInfo.IME_MASK_ACTION) == EditorInfo.IME_ACTION_DONE) {
							selectedEntity.pos.y = Float.parseFloat(v.getText().toString());
							int slc = ecTrPosY.getSelectionStart();
							v.setText(Float.toString(selectedEntity.pos.y));
							ecTrPosY.setSelection(slc);
							return true;
						}
						return false;
					}
				});

            ecTrSizeX.setOnEditorActionListener(new OnEditorActionListener() {

                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                        if ((actionId & EditorInfo.IME_MASK_ACTION) == EditorInfo.IME_ACTION_DONE) {
                            selectedEntity.size.x = Float.parseFloat(v.getText().toString());
                            int slc = ecTrSizeX.getSelectionStart();
                            v.setText(Float.toString(selectedEntity.size.x));
                            ecTrSizeX.setSelection(slc);
                            return true;
                        }
                        return false;
                    }
                });
            ecTrSizeY.setOnEditorActionListener(new OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                        if ((actionId & EditorInfo.IME_MASK_ACTION) == EditorInfo.IME_ACTION_DONE) {
                            selectedEntity.size.y = Float.parseFloat(v.getText().toString());
                            int slc = ecTrSizeY.getSelectionStart();
                            v.setText(Float.toString(selectedEntity.size.y));
                            ecTrSizeY.setSelection(slc);
                            return true;
                        }
                        return false;
                    }
				});
            ecTrRotation.setOnEditorActionListener(new OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                        if ((actionId & EditorInfo.IME_MASK_ACTION) == EditorInfo.IME_ACTION_DONE) {
                            selectedEntity.rotation = Float.parseFloat(v.getText().toString());
                            int slc = ecTrRotation.getSelectionStart();
                            v.setText(Float.toString(selectedEntity.rotation));
                            ecTrRotation.setSelection(slc);
                            return true;
                        }
                        return false;
                    }
				});
		}
        {
            ecFont = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.component_font, CEA, false);
			ecFnTx = ecFont.findViewById(R.id.fontEditText);
            ecFnSz = ecFont.findViewById(R.id.fontSize);
            ecFnCp = ecFont.findViewById(R.id.fontCP);

			((TextView)ecFont.findViewById(R.id.string_fontText))
				.setText(App.Resources.getString(R.string.font_edittext));
			((TextView)ecFont.findViewById(R.id.string_fontSize))
				.setText(App.Resources.getString(R.string.size));
			((TextView)ecFont.findViewById(R.id.string_fontColor))
				.setText(App.Resources.getString(R.string.color));

            ecFnSz.setOnEditorActionListener(new OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if ((actionId & EditorInfo.IME_MASK_ACTION) == EditorInfo.IME_ACTION_DONE) {
                            ((FontComponent)selectedComponent).setSize(Float.parseFloat(v.getText().toString()));
                            return true;
                        }
                        return false;
                    }
                });
            ecFnTx.setOnEditorActionListener(new OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                        if ((actionId & EditorInfo.IME_MASK_ACTION) == EditorInfo.IME_ACTION_DONE) {
                            ((FontComponent)selectedComponent).setText(v.getText().toString());
                            return true;
                        }
                        return false;
                    }
				});
            ecFnCp.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View p1) {
                        ColorPickerDialog cpd = new ColorPickerDialog(EditorActivity.this);
                        cpd.setOnColorPickedListener(new ColorPickerDialog.onColorPickedListener() {

                                @Override
                                public void onColorPicked(String hex, double a, double r, double g, double b) {
                                    selectedComponent.getPaint().setARGB((int)a, (int)r, (int)g, (int)b);
                                    ecFnCp.setBackgroundColor(selectedComponent.getPaint().getColor());
                                }


                            });
                        cpd.show("#" + Integer.toHexString(selectedComponent.getPaint().getColor()).toUpperCase());
                    }
                });
        }
		{ 
			ecLuaScript = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.component_luascript, CEA, false);
			ecLuaScriptPath = ecLuaScript.findViewById(R.id.luascriptSPath);
			((TextView)ecLuaScript.findViewById(R.id.string_luapath)).setText(App.Resources.getString(R.string.path));
			ecLuaScriptPath.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View p1) {
						final GridViewDialog dlg = new GridViewDialog(EditorActivity.this);
						dlg.setTitle("Select Texture");
						dlg.setList(files);
						dlg.getGridView().setOnItemClickListener(new OnItemClickListener() {
								@Override
								public void onItemClick(AdapterView<?> p1, View p2, int p, long p4) {
									((LuaScript)selectedComponent).init(files.get(p).substring(projectPath.concat("/Assets/").length()));
									ecLuaScriptPath.setText(((LuaScript)selectedComponent).getScriptPath());
									dlg.dismiss();
								}
						});
						dlg.show();
					}
			});
		}
		{
			ecJoystick = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.component_joystick, CEA, false);
			ecJSBC = ecJoystick.findViewById(R.id.joystickColor);
			ecJSKC = ecJoystick.findViewById(R.id.joystickKnobColor);
			
			((TextView)ecJoystick.findViewById(R.id.string_jsColor)).setText(App.Resources.getString(R.string.background_color));
			((TextView)ecJoystick.findViewById(R.id.string_jsKnobColor)).setText(App.Resources.getString(R.string.color));
			ecJSBC.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View p1) {
                        ColorPickerDialog cpd = new ColorPickerDialog(EditorActivity.this);
                        cpd.setOnColorPickedListener(new ColorPickerDialog.onColorPickedListener() {
                                @Override
                                public void onColorPicked(String hex, double a, double r, double g, double b) {
                                    selectedComponent.getPaint().setARGB((int)a, (int)r, (int)g, (int)b);
                                    ecJSBC.setBackgroundColor(selectedComponent.getPaint().getColor());
                                }
                            });
                        cpd.show("#" + Integer.toHexString(selectedComponent.getPaint().getColor()).toUpperCase());
                    }
                });
            ecJSKC.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View p1) {
                        ColorPickerDialog cpd = new ColorPickerDialog(EditorActivity.this);
                        cpd.setOnColorPickedListener(new ColorPickerDialog.onColorPickedListener() {

                                @Override
                                public void onColorPicked(String hex, double a, double r, double g, double b) {
                                    ((JoystickComponent)selectedComponent).getKnobPaint().setARGB((int)a, (int)r, (int)g, (int)b);
                                    ecJSBC.setBackgroundColor(((JoystickComponent)selectedComponent).getKnobPaint().getColor());
                                }
                            });
                        cpd.show("#" + Integer.toHexString(((JoystickComponent)selectedComponent).getKnobPaint().getColor()).toUpperCase());
                    }
                });
		}
		{
			ecMusicPlayer = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.component_musicplayer, CEA, false);
			
			ecMusicPath = ecMusicPlayer.findViewById(R.id.musicPath);
			((TextView)ecMusicPlayer.findViewById(R.id.string_mscPath)).setText(App.Resources.getString(R.string.path));
			ecMusicPath.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View p1) {
						final GridViewDialog dlg = new GridViewDialog(EditorActivity.this);
						dlg.setTitle("Select Music or Audio");
						dlg.setList(files);
						dlg.getGridView().setOnItemClickListener(new OnItemClickListener() {
								@Override
								public void onItemClick(AdapterView<?> p1, View p2, int p, long p4) {
									try{
										((MusicPlayerComponent)selectedComponent).setSource(files.get(p).substring(projectPath.concat("/Assets/").length()));
										ecMusicPath.setText(((MusicPlayerComponent)selectedComponent).getSourcePath());
									} catch(Exception e) {}
									dlg.dismiss();
								}
							});
						dlg.show();
					}
				
			});
			
			(ecMusicIsPlaying = ecMusicPlayer.findViewById(R.id.musicPlying)).
			setText(App.Resources.getString(R.string.play_on_start));
			(ecMusicIsLooping = ecMusicPlayer.findViewById(R.id.musicLooping)).
			setText(App.Resources.getString(R.string.is_looping));
			
			ecMusicIsPlaying.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton p1, boolean s) {
						((MusicPlayerComponent)selectedComponent).playonstart = s;
					}
				
			});
			
			ecMusicIsLooping.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton p1, boolean s) {
						((MusicPlayerComponent)selectedComponent).setLooping(s);
					}

				});
		}

		adEnt = new ArrayAdapter<Entity>(this, 0){
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) convertView = LayoutInflater.from(EditorActivity.this).inflate(R.layout.ListItem1, parent, false);
                ImageView iv = convertView.findViewById(R.id.li1ImageView1);
				TextView tv = convertView.findViewById(R.id.li1TextView1);
				if (selectedEntity == getItem(position))
                    iv.setImageDrawable(App.Resources.getDrawable(R.drawable.ic_cross));
                else
                    iv.setImageDrawable(App.Resources.getDrawable(R.drawable.ic_entity));
                tv.setText(getItem(position).ID);
				return convertView;
			}
		};

		for (Entity e: universe.getEntities()) adEnt.add(e);
		adEnt.notifyDataSetChanged();
		lwEntities.setAdapter(adEnt);

		lwEntities.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int index, long p4) {
                    if (selectedEntity == adEnt.getItem(index)) {
                        selectedEntity = null;
                        rightDrawer.setX(universe.Camera.getSize().x);
                        CEADrawer.setX(universe.Camera.getSize().x);
                    } else
					    selectEntity(adEnt.getItem(index));
                    adEnt.notifyDataSetChanged();
				}
			});

		lwEntities.setOnItemLongClickListener(new OnItemLongClickListener(){
				@Override
				public boolean onItemLongClick(AdapterView<?> p1, View li, final int index, long p4) {
                    PopupMenu pm = new PopupMenu(EditorActivity.this, li);
                    pm.inflate(R.menu.entityoptions);
					pm.getMenu().getItem(0).setTitle(App.Resources.getString(R.string.copy));
					pm.getMenu().getItem(1).setTitle(App.Resources.getString(R.string.remove));
                    pm.setOnMenuItemClickListener(new OnMenuItemClickListener() {

                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.entOpRem:
                                        Entity e = adEnt.getItem(index);
                                        universe.getEntities().remove(e);
                                        adEnt.remove(e);
                                        adEnt.notifyDataSetChanged();
                                        if (e == selectedEntity) {
                                            rightDrawer.setX(universe.Camera.getSize().x);
                                            CEADrawer.setX(universe.Camera.getSize().x);
                                        }
                                        break;
                                    case R.id.entOpCop:
                                        adEnt.add(adEnt.getItem(index).clone());
                                        adEnt.notifyDataSetChanged();
                                }
                                return false;
                            }


                        });
					pm.show();
					return true;
				}
			});

		addEntity.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1) {
					Entity e = new Entity(universe);
					adEnt.add(e);
					adEnt.notifyDataSetChanged();
				}
			});

		adCom = new ArrayAdapter<Component>(this, 0){
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) convertView = LayoutInflater.from(EditorActivity.this).inflate(R.layout.ListItem1, parent, false);
				ImageView iv = convertView.findViewById(R.id.li1ImageView1);
				TextView tv = convertView.findViewById(R.id.li1TextView1);
				Class c = getItem(position).getClass();
				if (c == LuaScript.class) {
					iv.setImageDrawable(App.Resources.getDrawable(R.drawable.ic_script));
					tv.setText("Lua Script");
				} else if (c == TextureComponent.class) {
					iv.setImageDrawable(App.Resources.getDrawable(R.drawable.ic_texture));
					tv.setText("Texture");
				} else if (c == AnimationComponent.class) {
					iv.setImageDrawable(App.Resources.getDrawable(R.drawable.ic_animation));
					tv.setText("Animation");
				} else if (c == ButtonComponent.class) {
					iv.setImageDrawable(App.Resources.getDrawable(R.drawable.ic_button));
					tv.setText("Button");
				} else if (c == SwitchComponent.class) {
					iv.setImageDrawable(App.Resources.getDrawable(R.drawable.ic_switch));
					tv.setText("Switch");
				} else if (c == FontComponent.class) {
					iv.setImageDrawable(App.Resources.getDrawable(R.drawable.ic_font));
					tv.setText("Font");
				} else if (c == ProgressBarComponent.class) {
					iv.setImageDrawable(App.Resources.getDrawable(R.drawable.ic_progressbar));
					tv.setText("Proggressbar");
				} else if (c == JoystickComponent.class) {
					iv.setImageDrawable(App.Resources.getDrawable(R.drawable.ic_joystick));
					tv.setText("Joystick");
				} else if (c == MusicPlayerComponent.class) {
					iv.setImageDrawable(App.Resources.getDrawable(R.drawable.ic_music));
					tv.setText("Music Player");
				} else if (c == CustomComponent.class) {
					switch (((CustomComponent) getItem(position)).name) {
						case "Transform":
							iv.setImageDrawable(App.Resources.getDrawable(R.drawable.ic_transform));
							tv.setText(App.Resources.getString(R.string.transform));
							break;
					}
				}
				return convertView;
			}
		};
		lwComponents.setAdapter(adCom);

		lwComponents.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
					rightDrawer.setX(universe.Camera.getSize().x);
					selectedComponent = adCom.getItem(p3);
					CEADrawer.setX(universe.Camera.getSize().x - 200 * App.density);
					CEA.removeAllViews();
					if (selectedComponent.getClass() == TextureComponent.class) {
						Texture tex = ((TextureComponent) selectedComponent).getTexture();
						if(tex.getDrawable() != null)
							ecTextureSlct.setImageBitmap(((BitmapDrawable)tex.getDrawable()).getBitmap());
						if(tex.getDrawablePath() != null)
							ecTexturePath.setText(tex.getDrawablePath());
						CEA.addView(ecTexture);
					} else if(selectedComponent.getClass() == AnimationComponent.class) {
						ArrayAdapter<Texture> adp = (ArrayAdapter<Texture>) ecAnmFrames.getAdapter();
						adp.clear();
						adp.addAll(((AnimationComponent)selectedComponent).getFrames());
						adp.notifyDataSetChanged();
						ecAnmChangeTime.setText(Float.toString(((AnimationComponent)selectedComponent).getChangeTime()));
						CEA.addView(ecAnimation);
					} else if (selectedComponent.getClass() == ButtonComponent.class) {
                        ecBtnC.setBackgroundColor(selectedComponent.getPaint().getColor());
                        ecBtnTC.setBackgroundColor(((ButtonComponent)selectedComponent).getTouchPaint().getColor());
                        CEA.addView(ecButton);
					} else if (selectedComponent.getClass() == SwitchComponent.class) {
						switchIsChecked.setChecked(((SwitchComponent)selectedComponent).isChecked());
                        ecSwBC.setBackgroundColor(selectedComponent.getPaint().getColor());
                        ecSwSBC.setBackgroundColor(((SwitchComponent)selectedComponent).getSelectedPaint().getColor());
                        ecSwHC.setBackgroundColor(((SwitchComponent)selectedComponent).getButtonPaint().getColor());
						CEA.addView(ecSwitch);
					} else if (selectedComponent.getClass() == ProgressBarComponent.class) {
                        ecPbMax.setText(Integer.toString(((ProgressBarComponent)selectedComponent).getMax()));
                        ecPbCur.setText(Integer.toString(((ProgressBarComponent)selectedComponent).getProgress()));
                        ecPbBC.setBackgroundColor(selectedComponent.getPaint().getColor());
                        ecPbPC.setBackgroundColor(((ProgressBarComponent)selectedComponent).getProgressPaint().getColor());
                        CEA.addView(ecProggressbar);
					} else if (selectedComponent.getClass() == FontComponent.class) {
                        ecFnTx.setText(((FontComponent)selectedComponent).getText());
                        ecFnSz.setText(Float.toString(((FontComponent)selectedComponent).getSize()));
                        ecFnCp.setBackgroundColor(selectedComponent.getPaint().getColor());
                        CEA.addView(ecFont);
                    } else if(selectedComponent.getClass() == LuaScript.class) {
						ecLuaScriptPath.setText(((LuaScript)selectedComponent).getScriptPath());
						CEA.addView(ecLuaScript);
					} else if (selectedComponent.getClass() == JoystickComponent.class) {
                        ecJSBC.setBackgroundColor(selectedComponent.getPaint().getColor());
                        ecJSKC.setBackgroundColor(((JoystickComponent)selectedComponent).getKnobPaint().getColor());
                        CEA.addView(ecJoystick);
					} else if(selectedComponent.getClass() == MusicPlayerComponent.class) {
						String sp = ((MusicPlayerComponent)selectedComponent).getSourcePath();
						ecMusicPath.setText(sp);
						ecMusicIsPlaying.setChecked(((MusicPlayerComponent)selectedComponent).playonstart);
						ecMusicIsLooping.setChecked(((MusicPlayerComponent)selectedComponent).isLooping());
						CEA.addView(ecMusicPlayer);
					} else if (selectedComponent.getClass() == CustomComponent.class) {
						CustomComponent cc = (EditorActivity.CustomComponent) selectedComponent;
						if (cc.name.equals("Transform")) {
							ecTrPosX.setText(Float.toString(selectedEntity.pos.x));
							ecTrPosY.setText(Float.toString(selectedEntity.pos.y));
                            ecTrSizeX.setText(Float.toString(selectedEntity.size.x));
                            ecTrSizeY.setText(Float.toString(selectedEntity.size.y));
                            ecTrRotation.setText(Float.toString(selectedEntity.rotation));
                            CEA.addView(ecTransform);
						}
					}
				}
			});

		lwComponents.setOnItemLongClickListener(new OnItemLongClickListener(){
				@Override
				public boolean onItemLongClick(AdapterView<?> p1, View li, final int index, long p4) {
					if (index == 0) return false;
                    adCom.remove(selectedEntity.getComponents().remove(index - 1));
                    adCom.notifyDataSetChanged();
					return true;
				}
			});

		addComponent.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1) {
					PopupMenu pm = new PopupMenu(EditorActivity.this, addComponent);
					pm.inflate(R.menu.components);
					pm.setOnMenuItemClickListener(new OnMenuItemClickListener(){
							@Override
							public boolean onMenuItemClick(MenuItem p1) {
								Component c = null;
								switch (p1.getItemId()) {
									case R.id.cmLuaScript:
										selectedEntity.addComponent(c = new LuaScript());
										adCom.add(c);
										break;
									case R.id.cmTexture:
										selectedEntity.addComponent(c = new TextureComponent());
										adCom.add(c);
										break;
									case R.id.cmAnimation:
										selectedEntity.addComponent(c = new AnimationComponent());
										adCom.add(c);
										break;
									case R.id.cmFont:
										selectedEntity.addComponent(c = new FontComponent());
										adCom.add(c);
										break;
									case R.id.cmProgressBar:
										selectedEntity.addComponent(c = new ProgressBarComponent());
										adCom.add(c);
										break;
									case R.id.cmButton:
										selectedEntity.addComponent(c = new ButtonComponent());
										adCom.add(c);
										break;
									case R.id.cmSwitch:
										selectedEntity.addComponent(c = new SwitchComponent());
										adCom.add(c);
										break;
									case R.id.cmJoystick:
										selectedEntity.addComponent(c = new JoystickComponent());
										adCom.add(c);
										break;
									case R.id.cmMusicPlayer:
										selectedEntity.addComponent(c = new MusicPlayerComponent());
										adCom.add(c);
										break;
								}
								adCom.notifyDataSetChanged();
								return false;
							}
						});
					try {
						Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
						fMenuHelper.setAccessible(true);
						Object menuHelper = fMenuHelper.get(pm);
						menuHelper.getClass().getDeclaredMethod("setForceShowIcon", boolean.class).invoke(menuHelper, true);
					} catch (Exception e) {}
					pm.show();
				}
			});

		addComponent.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					JoystickComponent c = new JoystickComponent();
					selectedEntity.addComponent(c);
					Toast.makeText(EditorActivity.this, "Hidden component added!", 500).show();
					return false;
				}
			});

		entitiesOpen.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1) {
                    if (cld == 1) {
                        leftDrawer.setX(-200 * App.density);
                        --cld;
						profiler.setX(40 * App.density);
                        return;
                    }
                    cld = 1;
					leftDrawer.setX(30 * App.density);
                    leftDrawer.removeAllViews();
                    leftDrawer.addView(ldentities);
					adEnt.notifyDataSetChanged();
					profiler.setX(leftDrawer.getX() + leftDrawer.getWidth() + 10 * App.density);
				}
			});
        settingsOpen.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View p1) {
                    if (cld == 2) {
                        leftDrawer.setX(-200 * App.density);
                        cld = 0;
						profiler.setX(40 * App.density);
                        return;
                    }
                    cld = 2;
                    leftDrawer.setX(30 * App.density);
                    leftDrawer.removeAllViews();
                    leftDrawer.addView(ldsettings);
					ldBcp.setBackgroundColor(universe.backgroundColor);
					setGX.setText(Float.toString(universe.getGravity().x));
					setGY.setText(Float.toString(universe.getGravity().y));
					profiler.setX(leftDrawer.getX() + leftDrawer.getWidth() + 10 * App.density);
                }
            });
		cameraOpen.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1) {
                    if (cld == 3) {
                        leftDrawer.setX(-200 * App.density);
                        cld = 0;
						profiler.setX(40 * App.density);
                        return;
                    }
                    cld = 3;
					leftDrawer.setX(30 * App.density);
                    leftDrawer.removeAllViews();
                    leftDrawer.addView(ldcamsets);
					if(universe.isResuming()) {
						Vector pos = universe.Camera.getPosition();
						setCX.setText(Float.toString(pos.x));
						setCY.setText(Float.toString(pos.y));
					} else {
						setCX.setText(Float.toString(camX));
						setCY.setText(Float.toString(camY));
					}
					Entity flw = universe.Camera.getFollowed();
					if(flw == null)
						camFes.setSelection(0);
					else
						camFes.setSelection(universe.getEntities().indexOf(flw) + 1);
					profiler.setX(leftDrawer.getX() + leftDrawer.getWidth() + 10 * App.density);
				}
			});
		rightDrawerOpen.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1) {
					if (rightDrawer.getX() == universe.Camera.getSize().x - 25 * App.density) {
						rightDrawer.setX(universe.Camera.getSize().x - 200 * App.density);
					} else {
						rightDrawer.setX(universe.Camera.getSize().x - 25 * App.density);
					}
				}
			});
        CEAClose.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1) {
					CEADrawer.setX(universe.Camera.getSize().x);
					rightDrawer.setX(universe.Camera.getSize().x - 200 * App.density);
				}
			});
		bdFilesOpen.setText(App.Resources.getString(R.string.files));
		bdFilesOpen.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1) {
                    if (cbd == 1) {
                        bottomDrawer.setY(universe.Camera.getSize().y - 25 * App.density);
                        --cbd;
						profiler.setY(bottomDrawer.getY() - 50 * App.density);
						return;
                    }
                    cbd = 1;
					bottomDrawer.setY(universe.Camera.getSize().y - 210 * App.density);
                    BE.removeAllViews();
                    BE.addView(bdFiles);
					profiler.setY(bottomDrawer.getY() - 50 * App.density);
				}
			});
		bdOutputOpen.setText(App.Resources.getString(R.string.output));
		bdOutputOpen.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View p1) {
                    if (cbd == 2) {
                        bottomDrawer.setY(universe.Camera.getSize().y - 25 * App.density);
                        cbd = 0;
						profiler.setY(bottomDrawer.getY() - 50 * App.density);
						return;
                    }
                    cbd = 2;
                    bottomDrawer.setY(universe.Camera.getSize().y - 210 * App.density);
                    BE.removeAllViews();
                    BE.addView(bdOutput);
					profiler.setY(bottomDrawer.getY() - 50 * App.density);
				}
            });
		save.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1) {
					if (isCanSave)
						save();
                    else
						Toast.makeText(EditorActivity.this, R.string.sgbsu, 500).show();
				}
			});
		envPP.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1) {
					if(isCanSave) {
						universe.Camera.getPosition().set(0, 0);
						isCanSave = false;
                    	saveData();
						Input.isDown = false;
						Input.pos.set(0, 0);
					} else if(!universe.isResuming()) 
						for(Entity e: universe.getEntities())
							for(Component c: e.getComponents())
								if(c.getClass() == MusicPlayerComponent.class)
									if(((MusicPlayerComponent)c).p)
										((MusicPlayerComponent)c).play();

					envS.setAlpha(1f);
					if (universe.isResuming()) {
						universe.setResuming(false);
						envPP.setImageDrawable(App.Resources.getDrawable(R.drawable.ic_play));
						for(Entity e: universe.getEntities())
							for(Component c: e.getComponents())
								if(c.getClass() == MusicPlayerComponent.class) {
									MusicPlayerComponent mp = (MusicPlayerComponent)c;
									mp.p = mp.isPlaying();
									mp.pause();
								}
						return;
					}
					universe.setResuming(true);
					envPP.setImageDrawable(App.Resources.getDrawable(R.drawable.ic_pause));
				}
			});
		envS.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1) {
                    if (isCanSave) return;
					for(Entity e: universe.getEntities())
						for(Component c: e.getComponents())
							if(c.getClass() == MusicPlayerComponent.class)
								((MusicPlayerComponent)c).stop();
					for (int i=0; i < LNet.sl.size(); i++)
						try {
							LNet.sl.remove(i).close();
						} catch (IOException e) {}
					selectedEntity = null;
					adEnt.clear();
					isCanSave = true;
                    universe.setUniverseData(universeData, EditorActivity.this);
					universeData = null;
					universe.setResuming(false);
					universe.Camera.zoom = 1;
					Engine.startUniverse(universe);
					envPP.setImageDrawable(App.Resources.getDrawable(R.drawable.ic_play));
					for (Entity e: universe.getEntities()) adEnt.add(e);
					adEnt.notifyDataSetChanged();
					envS.setAlpha((float)0.5);
					rightDrawer.setX(universe.Camera.getSize().x);
					CEADrawer.setX(universe.Camera.getSize().x);
				}
			});
		
		toolFE.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1) {
					if(selectedEntity == null) return;
					Vector p = selectedEntity.pos;
					universe.Camera.getPosition().set(p.x, -p.y);
				}
			});
		toolFC.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1) {
					universe.Camera.getPosition().set(0, 0);
				}
			});
		ID.setOnKeyListener(new OnKeyListener(){
				@Override
				public boolean onKey(View p1, int p2, KeyEvent p3) {

					return false;
				}
			});
		ID.setOnEditorActionListener(new OnEditorActionListener() {

				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

					if ((actionId & EditorInfo.IME_MASK_ACTION) == EditorInfo.IME_ACTION_DONE) {
						selectedEntity.ID = ID.getText().toString();
						adEnt.notifyDataSetChanged();
					}
					return false;
				}
			});
		UIb.setOnCheckedChangeListener(new OnCheckedChangeListener(){
				@Override
				public void onCheckedChanged(CompoundButton p1, boolean p2) {
					selectedEntity.setUI(p2);
				}
			});
    }

	@Override
	protected void onResume() {
		super.onResume();

		files.clear();
		FileUtil.listDir(projectPath.concat("/Assets/"), files);

		Engine.init(this);
		Engine.Assets = al;
        Engine.startUniverse(universe);
		rightDrawer.setX(universe.Camera.getSize().x);
		if(selectedEntity != null)
			rightDrawer.setX(rightDrawer.getX() - 25 * App.density);
        CEADrawer.setX(universe.Camera.getSize().x);
		universeData = null;
    }

	@Override
	protected void onPause() {
		super.onPause();
		envS.performClick();
	}

	void selectEntity(Entity e) {
		selectedEntity = e;
		if (rightDrawer.getX() == universe.Camera.getSize().x) rightDrawer.setX(universe.Camera.getSize().x - 25 * App.density);
		adCom.clear();
		adCom.add(new CustomComponent("Transform"));
		for (Component c: e.getComponents())
			adCom.add(c);
		ID.setText(e.ID);
		UIb.setChecked(e.isUI());
	}

	void save() {
		saveData();
		FileUtil.writeFile(projectPath.concat("/Assets/").concat(currentUniverse).concat(".universe"), universeData);
		String scf = projectPath.concat("/data/").concat("ScreenCap.png");
		if(FileUtil.isExistFile(scf))
			FileUtil.deleteFile(scf);
		try {
			universe.captureBitmap().compress(Bitmap.CompressFormat.PNG, 1, new FileOutputStream(scf));
		} catch (FileNotFoundException e) {}
		universeData = null;
	}

	void saveData() {
		try {
			JSONObject jo = new JSONObject();
            jo.put("backgroundColor", universe.backgroundColor);
			jo.put("grav.x", universe.getGravity().x);
			jo.put("grav.y", universe.getGravity().y);
			JSONObject cam = new JSONObject();
			{
				cam.put("pos.x", camX);
				cam.put("pos.y", camY);
				Entity e = universe.Camera.getFollowed();
				cam.put("followed", e == null ? -1: universe.getEntities().indexOf(e));
			}
			jo.put("camera", cam);
			JSONArray jae = new JSONArray();
			for (Entity e: universe.getEntities()) {
				JSONObject joe = new JSONObject();
				joe.put("ID", e.ID);
				joe.put("pos.x", e.pos.x);
				joe.put("pos.y", e.pos.y);
				joe.put("size.x", e.size.x);
				joe.put("size.y", e.size.y);
				joe.put("rotation", e.rotation);
				joe.put("isUI", e.isUI());
				JSONArray jac = new JSONArray();
				for (Component c: e.getComponents()) try {
						jac.put(c.toJSON());
					} catch (JSONException exc) {}
				joe.put("components", jac);
				jae.put(joe);
			}
			jo.put("entities", jae);
			universeData = jo.toString(4);
		} catch (Exception e) {
			Toast.makeText(EditorActivity.this, e.getMessage(), 500).show();
		}
	}

	class CustomComponent extends Component {

		String name;

		CustomComponent(String name) {
			this.name = name;
		}
	}
}
