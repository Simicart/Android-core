package com.simicart.core.catalog.product.delegate;

import java.util.ArrayList;

import com.simicart.core.base.delegate.SimiDelegate;

public interface CustomerReviewDelegate extends SimiDelegate {

	public void onUpdateHeaderView(ArrayList<Integer> mRatingStar);

	public void addFooterView();

	public void removeFooterView();
}
