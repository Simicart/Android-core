package com.simicart.core.base.fragment;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.simicart.core.config.DataLocal;
import com.simicart.core.event.fragment.CacheFragment;
import com.simicart.core.event.fragment.EventFragment;

public class SimiFragment extends Fragment {

	protected View rootView;
	protected boolean isShowPopup = false;
	protected String screenName = "";

	public static SimiFragment newInstance() {
		SimiFragment fragment = new SimiFragment();
		return fragment;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setShowPopup(boolean isShowPopup) {
		this.isShowPopup = isShowPopup;
	}

	public boolean isShowPopup() {
		return isShowPopup;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if (isVisibleToUser && getActivity() != null) {
			if (DataLocal.isTablet) {
				getActivity().setRequestedOrientation(
						ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			} else {
				getActivity().setRequestedOrientation(
						ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}
		}
	}

	public void setScreenName(String name) {
		this.screenName = name;

		CacheFragment cache = new CacheFragment();
		cache.setFragment(this);
		EventFragment event = new EventFragment();
		event.dispatchEvent(
				"com.simicart.core.base.fragment.SimiFragment.setName", cache);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
}
