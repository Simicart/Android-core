package com.simicart.core.catalog.product.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;

public class OptionFragment extends SimiFragment {

	public static OptionFragment newInstance(View view, OnClickListener listener) {
		OptionFragment fragment = new OptionFragment();
		fragment.view = view;
		fragment.listener = listener;
		return fragment;
	}

	View mRootView;
	View view;
	OnClickListener listener;
	FrameLayout frameLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(
				Rconfig.getInstance().layout("core_option_detail_layout"),
				container, false);
		// final Context context = getActivity();

		// frameLayout = (FrameLayout) mRootView.findViewById(Rconfig
		// .getInstance().id("container_fragment"));
		// frameLayout.addView(this.view);
		RelativeLayout relativeLayout = (RelativeLayout) mRootView
				.findViewById(Rconfig.getInstance().id("rlt_core_option"));
		relativeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SimiManager.getIntance().getManager().popBackStack();
			}
		});

		TextView tv_cancel = (TextView) mRootView.findViewById(Rconfig
				.getInstance().id("tv_cancel"));
		tv_cancel.setText(Config.getInstance().getText("Cancel"));

		TextView tv_done = (TextView) mRootView.findViewById(Rconfig
				.getInstance().id("tv_done"));
		tv_done.setText(Config.getInstance().getText("Done"));

		ScrollView scroll_view = (ScrollView) mRootView.findViewById(Rconfig
				.getInstance().id("scl_body"));
		if (view.getParent() != null)
			((ViewGroup) view.getParent()).removeView(view);
		scroll_view.addView(view);
		Animation animationToTop = AnimationUtils.loadAnimation(SimiManager
				.getIntance().getCurrentContext(),
				Rconfig.getInstance().getId("down_to_top", "anim"));
		animationToTop.setStartOffset(10);
		view.setAnimation(animationToTop);

		tv_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SimiManager.getIntance().getManager().popBackStack();
			}
		});

		// tv_done.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// SimiManager.getIntance().backPreviousFragment();
		//
		// }
		// });
		tv_done.setOnClickListener(listener);

		return mRootView;
	}

}
