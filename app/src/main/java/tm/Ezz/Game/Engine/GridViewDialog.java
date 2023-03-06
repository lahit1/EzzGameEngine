package tm.Ezz.Game.Engine;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import tm.Ezz.Game.Engine.App;
import tm.Ezz.Game.Engine.R;

public class GridViewDialog {

    private GridView gridview;
	private AlertDialog dialog;
	private List<String> list = new ArrayList<String>();

	public GridViewDialog(final Context context) {
		gridview = new GridView(context);
		gridview.setNumColumns(3);
		gridview.setHorizontalSpacing((int)(5 * App.density));
		int padding =(int)(5 * App.density);
		gridview.setPadding(padding, padding, padding, padding);
		gridview.setAdapter(new BaseAdapter() {
				@Override
				public int getCount() {
					return list.size();
				}

				@Override
				public Object getItem(int index) {
					return list.get(index);
				}

				@Override
				public long getItemId(int p1) {
					return 0;
				}
				@Override
				public View getView(int index, View convertview, ViewGroup parent) {
					if (convertview == null)
						convertview = LayoutInflater.from(context).inflate(R.layout.ListFileItem, parent, false);
					TextView ft = convertview.findViewById(R.id.LFIName);
					ImageView img = convertview.findViewById(R.id.lfiImg);
                    String fileName = Uri.parse(list.get(index)).getLastPathSegment();
					ft.setText(fileName);
					if (fileName.endsWith(".universe"))
						img.setImageDrawable(App.Resources.getDrawable(R.drawable.app_icon));
					else if (fileName.endsWith(".lua"))
						img.setImageDrawable(App.Resources.getDrawable(R.drawable.ic_script));
					else {
						String[] imgExs = {".png", ".jpg", ".jpeg"};
						for (String ex: imgExs)
							if (fileName.endsWith(ex))
								img.setImageBitmap(BitmapFactory.decodeFile(list.get(index)));
					}
					return convertview;
				}
			});

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(gridview);

		dialog = builder.create();
	}

	public void setTitle(String title) {
		dialog.setTitle(title);
	}

	public void setList(List<String> list) {
		this.list.clear();
		this.list.addAll(list);
	}

	public GridView getGridView() {
		return gridview;
	}

	public void show() {
		dialog.show();
	}

	public void dismiss() {
		dialog.dismiss();
	}
}
