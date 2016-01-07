package com.simicart.core.base.fragment;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.simicart.core.config.Constants;
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

	public static void setData(String key, Object object, String type, Bundle bundle) {

		switch (type) {
		case Constants.KeyData.TYPE_STRING:
			bundle.putString(key, (String) object);

			break;
		case Constants.KeyData.TYPE_INT:
			bundle.putInt(key, (int) object);

			break;
		case Constants.KeyData.TYPE_LIST_STRING:
			bundle.putStringArrayList(key, (ArrayList<String>) object);

			break;
		case Constants.KeyData.TYPE_LIST_INT:
			bundle.putIntegerArrayList(key, (ArrayList<Integer>) object);

			break;
		case Constants.KeyData.TYPE_MODEL:
			bundle.putSerializable(key, (Serializable) object);

			break;
		case Constants.KeyData.TYPE_LIST_MODEL:
			bundle.putSerializable(key, (Serializable) object);

			break;

		default:
			break;
		}

	}

	public static Object getData(String key, String type,Bundle bundle) {

		Object object = new Object();
		switch (type) {
		case Constants.KeyData.TYPE_STRING:
			object = bundle.getString(key, "");

			break;
		case Constants.KeyData.TYPE_INT:
			object = bundle.getInt(key, 0);

			break;
		case Constants.KeyData.TYPE_LIST_STRING:
			object = bundle.getStringArrayList(key);

			break;
		case Constants.KeyData.TYPE_LIST_INT:
			object = bundle.getIntegerArrayList(key);

			break;
		case Constants.KeyData.TYPE_MODEL:
			object = bundle.getSerializable(key);

			break;
		case Constants.KeyData.TYPE_LIST_MODEL:
			object = bundle.getSerializable(key);

			break;

		default:
			break;
		}

		return object;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
}
