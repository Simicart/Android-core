package com.simicart.plugins.download.block;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.download.adapter.DownloadAdapter;
import com.simicart.plugins.download.delegate.DownloadDelegate;
import com.simicart.plugins.download.entity.DownloadEntity;

public class DownloadBlock extends SimiBlock implements DownloadDelegate{
	protected ListView mListDownload;
	protected DownloadAdapter mAdapter;
	Context context;
	
	public DownloadBlock(View view, Context context) {
		super(view, context);
		this.context = context;
	}

	@Override
	public void initView() {
		mListDownload = (ListView) mView.findViewById(Rconfig.getInstance().id("list_download"));
	}
	
	@Override
	public void drawView(SimiCollection collection) {
		ArrayList<SimiEntity> entity = collection.getCollection();
		if (null != entity && entity.size() > 0) {
			ArrayList<DownloadEntity> list_download = new ArrayList<DownloadEntity>();
			for (int i = 0; i < entity.size(); i++) {
				SimiEntity simiEntity = entity.get(i);
				DownloadEntity download = (DownloadEntity) simiEntity;
				list_download.add(download);
			}
			//new FileNameAsyncTask(list_download).execute();
			if(list_download.size() > 0){
				if(null == mAdapter){
					mAdapter = new DownloadAdapter(mContext, list_download);
					mListDownload.setAdapter(mAdapter);
				}else{
					mAdapter.setListDownload(list_download);
					mAdapter.notifyDataSetChanged();
				}
			}
		}
	}
	
	public void visiableView(String message) {
		((ViewGroup) mView).removeAllViewsInLayout();
		TextView tv_notify = new TextView(mContext);
		tv_notify.setText(Config.getInstance().getText(message));
		tv_notify.setTypeface(null, Typeface.BOLD);
		if (DataLocal.isTablet) {
			tv_notify.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
		} else {
			tv_notify.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
		}
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		tv_notify.setGravity(Gravity.CENTER);
		tv_notify.setLayoutParams(params);
		((ViewGroup) mView).addView(tv_notify);
	}

	@Override
	public void getMessage(String message) {
		visiableView(message);
	}
}
