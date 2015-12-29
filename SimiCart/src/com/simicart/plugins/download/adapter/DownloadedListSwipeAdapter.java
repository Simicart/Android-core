package com.simicart.plugins.download.adapter;

import java.io.File;
import java.util.ArrayList;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DownloadedListSwipeAdapter extends BaseSwipeAdapter {

	private Context mContext;
	protected ArrayList<File> list_downloaded;

	public DownloadedListSwipeAdapter(Context mContext,
			ArrayList<File> list_download) {
		this.mContext = mContext;
		this.list_downloaded = list_download;
	}

	@Override
	public int getSwipeLayoutResourceId(int position) {
		return Rconfig.getInstance().id("swipe");
	}

	// ATTENTION: Never bind listener or fill values in generateView.
	// You have to do that in fillValues method.
	@Override
	public View generateView(int position, ViewGroup parent) {
		return LayoutInflater.from(mContext)
				.inflate(
						Rconfig.getInstance().layout(
								"plugins_downloaded_product_item"), null);
	}

	@Override
	public void fillValues(final int position, final View convertView) {
		try {
			final File item = list_downloaded.get(position);
			TextView txt_filename = (TextView) convertView.findViewById(Rconfig
					.getInstance().id("text_downloaded_filename"));
			txt_filename.setText(item.getName());

			RelativeLayout rl_delete = (RelativeLayout) convertView
					.findViewById(Rconfig.getInstance().id("rl_delete"));
			rl_delete.setBackgroundColor(Config.getInstance().getColorMain());
			ImageView iv_delete = (ImageView) convertView.findViewById(Rconfig
					.getInstance().id("iv_delete"));
			Drawable drawable = mContext.getResources().getDrawable(
					Rconfig.getInstance().drawable("recycle_bin"));
			drawable.setColorFilter(Config.getInstance()
					.getTop_menu_icon_color(), PorterDuff.Mode.SRC_ATOP);
			iv_delete.setImageDrawable(drawable);

			rl_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					((SwipeLayout) convertView).close();
					list_downloaded.remove(item);
					item.delete();
					notifyDataSetChanged();
					Toast.makeText(mContext,
							"Delete file name" + item.getName(),
							Toast.LENGTH_LONG).show();
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public int getCount() {
		return list_downloaded.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}