package com.simicart.core.catalog.product.delegate;

import android.view.View;
import android.widget.LinearLayout;

import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.style.VerticalViewPager2;

public interface ProductDelegate extends SimiDelegate {

	public void onUpdateOptionView(View view);

	public void onUpdatePriceView(View view);

	public String[] getImage();

	public boolean isShown();

	public void onVisibleTopBottom(boolean isVisible);

	public void updateViewPager(VerticalViewPager2 viewpager);
	
	public LinearLayout getLayoutMore ();

}