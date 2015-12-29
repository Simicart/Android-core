package com.simicart.core.style;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.checkout.controller.ConfigCheckout;
import com.simicart.core.config.Rconfig;

public class FragmentDialogHandle extends DialogFragment {
	Fragment fragment;
	ImageView back;

	public static FragmentDialogHandle newInstance(Fragment fragment) {
		FragmentDialogHandle fHandle = new FragmentDialogHandle();
		fHandle.fragment = fragment;
		return fHandle;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		getDialog().setCanceledOnTouchOutside(false);
		View v = inflater.inflate(
				Rconfig.getInstance().layout("core_popup_tablet_layout"),
				container, false);

		FragmentTransaction ft = getChildFragmentManager().beginTransaction();
		ft.replace(Rconfig.getInstance().id("popup_container"), fragment);
		ft.addToBackStack(null).commit();

		back = (ImageView) v.findViewById(Rconfig.getInstance().id("bt_back"));
		back.setVisibility(View.GONE);

		back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.e("FragmentDialogHanle ", "onClickBack");
				int count = getChildFragmentManager().getBackStackEntryCount();
				Log.e("FragmentDialogHandle ", "Count  : " + count);
				if (count > 1) {
					if (count <= 2) {
						back.setVisibility(View.GONE);
						getChildFragmentManager().popBackStackImmediate();
					}
					//check fragment from thankyou page
					if(count > 2){
						Fragment fragment = getChildFragmentManager().getFragments().get(count-2);
						int tag = fragment.getTargetRequestCode();
						if(tag == ConfigCheckout.TARGET_REVIEWORDER){
							SimiManager.getIntance().backToHomeFragment();
						}else{
							getChildFragmentManager().popBackStackImmediate();
						}
					}
					
				}
			}
		});

		ImageView exit = (ImageView) v.findViewById(Rconfig.getInstance().id(
				"bt_exit"));
		exit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dismiss();
			}
		});
		return v;
	}

	public void changeFragment(Fragment fragment) {
		FragmentTransaction ft = getChildFragmentManager().beginTransaction();
		ft.replace(Rconfig.getInstance().id("popup_container"), fragment);
		ft.addToBackStack(null).commit();
	}
}
