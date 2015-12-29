package com.simicart.core.catalog.product.block;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.catalog.product.adapter.ProductDetailChildeAdapter;
import com.simicart.core.catalog.product.controller.ProductDetailParentController;
import com.simicart.core.catalog.product.delegate.ProductDetailChildDelegate;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.config.Rconfig;
import com.simicart.core.style.VerticalViewPager2;

public class ProductDetailChildBlock extends SimiBlock implements ProductDetailChildDelegate{

	protected VerticalViewPager2 mPagerChild;
	protected Product mProduct;
	protected FragmentManager mFragmentChild;
	protected ProductDetailParentController mParentController;
	protected ProductDetailChildeAdapter mAdapter;

	public void setDelegate(ProductDetailParentController delegate) {
		mParentController = delegate;
	}

	public ProductDetailChildBlock(View view, Context context,
			FragmentManager fragmentChild) {
		super(view, context);
		this.mFragmentChild = fragmentChild;

	}

	@Override
	public void initView() {
		mPagerChild = (VerticalViewPager2) mView.findViewById(Rconfig
				.getInstance().id("pager_child"));
		mPagerChild.setOffscreenPageLimit(3);
	}

	@Override
	public void drawView(SimiCollection collection) {
		if (null != collection) {
			mProduct = getProductFromCollection(collection);
			if (null != mProduct) {
				showImage();
			}

		}
	}

	protected Product getProductFromCollection(SimiCollection collection) {
		Product product = null;
		ArrayList<SimiEntity> entity = collection.getCollection();
		if (null != entity && entity.size() > 0) {
			product = (Product) entity.get(0);
		}
		return product;
	}

	protected void showImage() {
		Log.e("ProductDetailChildBlock ", "Show Image");
		if(mProduct.getImages() != null && mProduct.getImages().length > 0) {
			String[] Images = mProduct.getImages();
			mAdapter = new ProductDetailChildeAdapter(
					mFragmentChild, Images);
			mAdapter.setDelegate(mParentController);
			mPagerChild.setAdapter(mAdapter);
		}
	}

	@Override
	public void updateIndicator() {
		Log.e("ProductDetailChildBlock ", "updateIndicator");
		mParentController.updateViewPager(mPagerChild);
	}

}
