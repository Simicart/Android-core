package com.simicart.plugins.download.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.download.adapter.DownloadingAdapter;
import com.simicart.plugins.download.common.DownloadConstant;
import com.simicart.plugins.download.entity.DownloadingEntity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class DownloadingListProductFragment extends SimiFragment {

	public static DownloadingListProductFragment instance;
	protected DownloadingAdapter adapter;
	protected ArrayList<DownloadingEntity> list_downloading;
	protected List<String> downloadingID;
	protected SharedPreferences prefs;
	
	public static DownloadingListProductFragment newInstance() {
		if(instance == null) {
			instance = new DownloadingListProductFragment();
		}
		return instance;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(Rconfig.getInstance().layout("plugins_downloading_product_layout"), container, false);
		ListView list = (ListView) v.findViewById(Rconfig.getInstance().id("list_downloading"));
		list_downloading = new ArrayList<>();
		prefs = getActivity().getSharedPreferences(DownloadConstant.PREF, Context.MODE_PRIVATE);
		String serialized = prefs.getString(DownloadConstant.TASKS, null);
		if (serialized != null) {
			downloadingID = new LinkedList<String>(Arrays.asList(TextUtils.split(serialized, ",")));
		} else
			downloadingID = new ArrayList<>();
		if(downloadingID.size() > 0) {
			for(int i=0;i<downloadingID.size();i++) {
				String downloadID = downloadingID.get(i).substring(0, downloadingID.get(i).indexOf("Simi"));
				list_downloading.add(new DownloadingEntity(downloadID));
			}
		}
		adapter = new DownloadingAdapter(getActivity(), null, list_downloading);
		list.setAdapter(adapter);
		
		return v;
	}

}
