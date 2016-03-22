package com.simicart.core.catalog.product.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.adapter.TabAdapterFragment;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.model.entity.BusEntity;
import com.simicart.core.catalog.product.block.ProductMorePluginBlock;
import com.simicart.core.catalog.product.controller.ProductMorePluginController;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.Rconfig;
import com.simicart.core.style.PagerSlidingTabStrip;

import de.greenrobot.event.EventBus;

public class InformationFragment extends SimiFragment {

	protected Product mProduct;
	protected View mRootView;
	protected ProductMorePluginBlock mPluginBlock;
	protected ProductMorePluginController mPluginController;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public static InformationFragment newInstance(Product product) {
		InformationFragment fragment = new InformationFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable(Constants.KeyData.PRODUCT, product);
		fragment.setArguments(bundle);
		return fragment;
	}

	public Product getProduct() {
		return mProduct;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(
				Rconfig.getInstance().layout("core_information_layout"),
				container, false);
		Context context = getActivity();
		
		if(getArguments() != null){
			mProduct = (Product) getArguments().getSerializable(Constants.KeyData.PRODUCT);
		}
		if (null != mProduct) {
			BusEntity<Product> busEntity = new BusEntity<>();
			busEntity.setKey(Constants.KeyBus.PRODUCT);
			busEntity.setValue(mProduct);
			EventBus.getDefault().postSticky(busEntity);
			initView();
			// RelativeLayout ll_plugin = (RelativeLayout) mRootView
			// .findViewById(Rconfig.getInstance().id("more_plugins"));
			mPluginBlock = new ProductMorePluginBlock(mRootView, context);
			mPluginBlock.setProduct(mProduct);
			mPluginBlock.initView();
			if (mPluginController == null) {
				mPluginController = new ProductMorePluginController();
				mPluginController.setProduct(mProduct);
				mPluginController.onStart();
			} else {
				mPluginController.onResume();
				mPluginController.setProduct(mProduct);
			}
			mPluginBlock.setListenerMoreShare(mPluginController
					.getClickerShare());

		}
		return mRootView;
	}

	public void initView() {
		final TabAdapterFragment adapter = new TabAdapterFragment(
				getChildFragmentManager(), mProduct);
		final ViewPager mPager = (ViewPager) mRootView.findViewById(Rconfig
				.getInstance().id("pager"));
		mPager.setAdapter(adapter);

		PagerSlidingTabStrip title_tab = (PagerSlidingTabStrip) mRootView
				.findViewById(Rconfig.getInstance().id("pager_title_strip"));
		title_tab.setTextColor(Config.getInstance().getSection_text_color());
		title_tab.setBackgroundColor(Color.parseColor(Config.getInstance()
				.getSection_color()));
		title_tab.setDividerColor(Config.getInstance().getSection_text_color());
		title_tab.setIndicatorColor(Config.getInstance().getKey_color());
		title_tab.setIndicatorHeight(5);
		title_tab.setAllCaps(false);
		title_tab.setViewPager(mPager);
	}
	
}
