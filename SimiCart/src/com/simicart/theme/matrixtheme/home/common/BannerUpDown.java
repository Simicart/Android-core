package com.simicart.theme.matrixtheme.home.common;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ViewFlipper;

import com.simicart.core.config.Rconfig;


public class BannerUpDown {

	float lastX;
	ViewFlipper mBannerFlipper;
	Context mContext;

	public BannerUpDown(Context context, ViewFlipper bannerFlipper, int time) {
		mBannerFlipper = bannerFlipper;
		mBannerFlipper.setFlipInterval(time);
		mContext = context;
		mBannerFlipper.setInAnimation(context,
				Rconfig.getInstance().getId("in_from_down", "anim"));
		mBannerFlipper.setOutAnimation(context,
				Rconfig.getInstance().getId("out_to_up", "anim"));
		mBannerFlipper.startFlipping();
	}

	public void onTouchEvent(String url, View bannerView) {
		final String url_ad = url;
		bannerView.setOnTouchListener(new OnTouchListener() {
			private float lastY;

			@Override
			public boolean onTouch(View v, MotionEvent touchevent) {
				switch (touchevent.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					lastX = touchevent.getX();
					lastY = touchevent.getY();
					break;
				}

				}
				return true;
			}
		});
	}
}
