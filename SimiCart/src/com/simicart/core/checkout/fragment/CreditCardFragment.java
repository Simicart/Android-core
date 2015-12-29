package com.simicart.core.checkout.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.checkout.block.CreditCardBlock;
import com.simicart.core.checkout.controller.CreditCardController;
import com.simicart.core.checkout.entity.PaymentMethod;
import com.simicart.core.config.Rconfig;

public class CreditCardFragment extends SimiFragment {

	protected CreditCardBlock mBlock;
	protected CreditCardController mController;
	protected PaymentMethod mPaymentMethod;
	protected boolean isCheckedMethod;

	public static CreditCardFragment newInstance() {
		CreditCardFragment fragment = new CreditCardFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				Rconfig.getInstance().layout("core_credit_card_layout"),
				container, false);
		Context context = getActivity();

		mBlock = new CreditCardBlock(view, context);
		mBlock.setPaymentMethod(mPaymentMethod);
		mBlock.setCheckedMethod(isCheckedMethod);
		mBlock.initView();

		if (null == mController) {
			mController = new CreditCardController();
			mController.setDelegate(mBlock);
			mController.setIsCheckedMethod(isCheckedMethod);
			mController.onStart();
		} else {
			mController.setDelegate(mBlock);
			mController.onResume();
		}

		mBlock.setClickSave(mController.getOnClickSave());
		return view;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.mPaymentMethod = paymentMethod;
	}

	public void setIsCheckedMethod(boolean isCheckedMethod) {
		this.isCheckedMethod = isCheckedMethod;
	}
}