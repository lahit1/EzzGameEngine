package tm.Ezz.Game.Engine;

import android.app.Application;
import android.content.res.Resources;
import android.os.Build;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import java.util.Map;
import java.util.HashMap;
import android.content.Intent;
import android.app.AlertDialog;
import android.content.DialogInterface;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;

public class App extends Application {
	
	static float density;
	static Resources Resources;

	private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;
	@Override
	public void onCreate() {
		Resources = getResources();
		density = Resources.getDisplayMetrics().density;
		this.uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(final Thread thread, final Throwable ex) {
				FirebaseDatabase db = FirebaseDatabase.getInstance();
				
				boolean isrooted = false;
				{
					String[] paths = { "/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
						"/system/bin/failsafe/su", "/data/local/su", "/su/bin/su"};
					for (String path : paths)
						if (new File(path).exists()) {
							isrooted = true;
							break;
						}
				}
				
				Map<String, Object> mp = new HashMap<String, Object>();
				
				mp.put("device", Build.DEVICE);
				mp.put("device-modelname", Build.MODEL);
				mp.put("version", Build.VERSION.SDK_INT);
				mp.put("version-release", String.format("%s : %s", Build.VERSION.RELEASE, Build.VERSION.CODENAME));
				mp.put("bootloader", Build.BOOTLOADER);
				mp.put("root", isrooted);
				mp.put("exception-name", ex.getClass().getSimpleName());
				mp.put("exception-message", ex.getMessage());
				mp.put("exception-stacktrace", getStackTrace(ex));
				mp.put("date", new Date().toString());
				db.getReference().child("errorlogs").child("beta").push().
					setValue(mp).addOnFailureListener(new OnFailureListener() {

						@Override
						public void onFailure(Exception p1) {
							uncaughtExceptionHandler.uncaughtException(thread, ex);
						}
					}).addOnSuccessListener(new OnSuccessListener<Void>() {

						@Override
						public void onSuccess(Void p1) {
							uncaughtExceptionHandler.uncaughtException(thread, ex);
						}
					});
			}
		});
        super.onCreate();
	}
	
	private String getStackTrace(Throwable th){
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		Throwable cause = th;
		while(cause != null){
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		final String stacktraceAsString = result.toString();
		printWriter.close();
		return stacktraceAsString;
	}
}
