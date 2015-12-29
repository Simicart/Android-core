package com.simicart.plugins.instantcontact.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.instantcontact.block.ContactUsBlock;
import com.simicart.plugins.instantcontact.controller.ContactUsController;

public class ContactUsFragment extends SimiFragment {

	protected ContactUsBlock mBlock;
	protected ContactUsController mController;

	public static ContactUsFragment newInstance() {
		ContactUsFragment fragment = new ContactUsFragment();
		fragment.setShowPopup(true);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				Rconfig.getInstance().layout("plugins_contactus_layout2"),
				container, false);
		Context context = getActivity();
		mBlock = new ContactUsBlock(view, context);
		mBlock.initView();

		if (null == mController) {
			mController = new ContactUsController();
			mController.setDelegate(mBlock);
			mController.onStart();
		} else {
			mController.setDelegate(mBlock);
			mController.onResume();
		}
		mBlock.setGridViewOnItemClick(mController.getGridOnItemClick());
		// mBlock.setMakeACallListener(mController.getMakeACallListener());
		// mBlock.setSendEmailListener(mController.getSendEmailListener());
		// mBlock.setSendMessage(mController.getSendMessageListener());
		// mBlock.setVisitWebsite(mController.getVisitWebsiteListener());

		return view;
	}

}
