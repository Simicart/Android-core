package com.simicart.core.splashscreen.block;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
		/*img_logo.setImageResource(Rconfig.getInstance().getId(mContext,
				"default_splash_screen", "drawable"));*/
		img_logo.setImageBitmap(decodeSampledBitmapFromResource(
				mContext.getResources(),
				Rconfig.getInstance().drawable("default_splash_screen"),
				700, 700));
		img_logo.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

	}

	public Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
			int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	public int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

}
