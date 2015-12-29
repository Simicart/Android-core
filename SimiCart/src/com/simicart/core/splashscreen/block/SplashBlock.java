package com.simicart.core.splashscreen.block;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;

public class SplashBlock {

	protected View rootView;
	protected Context mContext;

	public SplashBlock(View view, Context context) {
		rootView = view;
		mContext = context;
	}

	@SuppressLint("DefaultLocale")
	public void initView() {
		rootView.setBackgroundColor(Config.getInstance().getColorSplash());

		// label demo
		String textdemo = "This is a demo version.<br/>This text will be removed from live app.";
		TextView tv_demo = (TextView) rootView.findViewById(Rconfig
				.getInstance().getId(mContext, "core_splash_screen_tv_demo",
						"id"));
		tv_demo.setText(Html.fromHtml(textdemo));
		tv_demo.setTextColor(Config.getInstance().getColorMain());
		if (Config.getInstance().getDemoEnable().equals("DEMO_ENABLE")
				|| Config.getInstance().getDemoEnable().toUpperCase()
						.equals("YES")) {
			tv_demo.setVisibility(View.VISIBLE);
		} else {
			tv_demo.setVisibility(View.VISIBLE);
			tv_demo.setText("");
		}

		// show loading screen
		ProgressBar prg_loading = (ProgressBar) rootView.findViewById(Rconfig
				.getInstance().getId(mContext,
						"core_splash_screen_prg_loading_screen", "id"));
		prg_loading.setVisibility(View.VISIBLE);

		// images for logo
		ImageView img_logo = (ImageView) rootView.findViewById(Rconfig
				.getInstance().getId(mContext, "core_splash_screen_img_logo",
						"id"));
		img_logo.setImageResource(Rconfig.getInstance().getId(mContext,
				"default_splash_screen", "drawable"));
		img_logo.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

	}

}
