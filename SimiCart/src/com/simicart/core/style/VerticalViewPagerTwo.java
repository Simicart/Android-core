package com.simicart.core.style;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

@SuppressLint("NewApi")
public class VerticalViewPagerTwo extends ViewPager {

	public VerticalViewPagerTwo(Context context) {
		super(context);
	}

	public VerticalViewPagerTwo(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(heightMeasureSpec, widthMeasureSpec);
	}

}
