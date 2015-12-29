package com.simicart.plugins.download.fragment;

import java.io.File;
import java.util.ArrayList;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.magestore.simicart.R;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.download.adapter.DownloadedListSwipeAdapter;

public class DownloadedListProductFragment extends SimiFragment {

	public static DownloadedListProductFragment instance;
	private ListView list_downloaded;
	private Context mContext;
	private ArrayList<File> list_file;
	private DownloadedListSwipeAdapter adapter;

	public static DownloadedListProductFragment newInstance() {
		if (instance == null)
			instance = new DownloadedListProductFragment();
		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater
				.inflate(
						Rconfig.getInstance().layout(
								"plugins_download_product_layout"), container,
						false);

		mContext = getActivity();

		list_downloaded = (ListView) rootView.findViewById(Rconfig
				.getInstance().id("list_download"));
		list_file = new ArrayList<>();
		File fileList = new File("/sdcard/"
				+ getActivity().getResources().getString(R.string.app_name));
		if (fileList != null && fileList.listFiles() != null) {
			File[] filenames = fileList.listFiles();
			if (filenames.length > 0)
				for (File tmpf : filenames) {
					if (tmpf.getName().equals("download.txt"))
						continue;
					list_file.add(tmpf);
				}
		}

		adapter = new DownloadedListSwipeAdapter(getActivity(), list_file);
		list_downloaded.setAdapter(adapter);

		list_downloaded.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.e("Downloaded list", "item clicked");
				File item = list_file.get(position);
				String filename = item.getName();
				MimeTypeMap myMime = MimeTypeMap.getSingleton();
				Intent newIntent = new Intent(Intent.ACTION_VIEW);
				String mimeType = myMime.getMimeTypeFromExtension(fileExt(
						filename).substring(1));
				newIntent.setDataAndType(Uri.fromFile(item), mimeType);
				newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				try {
					mContext.startActivity(newIntent);
				} catch (ActivityNotFoundException e) {
					Toast.makeText(
							mContext,
							"No existing apps support viewing/opening this file.",
							Toast.LENGTH_LONG).show();
				}
			}
		});

		// adapter2.getSwipeLayoutResourceId(position);

		return rootView;
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
}
