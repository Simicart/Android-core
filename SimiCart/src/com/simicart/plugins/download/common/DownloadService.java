package com.simicart.plugins.download.common;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

public class DownloadService extends Service {

	protected static final String TAG = "DownloadService";
	protected DownloadManager manager;
	protected List<String> currentTasks;
	protected int pos = -1;
	protected BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.e("Download receiver", "onReceive");
			
			Long downloadID = intent.getLongExtra(
					DownloadManager.EXTRA_DOWNLOAD_ID, -1);

			SharedPreferences prefs = getSharedPreferences(
					DownloadConstant.PREF, Context.MODE_PRIVATE);
			String serialized = prefs.getString(DownloadConstant.TASKS, null);
			Log.e(TAG, "++" + serialized);
			if (serialized != null) {
				currentTasks = new LinkedList<String>(Arrays.asList(TextUtils
						.split(serialized, ",")));
				for (int i = 0; i < currentTasks.size(); i++) {
					String s = currentTasks.get(i);
					if (s.substring(0,
							s.indexOf(DownloadConstant.DOWNLOAD_ACTION))
							.equals(downloadID.toString())) {
						pos = i;
						break;
					}
				}
			}

			DownloadManager.Query query = new DownloadManager.Query();
			query.setFilterById(downloadID);
			Cursor cursor = manager.query(query);
			if(!cursor.moveToFirst())
				return;
			int columnIndex = cursor
					.getColumnIndex(DownloadManager.COLUMN_STATUS);
			int status = cursor.getInt(columnIndex);
			Log.e("Download receiver", ":" + status);
			Intent i = new Intent();
			switch (status) {
			case DownloadManager.STATUS_FAILED:
				if (findDownloadID(downloadID.toString()) != -1) {
					i.setAction(DownloadConstant.DOWNLOAD_ACTION);
					String orderID = currentTasks.get(findDownloadID(downloadID
							.toString()));
					orderID = orderID
							.substring(
									orderID.indexOf(DownloadConstant.DOWNLOAD_ACTION) + 4,
									orderID.length());
					i.putExtra(DownloadConstant.STATUS, "Failed_" + orderID);
					sendBroadcast(i);
				}
				break;
			case DownloadManager.STATUS_SUCCESSFUL:
				Log.e("Download receiver",
						":" + findDownloadID(downloadID.toString()));
				if (findDownloadID(downloadID.toString()) != -1) {
					i.setAction(DownloadConstant.DOWNLOAD_ACTION);
					String orderID = currentTasks.get(findDownloadID(downloadID
							.toString()));
					orderID = orderID
							.substring(
									orderID.indexOf(DownloadConstant.DOWNLOAD_ACTION) + 4,
									orderID.length());
					Log.e("Download receiver", ":" + orderID);
					i.putExtra(DownloadConstant.STATUS, "Success_" + orderID);
					sendBroadcast(i);
				}
				break;

			default:
				break;
			}
			currentTasks.remove(pos);
			Editor editor = prefs.edit();
			editor.putString(DownloadConstant.TASKS,
					TextUtils.join(",", currentTasks));
			editor.commit();
			Log.e(TAG, currentTasks.toString());
		}
	};

	public int findDownloadID(String id) {
		int pos = -1;
		if (currentTasks != null && currentTasks.size() > 0) {
			for (int i = 0; i < currentTasks.size(); i++) {
				String s = currentTasks.get(i);
				if (s.substring(0, s.indexOf(DownloadConstant.DOWNLOAD_ACTION))
						.equals(id.toString())) {
					pos = i;
					break;
				}
			}
		}
		return pos;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Log.e(TAG, "onBind");
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.e(TAG, "onCreate");

		manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.e(TAG, "onDestroy");

		unregisterReceiver(downloadReceiver);

		super.onDestroy();
	}

	@Deprecated
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		Log.e(TAG, "onStart");

		// super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.e(TAG, "onStartCommand");

		IntentFilter filter = new IntentFilter(
				DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		registerReceiver(downloadReceiver, filter);

		return Service.START_STICKY;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		Log.e(TAG, "onUnbind");
		return super.onUnbind(intent);
	}
}
