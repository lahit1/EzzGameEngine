package tm.Ezz.Game.Engine;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.luaj.vm2.Globals;
import org.luaj.vm2.lib.jse.JsePlatform;
import java.io.IOException;
import java.io.StringReader;
import android.widget.Toast;
import com.Ezz.Game.Engine.luascript.EzzGlobals;

public class ScriptEditorActivity extends Activity {

	EditText et;
	TextView lln;
	ImageButton backB, saveB;
	boolean edited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_script_editor);
		et = findViewById(R.id.script_editorEditText);
		lln = findViewById(R.id.script_editorLineNums);
		backB = findViewById(R.id.script_editorBackBut);
		saveB = findViewById(R.id.script_editorSaveBut);

		et.setText(FileUtil.readFile(getIntent().getStringExtra("fpath")));

        String ln = "1";
        int line = 1;
        char[] arr = et.getText().toString().toCharArray();
        for (int i=0; i < arr.length; i++) 
            if (arr[i] == '\n')
                ln += "\n" + ++line;
        if (lln.getText() != ln)
            lln.setText(ln);

		et.setOnKeyListener(new OnKeyListener() {
				@Override
				public boolean onKey(View v, int key, KeyEvent ke) {
					String ln = "1";
					int line = 1;
					char[] arr = et.getText().toString().toCharArray();
					for (int i=0; i < arr.length; i++) 
						if (arr[i] == '\n')
							ln += "\n" + ++line;
					if (lln.getText() != ln)
						lln.setText(ln);
					edited = true;
					return false;
				}
			});
		TextWatcher tw = new TextWatcher() {

			ColorScheme basics = new ColorScheme(
				Pattern.compile(
					"\"([^\"]|\\.)*\"|'([^']|\\.)*'|[0-9]+"),
				Color.parseColor("#af0000")
			);

			ColorScheme keywordR = new ColorScheme(
				Pattern.compile("\\bnot\\b|\\bin\\b"),
				Color.parseColor("#af0000")
			);

			ColorScheme keywordG = new ColorScheme(
				Pattern.compile("==|~=|<|>|>=|<=||\\blocal\\b"),
				Color.parseColor("#00af00")
			);

			ColorScheme keywordB = new ColorScheme(
				Pattern.compile("\\bfunction\\b|\\bfunction\\(|\\bend\\b|\\bif\\b|\\bif\\(|\\belse\\b|\\belseif\\b|\\elseif\\(|\\bwhile\\b|\\bwhile\\(|\\buntil\\b|\\bfor\\b|\\bfor\\(|\\brepeat\\b|\\bbreak\\b|\\breturn\\b|\\bthen\\b|\\bnil\\b"),
				Color.parseColor("#0000af")
			);

			ColorScheme brackets = new ColorScheme(
				Pattern.compile("\\(|\\)|\\{|\\}|\\[|\\]"),
				Color.parseColor("#7f7fff")
			);

			ColorScheme keywordY = new ColorScheme(
				Pattern.compile("=|\\+|\\-|\\*|\\/|\\band\\b|\\bor\\b"),
				Color.parseColor("#afaf00")
			);
			
			ColorScheme keywordGy = new ColorScheme(
			Pattern.compile("--.*|^#!.*"),
				Color.parseColor("#afafaf")
			);
			
			final ColorScheme[] schemes = {keywordR, keywordB, brackets, keywordY,keywordG, keywordGy, basics};

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				removeSpans(s, ForegroundColorSpan.class);
				for (ColorScheme scheme : schemes) {
					for (Matcher m = scheme.pattern.matcher(s); m.find();) {
						s.setSpan(new ForegroundColorSpan(scheme.color),
								  m.start(),
								  m.end(),
								  Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
				}
			}

			void removeSpans(Editable e, Class<? extends CharacterStyle> type) {
				CharacterStyle[] spans = e.getSpans(0, e.length(), type);
				for (CharacterStyle span : spans) {
					e.removeSpan(span);
				}
			}

			class ColorScheme {
				final Pattern pattern;
				final int color;

				ColorScheme(Pattern pattern, int color) {
					this.pattern = pattern;
					this.color = color;
				}
			}

		};
		tw.afterTextChanged(et.getText());
		et.addTextChangedListener(tw);
		backB.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1) {
					onBackPressed();
				}
			});

		saveB.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1) {
					try {
						EzzGlobals.standardGlobals().compilePrototype(new StringReader(et.getText().toString()), "Prototype.lua");
						FileUtil.writeFile(getIntent().getStringExtra("fpath"), et.getText().toString());
						edited = false;
					}
					catch (Exception e) {
						Toast.makeText(ScriptEditorActivity.this, e.getMessage(), 500).show();
					}
				}
			});
    }
}
