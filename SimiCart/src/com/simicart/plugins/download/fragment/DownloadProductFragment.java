package com.simicart.plugins.download.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.download.block.DownloadBlock;
import com.simicart.plugins.download.controller.DownloadController;

public class DownloadProductFragment extends SimiFragment {
	protected DownloadBlock mBlock;
	protected DownloadController mController;
	
	public static DownloadProductFragment newInstance() {
		DownloadProductFragment fragment = new DownloadProductFragment();
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				Rconfig.getInstance().layout("plugins_download_product_layout"),
				container, false);
		Context context = getActivity();
		mBlock = new DownloadBlock(view, context);
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
