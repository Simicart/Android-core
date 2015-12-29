package com.simicart.plugins.wishlist.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.catalog.product.block.RelatedProductBlock;
import com.simicart.core.catalog.product.controller.RelatedProductController;
import com.simicart.core.config.Rconfig;
import com.simicart.core.event.block.CacheBlock;
import com.simicart.core.event.block.EventBlock;
import com.simicart.plugins.wishlist.block.ProductWishlistBlock;
import com.simicart.plugins.wishlist.controller.ProductWishListController;
import com.simicart.plugins.wishlist.delegate.MyWishListDelegate;

public class ProductWishListFragment extends SimiFragment {
	protected ProductWishlistBlock mBlock = null;
	protected ProductWishListController mController = null;
	protected RelatedProductBlock mRelatedBlock;
	protected RelatedProductController mRelatedController;
	protected String mID;
	protected MyWishListDelegate mDelegate;
	
	public void setDelegate(MyWishListDelegate delegate)
	{
		mDelegate = delegate;
	}

	public void setProductId(String id) {
		mID = id;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				Rconfig.getInstance()
						.layout("wishlist_fragment_product_detail"), container,
				false);
		Context context = getActivity();

		mBlock = new ProductWishlistBlock(view, context,
				getChildFragmentManager(), getFragmentManager());
		mBlock.setDelegate(mDelegate);
		mBlock.initView();

		if (null == mController) {
			mController = new ProductWishListController();
			mController.setDelegate(mBlock);
			mController.setProductId(mID);
			mController.onStart();
		} else {
			mController.setDelegate(mBlock);
			mController.setProductId(mID);
			mController.onResume();
		}

		mBlock.setListenerAddToCart(mController.getListenerAddToCart());

		View relatedView = view.findViewById(Rconfig.getInstance().id(
				"ll_parent_related_product"));
		mRelatedBlock = new RelatedProductBlock(relatedView, context);
		// event for related product block
		CacheBlock cacheRelated = new CacheBlock();
		cacheRelated.setBlock(mRelatedBlock);
		EventBlock eventRelated = new EventBlock();
		eventRelated.dispatchEvent(
				"com.simicart.core.catalog.product.block.RelatedProductBlock",
				relatedView, context, cacheRelated);
		mRelatedBlock = (RelatedProductBlock) cacheRelated.getBlock();

		mRelatedBlock.initView();
		if (mRelatedController == null) {
			mRelatedController = new RelatedProductController();
			mRelatedController.setProductId(mID);
			mRelatedController.setDelegate(mRelatedBlock);
			mRelatedController.onStart();
		} else {
			mRelatedController.setDelegate(mRelatedBlock);
			mRelatedController.onResume();
		}

		return view;
	}
}
