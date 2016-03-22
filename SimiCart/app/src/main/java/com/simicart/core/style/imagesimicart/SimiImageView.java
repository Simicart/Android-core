package com.simicart.core.style.imagesimicart;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.magestore.simicart.R;

public class SimiImageView extends RelativeLayout {

	protected RelativeLayout view;
	protected ImageView imageView;
	protected ProgressBar progressBar;

	public SimiImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		createView(context);
	}

	public SimiImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		createView(context);
	}

	public SimiImageView(Context context) {
		super(context);
		createView(context);
	}

	public void createView(Context context) {
		view = (RelativeLayout) LayoutInflater.from(context).inflate(
				R.layout.core_simi_image_view, this, true);

		imageView = (ImageView) view.findViewById(R.id.image_view);
		progressBar = (ProgressBar) view.findViewById(R.id.loading_bar);
	}

	public void setImageResource(int resId) {
		imageView.setImageResource(resId);
		progressBar.setVisibility(View.GONE);
	}

	public void setImageBitmap(Bitmap bm) {
		imageView.setImageBitmap(bm);
		progressBar.setVisibility(View.GONE);
	}

	public void setScaleType(ScaleType scaleType) {
		imageView.setScaleType(scaleType);
	}

}
