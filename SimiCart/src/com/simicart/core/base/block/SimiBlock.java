package com.simicart.core.base.block;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;
import com.simicart.core.event.block.CacheBlock;
import com.simicart.core.event.block.EventBlock;

public class SimiBlock implements SimiDelegate {
	protected View mView;
	protected Context mContext;
	protected View mImageView;
	protected ProgressDialog pd_loading;
	protected ViewGroup viewGroup;

	public SimiBlock(View view, Context context) {
		mView = view;

		mView.setBackgroundColor(Config.getInstance().getApp_backrground());

		mContext = context;
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		viewGroup = ((ViewGroup) mView);
		mImageView = inflater.inflate(
				Rconfig.getInstance().layout("core_base_loading"), viewGroup,
				false);

		pd_loading = ProgressDialog.show(mContext, null, null, true, false);
		pd_loading.setContentView(Rconfig.getInstance().layout(
				"core_base_loading"));
		pd_loading.setCanceledOnTouchOutside(false);
		pd_loading.setCancelable(false);
		pd_loading.dismiss();
	}

	public SimiBlock() {

	}

	public View getView() {
		return mView;
	}

	public void initView() {

	}

	@Override
	public void showLoading() {
		for (int i = 0; i < viewGroup.getChildCount(); i++) {
			viewGroup.getChildAt(i).setVisibility(View.GONE);
		}
		viewGroup.addView(mImageView);
	}

	@Override
	public void dismissLoading() {
		viewGroup.removeView(mImageView);
		for (int i = 0; i < viewGroup.getChildCount(); i++) {
			View view = viewGroup.getChildAt(i);
			view.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void updateView(SimiCollection collection) {
		this.drawView(collection);
		this.event(this.getClass().getName(), collection);
	}

	public void event(String name, SimiCollection collection) {
		CacheBlock cacheBlock = new CacheBlock();
		cacheBlock.setBlock(this);
		cacheBlock.setView(mView);
		cacheBlock.setContext(mContext);
		cacheBlock.setSimiCollection(collection);
		EventBlock eventBlock = new EventBlock();
		eventBlock.dispatchEvent(name, cacheBlock);
	}

	public View id(String id) {
		return mView.findViewById(Rconfig.getInstance().id(id));
	}

	/**
	 * 
	 * @override this in child
	 * @param collection
	 */
	public void drawView(SimiCollection collection) {

	}

	@Override
	public void showDialogLoading() {
		pd_loading.show();
	}

	@Override
	public void dismissDialogLoading() {
		pd_loading.dismiss();
	}
}
