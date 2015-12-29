package com.simicart.plugins.download.fragment;

import java.io.File;
import java.util.ArrayList;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.magestore.simicart.R;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.download.adapter.DownloadedListSwipeAdapter;

public class DownloadedListProductFragmentTablet extends SimiFragment {

	public static DownloadedListProductFragmentTablet instance;
	private GridView list_downloaded;
	private ArrayList<File> list_file;
	private DownloadedListSwipeAdapter adapter;

	public static DownloadedListProductFragmentTablet newInstance() {
		if (instance == null)
			instance = new DownloadedListProductFragmentTablet();
		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.e("DownloadedListProductFragmentTablet", "onCreateView");
		rootView = inflater.inflate(
				Rconfig.getInstance().layout("plugins_download_product"),
				container, false);

		list_downloaded = (GridView) rootView.findViewById(Rconfig
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
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent baseIntent = new Intent(Intent.ACTION_VIEW);
				baseIntent.setData(Uri.parse("/sdcard/"
						+ getActivity().getResources().getString(
								R.string.app_name)));

				Intent chooserIntent = Intent.createChooser(baseIntent,
						"Select Application");
			}
		});

		return rootView;
	}
}
