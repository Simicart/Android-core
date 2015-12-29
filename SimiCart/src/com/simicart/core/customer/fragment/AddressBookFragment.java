package com.simicart.core.customer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.block.AddressBookBlock;
import com.simicart.core.customer.controller.AddressBookController;

public class AddressBookFragment extends SimiFragment {

	protected AddressBookBlock mBlock;
	protected AddressBookController mController;

	public static AddressBookFragment newInstance() {
		AddressBookFragment fragment = new AddressBookFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(
				Rconfig.getInstance().layout("core_address_book_layout"),
				container, false);
		Context context = getActivity();

		mBlock = new AddressBookBlock(view, context);
		mBlock.initView();

		if (null == mController) {
			mController = new AddressBookController();
			mController.setDelegate(mBlock);
			mController.onStart();
		} else {
			mController.setDelegate(mBlock);
			mController.onResume();
		}

		mBlock.setOnItemClicker(mController.getItemClicker());
		mBlock.setonTouchListener(mController.getListener());

		return view;

	}

}
