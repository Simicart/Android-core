package com.simicart.plugins.download.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.download.block.DownloadBlockTablet;
import com.simicart.plugins.download.controller.DownloadController;

public class DownloadProductFragmentTablet extends SimiFragment {
	protected DownloadBlockTablet mBlock;
	protected DownloadController mController;
	
	public static DownloadProductFragmentTablet newInstance() {
		DownloadProductFragmentTablet fragment = new DownloadProductFragmentTablet();
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				Rconfig.getInstance().layout("plugins_download_product"),
				container, false);
		Context context = getActivity();
		mBlock = new DownloadBlockTablet(view, context);
		mBlock.initView();
		
		if(mController == null){
			mController = new DownloadController();
			mController.setDelegate(mBlock);
			mController.onStart();
		}else{
			mController.setDelegate(mBlock);
			mController.onResume();
		}
		return view;
	}
}
