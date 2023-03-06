package tm.Ezz.Game.Engine;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import tm.Ezz.Game.Engine.R;
import android.content.Context;
import android.app.Activity;

public class ColorPickerDialog {
    
    private Activity context;
    private onColorPickedListener ocpl;
    
    public ColorPickerDialog(Activity context) {
        this.context = context;
    }
    
    public void show(String Hex){
        if(context == null || ocpl == null) return;
        final AlertDialog ColorPicker = new AlertDialog.Builder(context).create();
        LayoutInflater ColorPickerLI = context.getLayoutInflater();
        View ColorPickerCV = (View) ColorPickerLI.inflate(R.layout.color_picker_dialog, null);
        ColorPicker.setView(ColorPickerCV);
        final SeekBar ska = (SeekBar)
            ColorPickerCV.findViewById(R.id.seekbar1);

        final SeekBar skr = (SeekBar)
            ColorPickerCV.findViewById(R.id.seekbar2);

        final SeekBar skg = (SeekBar)
            ColorPickerCV.findViewById(R.id.seekbar3);

        final SeekBar skb = (SeekBar)
            ColorPickerCV.findViewById(R.id.seekbar4);

        final TextView a = (TextView)
            ColorPickerCV.findViewById(R.id.textview6);

        final TextView r = (TextView)
            ColorPickerCV.findViewById(R.id.textview7);

        final TextView g = (TextView)
            ColorPickerCV.findViewById(R.id.textview8);

        final TextView b = (TextView)
            ColorPickerCV.findViewById(R.id.textview9);

        final Button cancelar = (Button)
            ColorPickerCV.findViewById(R.id.button1);

        final Button confirmar = (Button)
            ColorPickerCV.findViewById(R.id.button2);

        final LinearLayout cor = (LinearLayout)
            ColorPickerCV.findViewById(R.id.linear12);

        final EditText hex = (EditText)
            ColorPickerCV.findViewById(R.id.edittext1);
        InputFilter[] editFiltersHEX = hex.getFilters(); InputFilter[] newFiltersHEX = new InputFilter[editFiltersHEX.length + 1]; System.arraycopy(editFiltersHEX, 0, newFiltersHEX, 0, editFiltersHEX.length); newFiltersHEX[editFiltersHEX.length] = new InputFilter.LengthFilter(8); hex.setFilters(newFiltersHEX);
        ska.getProgressDrawable().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        ska.getThumb().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);

        skr.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        skr.getThumb().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

