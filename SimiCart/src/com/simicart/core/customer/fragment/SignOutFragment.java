package com.simicart.core.customer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.controller.SignOutController;

public class SignOutFragment extends SimiFragment {

	public static SignOutFragment newInstance() {
		SignOutFragment fragment = new SignOutFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				Rconfig.getInstance().layout("core_home_layout"), container,
				false);
		Context context = getActivity();
		SimiBlock block = new SimiBlock(view, context);
		SignOutController controller = new SignOutController();
		controller.setDelegate(block);
		controller.onStart();

		return view;
	}

}
