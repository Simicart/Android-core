package com.simicart.core.checkout.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.checkout.block.PaymentMethodBlock;
import com.simicart.core.checkout.block.ReviewOrderBlock;
import com.simicart.core.checkout.block.ShippingBlock;
import com.simicart.core.checkout.controller.ConfigCheckout;
import com.simicart.core.checkout.controller.ReviewOrderController;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.entity.MyAddress;

public class ReviewOrderFragment extends SimiFragment {

	protected ReviewOrderBlock mBlock;
	protected PaymentMethodBlock mPaymentMethodBlock;
	protected ShippingBlock mShippingBlock;
	protected ReviewOrderController mController;
	protected MyAddress mBillingAddress;
	protected MyAddress mShippingAddress;
	protected int mAfterControll;

	public static ReviewOrderFragment newInstance(int afterControll,
			MyAddress shippingAdd, MyAddress billingAdd) {
		ReviewOrderFragment fragment = new ReviewOrderFragment();
		fragment.setTargetFragment(fragment, ConfigCheckout.TARGET_REVIEWORDER);
		Bundle bundle = new Bundle();
		setData(Constants.KeyData.AFTER_CONTROL, afterControll,
				Constants.KeyData.TYPE_INT, bundle);
		bundle.putSerializable(Constants.KeyData.SHIPPING_ADDRESS, shippingAdd);
		bundle.putSerializable(Constants.KeyData.BILLING_ADDRESS, billingAdd);
		fragment.setArguments(bundle);
		fragment.mBillingAddress = billingAdd;
		fragment.mShippingAddress = shippingAdd;
		return fragment;
	}

	public int getAftercontroll() {
		return mAfterControll;
	}

	public MyAddress getBillingAddress() {
		return mBillingAddress;
	}

	public MyAddress getShippingAddress() {
		return mShippingAddress;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setScreenName("Review Order Screen");
		SimiManager.getIntance().showCartLayout(false);
		View view = inflater.inflate(
				Rconfig.getInstance().layout("core_review_order_layout"),
				container, false);
		if (DataLocal.isLanguageRTL) {
			view = inflater.inflate(
					Rconfig.getInstance().layout("rtl_review_order_layout"),
					container, false);
		}
		Context context = getActivity();

		// getdata
		if (getArguments() != null) {
			mAfterControll = (int) getData(Constants.KeyData.AFTER_CONTROL,
					Constants.KeyData.TYPE_INT, getArguments());
			mShippingAddress = (MyAddress) getArguments().getSerializable(
					Constants.KeyData.SHIPPING_ADDRESS);
			mBillingAddress = (MyAddress) getArguments().getSerializable(
					Constants.KeyData.BILLING_ADDRESS);
		}
		Log.e("duyquang", "==ReviewOrderFragment3==" + mAfterControll
				+ mShippingAddress.toString());
		mBlock = new ReviewOrderBlock(view, context);
		mBlock.initView();

		LinearLayout list_shiping = (LinearLayout) view.findViewById(Rconfig
				.getInstance().id("ll_shipping"));
		mShippingBlock = new ShippingBlock(view, context);
		mShippingBlock.initView();
		mShippingBlock.setDelegate(mBlock);

		LinearLayout list_payment = (LinearLayout) view.findViewById(Rconfig
				.getInstance().id("ll_payment"));
		mPaymentMethodBlock = new PaymentMethodBlock(view, context);
		mPaymentMethodBlock.initView();
		mPaymentMethodBlock.setDelegate(mBlock);

		if (mController == null) {
			mController = new ReviewOrderController();
			mShippingBlock.setReviewOrder(mController);
			mPaymentMethodBlock.setReviewOrder(mController);
			mController.setDelegate(mBlock);
			mController.setShippingDelegate(mShippingBlock);
			mController.setDelegate(mPaymentMethodBlock);
			mController.setBillingAddress(mBillingAddress);
			mController.setShippingAddress(mShippingAddress);
			mController.setAfterControll(mAfterControll);
			mController.onStart();
		} else {
			mShippingBlock.setReviewOrder(mController);
			mPaymentMethodBlock.setReviewOrder(mController);
			mController.setDelegate(mBlock);
			mController.setShippingDelegate(mShippingBlock);
			mController.setDelegate(mPaymentMethodBlock);
			mController.onResume();
		}
		mBlock.setOnChoiceBillingAddress(mController
				.getOnChoiceBillingAddress());
		mBlock.setOnChoiceShippingAddress(mController
				.getOnChoiceShippingAddress());
		mBlock.setClickPlaceNow(mController.getOnPlaceNow());
		mBlock.setCouponCodeListener(mController.getCouponCodeListener());
		mBlock.setOnViewDetaiProduct(mController.getOnViewDetailProduct());

		return view;
	}

	@Override
	public void onDestroy() {
		SimiManager.getIntance().showCartLayout(true);
		if (null != mController) {
			mController.onDestroy();
		}
		super.onDestroy();
	}

}