        skg.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
        skg.getThumb().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);

        skb.getProgressDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
        skb.getThumb().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
        try{

            hex.setText(Hex.replace("#", ""));

            a.setText(String.valueOf((long)(Double.parseDouble(Hex.replace("#", "").toLowerCase().substring((int)(1), (int)(2)).replace("a", "10").replace("b", "11").replace("c", "12").replace("d", "13").replace("e", "14").replace("f", "15")) + (Double.parseDouble(Hex.replace("#", "").toLowerCase().substring((int)(0), (int)(1)).replace("a", "10").replace("b", "11").replace("c", "12").replace("d", "13").replace("e", "14").replace("f", "15")) * 16))));

            ska.setProgress((int)Double.parseDouble(a.getText().toString()));

            r.setText(String.valueOf((long)(Double.parseDouble(Hex.replace("#", "").toLowerCase().substring((int)(3), (int)(4)).replace("a", "10").replace("b", "11").replace("c", "12").replace("d", "13").replace("e", "14").replace("f", "15")) + (Double.parseDouble(Hex.replace("#", "").toLowerCase().substring((int)(2), (int)(3)).replace("a", "10").replace("b", "11").replace("c", "12").replace("d", "13").replace("e", "14").replace("f", "15")) * 16))));

            skr.setProgress((int)Double.parseDouble(r.getText().toString()));

            g.setText(String.valueOf((long)(Double.parseDouble(Hex.replace("#", "").toLowerCase().substring((int)(5), (int)(6)).replace("a", "10").replace("b", "11").replace("c", "12").replace("d", "13").replace("e", "14").replace("f", "15")) + (Double.parseDouble(Hex.replace("#", "").toLowerCase().substring((int)(4), (int)(5)).replace("a", "10").replace("b", "11").replace("c", "12").replace("d", "13").replace("e", "14").replace("f", "15")) * 16))));

            skg.setProgress((int)Double.parseDouble(g.getText().toString()));

            b.setText(String.valueOf((long)(Double.parseDouble(Hex.replace("#", "").toLowerCase().substring((int)(7), (int)(8)).replace("a", "10").replace("b", "11").replace("c", "12").replace("d", "13").replace("e", "14").replace("f", "15")) + (Double.parseDouble(Hex.replace("#", "").toLowerCase().substring((int)(6), (int)(7)).replace("a", "10").replace("b", "11").replace("c", "12").replace("d", "13").replace("e", "14").replace("f", "15")) * 16))));

            skb.setProgress((int)Double.parseDouble(b.getText().toString()));

            cor.setBackground(new android.graphics.drawable.ColorDrawable(Color.parseColor(Hex)));

        }catch(Exception e){

        }
        ska.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged (SeekBar _param1, int _param2, boolean _param3) {
                    final int _progressValue = _param2;

                    a.setText(String.valueOf((long)(_progressValue)));

                    hex.setText("#".concat(String.format("%02X%02X%02X%02X", new Object[]{
                                                             Integer.valueOf((int)ska.getProgress()),
                                                             Integer.valueOf((int)skr.getProgress()), Integer.valueOf((int)skg.getProgress()), Integer.valueOf((int)skb.getProgress())
                                                         })).replace("#", ""));

                }

                @Override
                public void onStartTrackingTouch(SeekBar _param1) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar _param2) {

                }
            });
        skr.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged (SeekBar _param1, int _param2, boolean _param3) {
                    final int _progressValue = _param2;

                    r.setText(String.valueOf((long)(_progressValue)));

                    hex.setText("#".concat(String.format("%02X%02X%02X%02X", new Object[]{
                                                             Integer.valueOf((int)ska.getProgress()),
                                                             Integer.valueOf((int)skr.getProgress()), Integer.valueOf((int)skg.getProgress()), Integer.valueOf((int)skb.getProgress())
                                                         })).replace("#", ""));

                }

                @Override
                public void onStartTrackingTouch(SeekBar _param1) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar _param2) {

                }
            });
        skg.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged (SeekBar _param1, int _param2, boolean _param3) {
                    final int _progressValue = _param2;

                    g.setText(String.valueOf((long)(_progressValue)));

                    hex.setText("#".concat(String.format("%02X%02X%02X%02X", new Object[]{
                                                             Integer.valueOf((int)ska.getProgress()),
                                                             Integer.valueOf((int)skr.getProgress()), Integer.valueOf((int)skg.getProgress()), Integer.valueOf((int)skb.getProgress())
                                                         })).replace("#", ""));

                }

                @Override
                public void onStartTrackingTouch(SeekBar _param1) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar _param2) {

                }
            });
        skb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged (SeekBar _param1, int _param2, boolean _param3) {
                    final int _progressValue = _param2;

                    b.setText(String.valueOf((long)(_progressValue)));

                    hex.setText("#".concat(String.format("%02X%02X%02X%02X", new Object[]{
                                                             Integer.valueOf((int)ska.getProgress()),
                                                             Integer.valueOf((int)skr.getProgress()), Integer.valueOf((int)skg.getProgress()), Integer.valueOf((int)skb.getProgress())
                                                         })).replace("#", ""));

                }

                @Override
                public void onStartTrackingTouch(SeekBar _param1) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar _param2) {

                }
            });
        hex.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
                    final String _charSeq = _param1.toString();
                    hex.setSelection(hex.getText().length());

                    try{
                        if (hex.getText().toString().length() == 8) {

                            a.setText(String.valueOf((long)(Double.parseDouble(_charSeq.replace("#", "").toLowerCase().substring((int)(1), (int)(2)).replace("a", "10").replace("b", "11").replace("c", "12").replace("d", "13").replace("e", "14").replace("f", "15")) + (Double.parseDouble(_charSeq.replace("#", "").toLowerCase().substring((int)(0), (int)(1)).replace("a", "10").replace("b", "11").replace("c", "12").replace("d", "13").replace("e", "14").replace("f", "15")) * 16))));

                            ska.setProgress((int)Double.parseDouble(a.getText().toString()));

                            r.setText(String.valueOf((long)(Double.parseDouble(_charSeq.replace("#", "").toLowerCase().substring((int)(3), (int)(4)).replace("a", "10").replace("b", "11").replace("c", "12").replace("d", "13").replace("e", "14").replace("f", "15")) + (Double.parseDouble(_charSeq.replace("#", "").toLowerCase().substring((int)(2), (int)(3)).replace("a", "10").replace("b", "11").replace("c", "12").replace("d", "13").replace("e", "14").replace("f", "15")) * 16))));

                            skr.setProgress((int)Double.parseDouble(r.getText().toString()));

                            g.setText(String.valueOf((long)(Double.parseDouble(_charSeq.replace("#", "").toLowerCase().substring((int)(5), (int)(6)).replace("a", "10").replace("b", "11").replace("c", "12").replace("d", "13").replace("e", "14").replace("f", "15")) + (Double.parseDouble(_charSeq.replace("#", "").toLowerCase().substring((int)(4), (int)(5)).replace("a", "10").replace("b", "11").replace("c", "12").replace("d", "13").replace("e", "14").replace("f", "15")) * 16))));

                            skg.setProgress((int)Double.parseDouble(g.getText().toString()));

                            b.setText(String.valueOf((long)(Double.parseDouble(_charSeq.replace("#", "").toLowerCase().substring((int)(7), (int)(8)).replace("a", "10").replace("b", "11").replace("c", "12").replace("d", "13").replace("e", "14").replace("f", "15")) + (Double.parseDouble(_charSeq.replace("#", "").toLowerCase().substring((int)(6), (int)(7)).replace("a", "10").replace("b", "11").replace("c", "12").replace("d", "13").replace("e", "14").replace("f", "15")) * 16))));

                            skb.setProgress((int)Double.parseDouble(b.getText().toString()));

                            cor.setBackground(new android.graphics.drawable.ColorDrawable(Color.parseColor("#".concat(_charSeq))));

                        }

                    }catch(Exception e){
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {

                }

                @Override
                public void afterTextChanged(Editable _param1) {

                }
            });
		cancelar.setText(App.Resources.getString(android.R.string.cancel));
        cancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View _view) {
                    ColorPicker.dismiss();
                }
            });
		confirmar.setText(App.Resources.getString(android.R.string.ok));
        confirmar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View _view) {
                    hex.setText("#".concat(String.format("%02X%02X%02X%02X", new Object[]{
                                                             Integer.valueOf((int)ska.getProgress()),
                                                             Integer.valueOf((int)skr.getProgress()), Integer.valueOf((int)skg.getProgress()), Integer.valueOf((int)skb.getProgress())
                                                         })).replace("#", ""));

                    ocpl.onColorPicked("#".concat(hex.getText().toString()), Double.parseDouble(a.getText().toString()), Double.parseDouble(r.getText().toString()), Double.parseDouble(g.getText().toString()), Double.parseDouble(b.getText().toString()));
                    ColorPicker.dismiss();
                }
            });
        ColorPicker.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ColorPicker.setCancelable(false);

        ColorPicker.show();
    }

    public void setOnColorPickedListener(onColorPickedListener ocpl) {
        this.ocpl = ocpl;
    }
    
    public interface onColorPickedListener {
        public void onColorPicked(final String hex, final double a, final double r, final double g, final double b);
    }
    
}
