package com.simicart.core.base.fragment;

import java.util.ArrayList;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.simicart.core.base.model.entity.BusEntity;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.event.fragment.CacheFragment;
import com.simicart.core.event.fragment.EventFragment;

import de.greenrobot.event.EventBus;

public class SimiFragment extends Fragment {

	protected View rootView;
	protected boolean isShowPopup = false;
	protected String screenName = "";

	public static SimiFragment newInstance() {
		SimiFragment fragment = new SimiFragment();
//		Bundle bundle= new Bundle();
//		setData(Constants.KeyData.CHECK_POPUP, isShowPopup, Constants.KeyData.TYPE_BOOLEAN, bundle);
//		fragment.setArguments(bundle);
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
	
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
//		isShowPopup = (boolean) getData(Constants.KeyData.CHECK_POPUP, Constants.KeyData.TYPE_BOOLEAN, getArguments());
	}

	public void setScreenName(String name) {
		this.screenName = name;

		CacheFragment cache = new CacheFragment();
		cache.setFragment(this);
		EventFragment event = new EventFragment();
		event.dispatchEvent(
				"com.simicart.core.base.fragment.SimiFragment.setName", cache);
	}

	@SuppressWarnings("unchecked")
	public static void setData(String key, Object object, String type, Bundle bundle) {

		switch (type) {
		case Constants.KeyData.TYPE_STRING:
			bundle.putString(key, (String) object);

			break;
		case Constants.KeyData.TYPE_INT:
			bundle.putInt(key, (int) object);

			break;
		case Constants.KeyData.TYPE_FLOAT:
			bundle.putFloat(key,(float) object);

			break;
		case Constants.KeyData.TYPE_LIST_STRING:
			bundle.putStringArrayList(key, (ArrayList<String>) object);
			
			break;
		case Constants.KeyData.TYPE_LIST_INT:
			bundle.putIntegerArrayList(key, (ArrayList<Integer>) object);

			break;
//		case Constants.KeyData.TYPE_MODEL:
//			bundle.putSerializable(key, (Serializable) object);
//			
//		case Constants.KeyData.TYPE_MODEL_PAR:
//			bundle.putParcelable(key, (Parcelable) object);
//
//			break;
//		case Constants.KeyData.TYPE_LIST_MODEL:
//			bundle.putSerializable(key, (Serializable) object);
//
//			break;
			case Constants.KeyData.TYPE_JSONOBJECT:
			bundle.putString(key, (String) object);

			break;
		case Constants.KeyData.TYPE_BOOLEAN:
			bundle.putBoolean(key, (boolean) object);

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
		case Constants.KeyData.TYPE_FLOAT:
			object = bundle.getFloat(key, 0);

			break;
		case Constants.KeyData.TYPE_LIST_STRING:
			object = bundle.getStringArrayList(key);

			break;
		case Constants.KeyData.TYPE_LIST_INT:
			object = bundle.getIntegerArrayList(key);

			break;
			case Constants.KeyData.TYPE_JSONOBJECT:
			object = bundle.getString(key, "");

			break;
//		case Constants.KeyData.TYPE_MODEL:
//			object = bundle.getSerializable(key);
//
//			break;
//		case Constants.KeyData.TYPE_MODEL_PAR:
//			object = bundle.getParcelable(key);
//
//			break;
//		case Constants.KeyData.TYPE_LIST_MODEL:
//			object = bundle.getSerializable(key);
//
//			break;
		case Constants.KeyData.TYPE_BOOLEAN:
			object = bundle.getBoolean(key);

			break;
			
		default:
			break;
		}

		return object;
	}
	public void onEvent(BusEntity event){
		 /* Do something */
		}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
	}
	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
		
	}
}
