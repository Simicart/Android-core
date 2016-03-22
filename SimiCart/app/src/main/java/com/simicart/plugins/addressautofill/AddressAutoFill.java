package com.simicart.plugins.addressautofill;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.common.GPSTracker;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.block.NewAddressBookBlock;
import com.simicart.core.customer.entity.CountryAllowed;
import com.simicart.core.customer.entity.StateOfCountry;
import com.simicart.core.event.block.CacheBlock;

public class AddressAutoFill {
	protected Context mContext;
	protected View mView;
	protected NewAddressBookBlock mDelegate;
	protected SimiCollection mCollection;
	protected String mStreet, mCity, mLocality, mLocalityID, mPostalCode,
			mCountryName, mCountryID;
	protected ArrayList<CountryAllowed> mCountryAllowed;
	protected ArrayList<String> mListCountry;

	public AddressAutoFill(String method, CacheBlock cacheBlock) {
		mDelegate = (NewAddressBookBlock) cacheBlock.getBlock();
		mView = cacheBlock.getView();
		mCollection = cacheBlock.getSimiCollection();
		mContext = SimiManager.getIntance().getCurrentContext();
		if (method.equals("addCreateView")) {
			addCreateView();
		}
	}

	private void addCreateView() {
		LinearLayout ll_plugin = (LinearLayout) mView.findViewById(Rconfig
				.getInstance().id("ll_plugin"));
		ll_plugin.removeAllViewsInLayout();
		ll_plugin.setVisibility(View.VISIBLE);

		RelativeLayout ll_addressAuto = new RelativeLayout(mContext);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		ll_addressAuto.setLayoutParams(lp);

		ImageView img = new ImageView(mContext);
		int value25 = Utils.getValueDp(25);
		RelativeLayout.LayoutParams lp_img = new RelativeLayout.LayoutParams(
				value25, value25);
		lp_img.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		int value5 = Utils.getValueDp(5);
		img.setLayoutParams(lp_img);
		img.setPadding(value5, value5, value5, value5);
		img.setImageResource(Rconfig.getInstance().drawable(
				"plugins_locator_xam"));
		ll_addressAuto.addView(img);
		
		ImageView img_right = new ImageView(mContext);
		RelativeLayout.LayoutParams lp_img_right = new RelativeLayout.LayoutParams(
				value25, value25);
		lp_img_right.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		img_right.setLayoutParams(lp_img_right);
		img_right.setImageResource(Rconfig.getInstance().drawable("ic_extend"));
		ll_addressAuto.addView(img_right);

		final TextView tv_address = new TextView(mContext);
		RelativeLayout.LayoutParams title_lp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		if(DataLocal.isLanguageRTL){
			title_lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			title_lp.setMargins(0, 0, Utils.getValueDp(40), 0);
		}else{
		title_lp.setMargins(Utils.getValueDp(30), 0, 0, 0);
		}
		tv_address.setLayoutParams(title_lp);
		tv_address.setGravity(Gravity.CENTER_VERTICAL);
		tv_address.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		tv_address.setText(Config.getInstance().getText(
				"Use Your Current Location"));
		tv_address.setBackgroundColor(Color.WHITE);
		tv_address.setTextColor(Color.BLACK);
		ll_addressAuto.addView(tv_address);

		

		ll_plugin.addView(ll_addressAuto);

		getListCountryAllowed();

		ll_addressAuto.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					tv_address.setTextColor(Color.GRAY);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					tv_address.setTextColor(Color.BLACK);
					triggerLocation(mContext);
					control();
				} else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
					tv_address.setTextColor(Color.BLACK);
				}
				return true;
			}

		});

	}

	public void triggerLocation(final Context context) {
		// create class object
		GPSTracker gps = new GPSTracker(context);

		// check if GPS enabled
		if (gps.canGetLocation()) {

			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();
			// \n is for new line
			Log.e(getClass().getName(), "Your Location is - \nLat: " + latitude
					+ "\nLong: " + longitude);
			getAddress(latitude, longitude);
		} else {
			// can't get location
			// GPS or Network is not enabled
			// Ask user to enable GPS/network in settings
			Toast.makeText(
					context,
					"GPS is not enabled. Please enable it to get your current location",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void getAddress(double lat, double lon) {
		Geocoder geocoder = new Geocoder(mContext);
		try {
			List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
			if (addresses != null && addresses.size() > 0) {
				Address returnedAddress = addresses.get(0);
				mStreet = returnedAddress.getAddressLine(0) + ", "
						+ returnedAddress.getAddressLine(1) + ", "
						+ returnedAddress.getAddressLine(2);
				mCity = returnedAddress.getAdminArea();
				mLocality = returnedAddress.getLocality();
				mPostalCode = returnedAddress.getPostalCode();
				mCountryName = returnedAddress.getCountryName();
				mCountryID = returnedAddress.getCountryCode();
				StringBuilder strReturnedAddress = new StringBuilder();
				for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
					strReturnedAddress
							.append(returnedAddress.getAddressLine(i)).append(
									";");
				}
				// ret = strReturnedAddress.toString();
			} else {
				Toast.makeText(mContext,
						"Can't get your current address. Please try again!",
						Toast.LENGTH_SHORT).show();
			}
		} catch (IOException e) {
			// ret = "Can't get Address!";
		}
	}

	private void control() {
		if (Utils.validateString(mStreet)) {
			((EditText) findViewById("et_street")).setText(mStreet);
		}
		if (Utils.validateString(mCity)) {
			((EditText) findViewById("et_city")).setText(mCity);
		}
		if (Utils.validateString(mPostalCode)) {
			((EditText) findViewById("et_zipcode")).setText(mPostalCode);
		}

		RelativeLayout rl_state = (RelativeLayout) findViewById("rl_state");
		TextView edt_state = (TextView) findViewById("et_state");

		if (Utils.validateString(mCountryID)) {
			mDelegate.updateCountry(getCountryName(mCountryID));
			ArrayList<String> states = getStateFromCountry(
					getCountryName(mCountryID), mCountryAllowed);
			if (states.size() > 0) {
				edt_state.setVisibility(View.GONE);
				rl_state.setVisibility(View.VISIBLE);
			} else {
				edt_state.setVisibility(View.VISIBLE);
				rl_state.setVisibility(View.GONE);
				if (Utils.validateString(mLocality)) {
					edt_state.setText(mLocality);
				} else {
					edt_state.setText("");
				}
			}
		}
	}

	@SuppressLint("DefaultLocale")
	private String getCountryName(String country_code) {
		String country_name = "";
		for (int i = 0; i < mCountryAllowed.size(); i++) {
			if (country_code.toLowerCase().equals(
					mCountryAllowed.get(i).getCountry_code().toLowerCase())) {
				country_name = mCountryAllowed.get(i).getCountry_name();
			}
		}
		return country_name;
	}

	public ArrayList<String> getStateFromCountry(String country,
			ArrayList<CountryAllowed> listCountry) {
		ArrayList<String> states = new ArrayList<String>();
		for (CountryAllowed countryAllowed : listCountry) {
			if (countryAllowed.getCountry_name().equals(country)) {
				for (StateOfCountry state : countryAllowed.getStateList()) {
					states.add(state.getState_name());
				}
				return states;
			}
		}
		return states;
	}

	protected void getListCountryAllowed() {
		ArrayList<SimiEntity> entity = mCollection.getCollection();
		if (null != entity && entity.size() > 0) {
			mCountryAllowed = new ArrayList<CountryAllowed>();
			for (SimiEntity simiEntity : entity) {
				CountryAllowed country_add = (CountryAllowed) simiEntity;
				mCountryAllowed.add(country_add);
			}
			mListCountry = new ArrayList<String>();
			for (int i = 0; i < mCountryAllowed.size(); i++) {
				mListCountry.add(mCountryAllowed.get(i).getCountry_name());
			}
		}

	}

	protected View findViewById(String id) {
		View view = mView.findViewById(Rconfig.getInstance().id(id));
		return view;
	}

}
