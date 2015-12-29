package com.simicart.plugins.download.adapter;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import android.app.ActivityManager;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.magestore.simicart.R;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.fragment.OrderHistoryDetailFragment;
import com.simicart.core.material.ButtonRectangle;
import com.simicart.plugins.download.common.DownloadConstant;
import com.simicart.plugins.download.common.DownloadService;
import com.simicart.plugins.download.entity.DownloadEntity;

public class DownloadAdapter extends BaseAdapter {

	protected ArrayList<DownloadEntity> list_download;
	protected Context context;
	protected LayoutInflater inflater;
	protected DownloadTask downloadTask = null;
	protected DownloadManager manager;
	protected SharedPreferences prefs;
	ArrayList<File> listFile;
	protected List<String> downloadingID;
	protected HashMap<String, ViewHolder> downloading_view;
	protected BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String status = intent.getStringExtra(DownloadConstant.STATUS);
			Log.e("Download Adapter", "Download status: " + status);
			if (status != null) {
				String[] raw = status.split("_");
				Log.e("Download Adapter", ": " + raw[0]);
				Log.e("Download Adapter", ": " + raw[1]);
				if (raw[0].equals("Success")) {
					if (downloading_view.containsKey(raw[1])) {
						downloading_view.get(raw[1]).bt_download
								.setText("Re-download");
						downloading_view.get(raw[1]).bt_download
								.setBackgroundColor(Config.getInstance()
										.getColorMain());
						downloading_view.get(raw[1]).bt_download
								.setClickable(true);
						downloading_view.get(raw[1]).bt_download
								.setActivated(true);
						downloading_view.remove(raw[1]);
						/*
						 * downloading_button.get(raw[1]).bt_open.setVisibility(View
						 * .VISIBLE);
						 * downloading_button.get(raw[1]).bt_open.setText
						 * (Config. getInstance().getText("Open"));
						 * downloading_button
						 * .get(raw[1]).bt_open.setBackgroundColor
						 * (Config.getInstance().getColorMain());
						 */
					}
				} else if (raw[0].equals("Failed")) {
					if (downloading_view.containsKey(raw[1])) {
						downloading_view.get(raw[1]).bt_download
								.setText("Download");
						downloading_view.get(raw[1]).bt_download
								.setBackgroundColor(Config.getInstance()
										.getColorMain());
						downloading_view.get(raw[1]).bt_download
								.setClickable(true);
						downloading_view.get(raw[1]).bt_download
								.setActivated(true);
						downloading_view.remove(raw[1]);
					}
				}

				String serialized = prefs.getString(DownloadConstant.TASKS,
						null);
				if (serialized != null) {
					downloadingID = new LinkedList<String>(
							Arrays.asList(TextUtils.split(serialized, ",")));
				} else
					downloadingID = new ArrayList<>();
			}
		}
	};

	public DownloadAdapter(Context context,
			ArrayList<DownloadEntity> list_download) {
		this.context = context;
		this.list_download = list_download;
		this.inflater = LayoutInflater.from(context);
		downloading_view = new HashMap<>();
		this.prefs = context.getSharedPreferences(DownloadConstant.PREF,
				Context.MODE_PRIVATE);
		String serialized = prefs.getString(DownloadConstant.TASKS, null);
		if (serialized != null) {
			downloadingID = new LinkedList<String>(Arrays.asList(TextUtils
					.split(serialized, ",")));
		} else
			downloadingID = new ArrayList<>();
		if (!isMyServiceRunning(DownloadService.class)) {
			Log.e("DownloadAdapter", "Service start now");
			Intent service = new Intent(context, DownloadService.class);
			context.startService(service);
		} else {
			Log.e("DownloadAdapter", "Service started");
		}
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(DownloadConstant.DOWNLOAD_ACTION);
		context.registerReceiver(receiver, intentFilter);
	}

	public void setListDownload(ArrayList<DownloadEntity> list_download) {
		this.list_download = list_download;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_download.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list_download.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	private boolean isMyServiceRunning(Class<?> serviceClass) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceClass.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	protected boolean isDownloading(String id) {
		for (int i = 0; i < downloadingID.size(); i++) {
			String s = downloadingID.get(i);
			Log.e("isDownloading",
					s.substring(
							s.indexOf(DownloadConstant.DOWNLOAD_ACTION) + 4,
							s.length()));
			if (s.substring(s.indexOf(DownloadConstant.DOWNLOAD_ACTION) + 4,
					s.length()).equals(id.toString())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		Log.e("DownloadAdapter", "getView() " + downloading_view.size());
		manager = (DownloadManager) context
				.getSystemService(Context.DOWNLOAD_SERVICE);
		listFile = getListFile();
		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = this.inflater.inflate(
					Rconfig.getInstance().layout(
							"plugins_download_product_item_layout"), null);
			viewHolder = new ViewHolder();
			viewHolder.txt_order_name = (TextView) convertView
					.findViewById(Rconfig.getInstance().id("txt_order_name"));
			viewHolder.order_name = (TextView) convertView.findViewById(Rconfig
					.getInstance().id("order_name"));
			viewHolder.txt_order_date = (TextView) convertView
					.findViewById(Rconfig.getInstance().id("txt_order_date"));
			viewHolder.order_date = (TextView) convertView.findViewById(Rconfig
					.getInstance().id("order_date"));
			viewHolder.txt_order_status = (TextView) convertView
					.findViewById(Rconfig.getInstance().id("txt_order_status"));
			viewHolder.order_status = (TextView) convertView
					.findViewById(Rconfig.getInstance().id("order_status"));
			viewHolder.txt_order_remain = (TextView) convertView
					.findViewById(Rconfig.getInstance().id("txt_order_remain"));
			viewHolder.order_remain = (TextView) convertView
					.findViewById(Rconfig.getInstance().id("order_remain"));
			viewHolder.bt_download = (ButtonRectangle) convertView
					.findViewById(Rconfig.getInstance().id("bt_download"));
			viewHolder.bt_open = (ButtonRectangle) convertView
					.findViewById(Rconfig.getInstance().id("bt_open_file"));
			viewHolder.bt_order = (Button) convertView.findViewById(Rconfig
					.getInstance().id("bt_order"));
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final DownloadEntity item = this.list_download.get(position);
		viewHolder.txt_order_name.setText(Config.getInstance().getText(
				"Product Name"));
		viewHolder.order_name.setText(item.getOrderName());
		viewHolder.txt_order_date.setText(Config.getInstance().getText(
				"Order Date"));
		viewHolder.order_date.setText(item.getOrderDate());
		viewHolder.txt_order_status.setText(Config.getInstance().getText(
				"Order Status"));
		viewHolder.order_status.setText(item.getOrderStatus());
		viewHolder.txt_order_remain.setText(Config.getInstance().getText(
				"Order Remain"));
		viewHolder.order_remain.setText(item.getOrderRemain());
		viewHolder.bt_download.setTextSize(14);
		viewHolder.bt_open.setTextSize(14);
		if (listFile.size() > 0) {
			if(isDownloading(item.getOrderID()) == true) {
				
				viewHolder.bt_download.setText(Config.getInstance().getText(
						"Downloading"));
				viewHolder.bt_download.setBackgroundColor(Color.GRAY);
				viewHolder.bt_download.setClickable(false);
				viewHolder.bt_download.setActivated(false);
				
				if (downloading_view.containsKey(item.getOrderID())) {
					downloading_view.remove(item.getOrderID());
					downloading_view.put(item.getOrderID(), viewHolder);
				} else if (downloading_view.size() == 0) {
					downloading_view.put(item.getOrderID(), viewHolder);
				}
				
				if(isDownloaded(item.getOrderFile(), getListFile()) <= 1) {
					if(viewHolder.bt_open.getVisibility() != View.VISIBLE)
						viewHolder.bt_open.setVisibility(View.GONE);
					if(viewHolder.bt_open.getVisibility() == View.VISIBLE) {
						viewHolder.bt_open.setVisibility(View.VISIBLE);
						viewHolder.bt_open.setText(Config.getInstance().getText(
								"Open file"));
						viewHolder.bt_open.setBackgroundColor(Config.getInstance()
								.getColorMain());
					}
					
				} else if(isDownloaded(item.getOrderFile(), getListFile()) > 1) {
					viewHolder.bt_open.setVisibility(View.VISIBLE);
					viewHolder.bt_open.setText(Config.getInstance().getText(
							"Open file"));
					viewHolder.bt_open.setBackgroundColor(Config.getInstance()
							.getColorMain());
				}
			} else if(isDownloading(item.getOrderID()) == false) {
				if(isDownloaded(item.getOrderFile(), getListFile()) == 0) {
					viewHolder.bt_download.setText(Config.getInstance().getText(
							"Download"));
					viewHolder.bt_download.setBackgroundColor(Config.getInstance()
							.getColorMain());
					viewHolder.bt_download.setClickable(true);
					viewHolder.bt_download.setActivated(true);
					viewHolder.bt_open.setVisibility(View.GONE);
				} else if(isDownloaded(item.getOrderFile(), getListFile()) > 0) {
					viewHolder.bt_download.setText(Config.getInstance().getText(
							"Re-download"));
					viewHolder.bt_download.setBackgroundColor(Config.getInstance()
							.getColorMain());
					viewHolder.bt_download.setClickable(true);
					viewHolder.bt_download.setActivated(true);
					viewHolder.bt_open.setVisibility(View.VISIBLE);
					viewHolder.bt_open.setText(Config.getInstance().getText(
							"Open file"));
					viewHolder.bt_open.setBackgroundColor(Config.getInstance()
							.getColorMain());
				}
			}
			/*if (isDownloaded(item.getOrderFile(), listFile) == true
					&& isDownloading(item.getOrderID()) == false) {
				viewHolder.bt_download.setText(Config.getInstance().getText(
						"Re-download"));
				viewHolder.bt_download.setBackgroundColor(Config.getInstance()
						.getColorMain());
				viewHolder.bt_download.setClickable(true);
				viewHolder.bt_download.setActivated(true);
				viewHolder.bt_open.setVisibility(View.VISIBLE);
				viewHolder.bt_open.setText(Config.getInstance().getText(
						"Open file"));
				viewHolder.bt_open.setBackgroundColor(Config.getInstance()
						.getColorMain());
			} else if (isDownloaded(item.getOrderFile(), listFile) == true
					&& isDownloading(item.getOrderID()) == true) {
				if (viewHolder.bt_open.getVisibility() == View.VISIBLE) {
					viewHolder.bt_open.setVisibility(View.VISIBLE);
					viewHolder.bt_open.setText(Config.getInstance().getText(
							"Open file"));
					viewHolder.bt_open.setBackgroundColor(Config.getInstance()
							.getColorMain());
				} else {
					viewHolder.bt_open.setVisibility(View.GONE);
				}
				viewHolder.bt_download.setText(Config.getInstance().getText(
						"Downloading"));
				viewHolder.bt_download.setBackgroundColor(Color.GRAY);
				viewHolder.bt_download.setClickable(false);
				viewHolder.bt_download.setActivated(false);
				if (downloading_view.containsKey(item.getOrderID())) {
					downloading_view.remove(item.getOrderID());
					downloading_view.put(item.getOrderID(), viewHolder);
				} else if (downloading_view.size() == 0) {
					downloading_view.put(item.getOrderID(), viewHolder);
				}
			} else if (isDownloaded(item.getOrderFile(), listFile) == false
					&& isDownloading(item.getOrderID()) == true) {
				viewHolder.bt_download.setText(Config.getInstance().getText(
						"Downloading"));
				viewHolder.bt_download.setBackgroundColor(Color.GRAY);
				viewHolder.bt_download.setClickable(false);
				viewHolder.bt_download.setActivated(false);
				viewHolder.bt_open.setVisibility(View.GONE);
			} else {
				viewHolder.bt_download.setText(Config.getInstance().getText(
						"Download"));
				viewHolder.bt_download.setBackgroundColor(Config.getInstance()
						.getColorMain());
				viewHolder.bt_download.setClickable(true);
				viewHolder.bt_download.setActivated(true);
				viewHolder.bt_open.setVisibility(View.GONE);
			}*/
		} else {
			if(isDownloading(item.getOrderID()) == true) {
				viewHolder.bt_download.setText(Config.getInstance().getText(
						"Downloading"));
				viewHolder.bt_download.setBackgroundColor(Color.GRAY);
				viewHolder.bt_download.setClickable(false);
				viewHolder.bt_download.setActivated(false);
			} else {
				viewHolder.bt_download.setText(Config.getInstance().getText(
						"Download"));
				viewHolder.bt_download.setBackgroundColor(Config.getInstance()
						.getColorMain());
				viewHolder.bt_download.setClickable(true);
				viewHolder.bt_download.setActivated(true);
				viewHolder.bt_open.setVisibility(View.GONE);
			}
		}

		viewHolder.bt_download.setTextColor(Color.WHITE);
		viewHolder.bt_download.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (viewHolder.bt_download.getText().equals("Downloading")) {

				} else {
					if (item.getOrderStatus().equals("expired")) {
						SimiManager.getIntance().showToast(
								Config.getInstance().getText(
										"The link is expired")
										+ ".");
					} else if (item.getOrderStatus().equals("pending")) {
						SimiManager.getIntance().showToast(
								Config.getInstance().getText(
										"The link is not available")
										+ ".");
					} else {
						downloading_view.put(item.getOrderID(), viewHolder);
						viewHolder.bt_download.setText(Config.getInstance()
								.getText("Downloading"));
						viewHolder.bt_download.setBackgroundColor(Color.GRAY);
						viewHolder.bt_download.setClickable(false);
						viewHolder.bt_download.setActivated(false);

						Intent i = new Intent();
						i.setAction(DownloadConstant.DOWNLOADING_ACTION);
						context.sendBroadcast(i);

						onDownloadProduct(item);
						Log.e("download button", "++" + downloading_view.size());
					}
				}
			}
		});

		viewHolder.bt_open.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listFile = getListFile();
				if (listFile.size() > 0) {
					File file = null;
					for (int i = 0; i < listFile.size(); i++) {
						file = listFile.get(i);
						if (file.getName().equals(item.getOrderFile()))
							break;
					}
					if (file != null) {
						String filename = file.getName();
						MimeTypeMap myMime = MimeTypeMap.getSingleton();
						Intent newIntent = new Intent(Intent.ACTION_VIEW);
						String mimeType = myMime
								.getMimeTypeFromExtension(fileExt(filename)
										.substring(1));
						newIntent.setDataAndType(Uri.fromFile(file), mimeType);
						newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						try {
							context.startActivity(newIntent);
						} catch (ActivityNotFoundException e) {
							SimiManager
									.getIntance()
									.showToast(
											Config.getInstance()
													.getText(
															"No existing apps support viewing/opening this file"));
						}
					}
				}
			}
		});

		viewHolder.bt_order.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String id = item.getOrderID();
				OrderHistoryDetailFragment fragment = OrderHistoryDetailFragment
						.newInstance();
				fragment.setID(id);
				SimiManager.getIntance().replaceFragment(fragment);

			}
		});

		return convertView;
	}

	public class ViewHolder {
		TextView txt_order_name, order_name, txt_order_date, order_date,
				txt_order_status, order_status, txt_order_remain, order_remain;
		ButtonRectangle bt_download, bt_open;
		Button bt_order;
	}

	private String fileExt(String url) {
		if (url.indexOf("?") > -1) {
			url = url.substring(0, url.indexOf("?"));
		}
		if (url.lastIndexOf(".") == -1) {
			return null;
		} else {
			String ext = url.substring(url.lastIndexOf("."));
			if (ext.indexOf("%") > -1) {
				ext = ext.substring(0, ext.indexOf("%"));
			}
			if (ext.indexOf("/") > -1) {
				ext = ext.substring(0, ext.indexOf("/"));
			}
			return ext.toLowerCase();

		}
	}

	protected ArrayList<File> getListFile() {
		ArrayList<File> list_file = new ArrayList<>();
		File fileList = new File("/sdcard/"
				+ context.getResources().getString(R.string.app_name));
		if (fileList != null && fileList.listFiles() != null) {
			File[] filenames = fileList.listFiles();
			if (filenames.length > 0)
				for (File tmpf : filenames) {
					list_file.add(tmpf);
				}
		}
		return list_file;
	}

	protected int isDownloaded(String name, ArrayList<File> listID) {
		int num = 0;
		if(name.length() > 0) {
			name = name.substring(0, name.indexOf("."));
			Log.e("isDownloaded", name);
			for (int i = 0; i < listID.size(); i++) {
				if (listID.get(i).getName().contains(name))
					num++;
			}
		}
		Log.e("isDownloaded", "" + num);
		return num;
	}

	protected void onDownloadProduct(DownloadEntity item) {
		if (item.getOrderLink() != null && !item.getOrderLink().equals("null")
				&& !item.getOrderLink().equals("")
				&& URLUtil.isValidUrl(item.getOrderLink())) {

			String url = Uri.parse(
					item.getOrderLink()
							+ "email/"
							+ Base64.encodeToString(DataLocal.getEmail()
									.getBytes(), Base64.DEFAULT)).toString();
			DownloadManager.Request request = new Request(Uri.parse(url));
			request.setDescription(Config.getInstance().getText(
					"Download is processing"));
			request.setTitle(item.getOrderName());
			request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
					| DownloadManager.Request.NETWORK_MOBILE);
			request.setVisibleInDownloadsUi(true);
			request.allowScanningByMediaScanner();
			request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
			String fileName = "";
			try {
				fileName = new DownloadTask().execute(url).get();
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			request.addRequestHeader("Cookie", "store=default; "
					+ DownloadConstant.COOKIE);
			request.setDestinationInExternalPublicDir(context.getResources()
					.getString(R.string.app_name), fileName);
			request.setAllowedOverRoaming(false);
			final Long downloadId = manager.enqueue(request);
			downloadingID.add(downloadId.toString()
					+ DownloadConstant.DOWNLOAD_ACTION + item.getOrderID());

			Editor editor = prefs.edit();
			editor.putString(DownloadConstant.TASKS,
					TextUtils.join(",", downloadingID));
			editor.commit();
		}
	}

	private class DownloadTask extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... sUrl) {
			HttpURLConnection connection = null;
			String fileName = "";
			try {
				URL url = new URL(sUrl[0]);
				connection = (HttpURLConnection) url.openConnection();
				connection.connect();

				if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
					return Config.getInstance().getText("Connection error");
				}
				String content = connection
						.getHeaderField("Content-Disposition");
				Map<String, List<String>> headerFields = connection
						.getHeaderFields();
				List<String> cookiesHeader = headerFields.get("Set-Cookie");
				if (cookiesHeader.size() > 0) {
					DownloadConstant.COOKIE = cookiesHeader.get(0);
					DownloadConstant.COOKIE = DownloadConstant.COOKIE
							.substring(0, DownloadConstant.COOKIE
									.indexOf("; expires"));
					Log.e("Download Adapter", "Cookies: "
							+ DownloadConstant.COOKIE);
				}
				int index = content.indexOf("filename=");
				Log.e("FileName", content.substring(index + 9));
				if (content != null && content.indexOf("=") != -1) {
					fileName = content.substring(index + 9);
				} else {
					return Config.getInstance().getText("File not found");
				}

				File mediaStorageDir = new File("/sdcard/", context
						.getResources().getString(R.string.app_name));
				if (!mediaStorageDir.exists()) {
					if (!mediaStorageDir.mkdirs()) {
						return null;
					}
				}
			} catch (Exception e) {
			} finally {
				if (connection != null)
					connection.disconnect();
			}
			return fileName;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
		}

		@Override
		protected void onPostExecute(String result) {
		}
	}
}
