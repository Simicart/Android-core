package com.simicart.core.catalog.product.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout.LayoutParams;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.catalog.product.adapter.ProductDetailParentAdapter;
import com.simicart.core.catalog.product.adapter.ProductDetailParentAdapterTablet;
import com.simicart.core.catalog.product.block.ProductDetailParentBlock;
import com.simicart.core.catalog.product.controller.ProductDetailParentController;
import com.simicart.core.common.Utils;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.style.CirclePageIndicator;

public class ProductDetailParentFragment extends SimiFragment {

	protected ArrayList<String> mListID;
	protected String mID;
	protected ProductDetailParentBlock mBlock;
	protected ProductDetailParentController mController;

	public static ProductDetailParentFragment newInstance() {
		ProductDetailParentFragment fragment = new ProductDetailParentFragment();
		return fragment;
	}

	public void setProductID(String id) {
		mID = id;
	}

	public void setListIDProduct(ArrayList<String> ids) {
		mListID = ids;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setScreenName("Product Detail Screen - ProductID: " + mID);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				Rconfig.getInstance().layout("core_product_detail_parent"),
				container, false);

		Log.e("LONGTB", "ID PARENT SET" + mID);

		mBlock = new ProductDetailParentBlock(view, getActivity());
		mBlock.initView();
		if (null == mController) {
			mController = new ProductDetailParentController();
			mController.setDelegate(mBlock);
			mController.setProductDelegate(mBlock);
			mController.setProductId(mID);
			mController.onStart();
		} else {
			mController.setDelegate(mBlock);
			mController.setProductDelegate(mBlock);
			mController.onResume();
		}

		mBlock.setAddToCartListener(mController.getTouchAddToCart());
		mBlock.setOnDoneOption(mController.getOnDoneClick());
		mBlock.setDetailListener(mController.getTouchDetails());
		mBlock.setOptionListener(mController.getTouchOptions());

		SimiManager.getIntance().setChildFragment(getChildFragmentManager());

		int position = getPosition();

		if (position < 0) {
			mListID = new ArrayList<String>();
			mListID.add(mID);
			position = getPosition();
		}

		final ViewPager pager_parent = (ViewPager) view.findViewById(Rconfig
				.getInstance().id("pager_parent"));

		if (DataLocal.isTablet) {
			try {
				ProductDetailParentAdapterTablet adapter_tablet = new ProductDetailParentAdapterTablet(
						SimiManager.getIntance().getChilFragmentManager());
				adapter_tablet.setListID(mListID);
				adapter_tablet.setController(mController);
				adapter_tablet.setProductDelegate(mBlock);
				pager_parent.setAdapter(adapter_tablet);
				pager_parent.setCurrentItem(position);
				adapter_tablet.setCurrentID(mID);
				pager_parent.setClipToPadding(false);
				pager_parent.setPageMargin(50);

				pager_parent.setOffscreenPageLimit(3);

				CirclePageIndicator mIndicator = (CirclePageIndicator) view
						.findViewById(Rconfig.getInstance().id("indicator"));
				mIndicator.setScaleX(1.5f);
				mIndicator.setScaleY(1.5f);
				LayoutParams params = new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
				String message = getActivity().getString(
						Rconfig.getInstance().string("values"));

				if (message.equals("sw800dp")) {
					params.leftMargin = Utils.getValueDp(310);
				} else {
					params.leftMargin = Utils.getValueDp(260);
				}
				mIndicator.setLayoutParams(params);
				mController.setAdapterDelegate(adapter_tablet);
				pager_parent
						.setOnPageChangeListener(new OnPageChangeListener() {

							@Override
							public void onPageSelected(int position) {
								if (position == mListID.size() - 1) {
									pager_parent.setPadding(
											Utils.getValueDp(50), 0,
											Utils.getValueDp(200), 0);
								} else {
									pager_parent.setPadding(
											Utils.getValueDp(200), 0,
											Utils.getValueDp(50), 0);
								}
							}

							@Override
							public void onPageScrolled(int arg0, float arg1,
									int arg2) {

							}

							@Override
							public void onPageScrollStateChanged(int arg0) {

							}
						});
			} catch (Exception e) {
				System.err.println("Error Product detail:" + e.getMessage());
			}
		} else {
			ProductDetailParentAdapter adapter = new ProductDetailParentAdapter(
					SimiManager.getIntance().getChilFragmentManager());
			adapter.setController(mController);
			adapter.setListID(mListID);
			pager_parent.setOffscreenPageLimit(3);
			pager_parent.setAdapter(adapter);
			pager_parent.setCurrentItem(position);
			mController.setAdapterDelegate(adapter);
		}

		return view;
	}

	protected int getPosition() {
		if (null != mListID && mListID.size() > 0) {
			for (int i = 0; i < mListID.size(); i++) {
				String id = mListID.get(i);
				if (id.equals(mID)) {
					Log.e("ProductDetailParentFragment ", "POSITION " + i
							+ "ID " + id);
					return i;
				}
			}
		}

		return -1;
	}

}
