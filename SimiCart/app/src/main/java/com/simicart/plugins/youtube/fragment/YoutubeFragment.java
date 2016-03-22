package com.simicart.plugins.youtube.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.Constants;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.youtube.adapter.YoutubeAdapter;
import com.simicart.plugins.youtube.entity.YoutubeEnity;

public class YoutubeFragment extends SimiFragment {
	protected ArrayList<YoutubeEnity> mYoutube;
	protected ListView mListView;
	protected YoutubeAdapter mAdapter;

	public static YoutubeFragment newInstanse(ArrayList<YoutubeEnity> mYoutube) {
		YoutubeFragment fragment = new YoutubeFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable(Constants.KeyData.LIST_YOUTUBE, mYoutube);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater
				.inflate(
						Rconfig.getInstance().layout(
								"plugins_youtube_fragment_layout"), null);
		mListView = (ListView) rootView.findViewById(Rconfig.getInstance().id(
				"youtube_list"));
		
		if(getArguments() != null){
			mYoutube = (ArrayList<YoutubeEnity>) getArguments().getSerializable(Constants.KeyData.LIST_YOUTUBE);
		}
		
		if (mYoutube.size() > 0) {
			mAdapter = new YoutubeAdapter(getActivity(), mYoutube);
			mListView.setAdapter(mAdapter);
			mAdapter.setListYoutube(mYoutube);
			mAdapter.notifyDataSetChanged();
		}
		return rootView;
	}
}
