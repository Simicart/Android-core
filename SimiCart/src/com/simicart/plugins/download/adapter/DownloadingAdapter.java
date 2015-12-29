package com.simicart.plugins.download.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.simicart.MainActivity;
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.download.common.DownloadConstant;
import com.simicart.plugins.download.entity.DownloadingEntity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DownloadingAdapter extends BaseAdapter {

	protected Context context;
	protected LayoutInflater inflater;
	protected ArrayList<DownloadingEntity> list_downloading;
	protected DownloadManager manager;
	protected SharedPreferences prefs;
	protected List<String> downloadingID;

	public DownloadingAdapter(Context context, LayoutInflater inflater, ArrayList<DownloadingEntity> list_downloading) {
		super();
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.list_downloading = list_downloading;
		this.manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
		this.prefs = context.getSharedPreferences(DownloadConstant.PREF, Context.MODE_PRIVATE);
		String serialized = prefs.getString(DownloadConstant.TASKS, null);
		if (serialized != null) {
			downloadingID = new LinkedList<String>(Arrays.asList(TextUtils.split(serialized, ",")));
		} else
			downloadingID = new ArrayList<>();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_downloading.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list_downloading.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(Rconfig.getInstance().layout("plugins_downloading_product_item_layout"),
					arg2, false);
			holder = new ViewHolder();
			holder.tv_downloading_file_name = (TextView) convertView
					.findViewById(Rconfig.getInstance().id("downloading_file_name"));
			holder.tv_downloading_file_size = (TextView) convertView
					.findViewById(Rconfig.getInstance().id("downloading_file_size"));
			holder.tv_downloading_file_downloaded = (TextView) convertView
					.findViewById(Rconfig.getInstance().id("downloading_file_downloaded"));
			holder.pgr_downloading = (ProgressBar) convertView
					.findViewById(Rconfig.getInstance().id("downloading_pgr"));
			holder.btn_cancel = (Button) convertView.findViewById(Rconfig.getInstance().id("downloading_cancel"));
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final DownloadingEntity item = list_downloading.get(position);
		DownloadManager.Query query = new DownloadManager.Query();
		query.setFilterById(Long.parseLong(item.getDownloadingID()));
		Cursor cur = manager.query(query);
		if (cur.moveToFirst()) {
			holder.tv_downloading_file_name.setText(Config.getInstance().getText("File Title: ")
					+ cur.getString(cur.getColumnIndex(DownloadManager.COLUMN_TITLE)));
		}

		holder.btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				downloadingID.remove(position);
				Editor editor = prefs.edit();
				editor.putString(DownloadConstant.TASKS, TextUtils.join(",", downloadingID));
				editor.commit();

				manager.remove(Long.parseLong(item.getDownloadingID()));
				Intent i = new Intent();
				i.setAction(DownloadConstant.DOWNLOAD_ACTION);
				context.sendBroadcast(i);
			}
		});

		if (downloadingID.size() > 0) {
			final Long donwloadId = Long.parseLong(item.getDownloadingID());
			new Thread(new Runnable() {

				@Override
				public void run() {

					boolean downloading = true;

					while (downloading) {

						DownloadManager.Query q = new DownloadManager.Query();
						q.setFilterById(donwloadId);

						Cursor cursor = manager.query(q);
						if (!cursor.moveToFirst())
							break;
						cursor.moveToFirst();
						final int bytes_downloaded = cursor
								.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
						final int bytes_total = cursor
								.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

						if (cursor.getInt(cursor
								.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
							downloading = false;
						}

						final int dl_progress = (int) ((bytes_downloaded * 100l) / bytes_total);

						MainActivity.instance.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								holder.tv_downloading_file_size.setText(Config.getInstance().getText("File Size: "
										+ new DecimalFormat("##.##").format(bytes_total / (Math.pow(1024, 2))) + "MB"));
								holder.tv_downloading_file_downloaded.setText(Config.getInstance()
										.getText("Downloaded: "
												+ new DecimalFormat("##.##")
														.format(bytes_downloaded / (Math.pow(1024, 2)))
												+ "MB (" + dl_progress + "%)"));
								holder.pgr_downloading.setProgress((int) dl_progress);

							}
						});

						cursor.close();
					}

				}
			}).start();
		}

		return convertView;
	}

	public class ViewHolder {
		TextView tv_downloading_file_name, tv_downloading_file_size, tv_downloading_file_downloaded;
		ProgressBar pgr_downloading;
		Button btn_cancel;
	}
}
