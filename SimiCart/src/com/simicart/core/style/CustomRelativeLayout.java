package com.simicart.core.style;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class CustomRelativeLayout extends RelativeLayout {

	private float scale = 1.0f;

	public CustomRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CustomRelativeLayout(Context context) {
		super(context);
	}

	public void setScaleBoth(float scale) {
		this.scale = scale;
		this.invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int w = this.getWidth();
		int h = this.getHeight();
		canvas.scale(scale, scale, w / 2, h / 2);

		super.onDraw(canvas);
	}
}
