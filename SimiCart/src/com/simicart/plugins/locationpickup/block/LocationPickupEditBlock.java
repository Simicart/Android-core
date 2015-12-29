package com.simicart.plugins.locationpickup.block;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.block.AddressBookDetailBlock;
import com.simicart.core.customer.entity.CountryAllowed;
import com.simicart.core.customer.entity.MyAddress;
import com.simicart.core.customer.entity.StateOfCountry;

public class LocationPickupEditBlock extends AddressBookDetailBlock {
	protected LatLng start = new LatLng(42.568822, -76.009406);
	protected ImageView bt_detect;
	protected static Location mLocation;
	protected String lat = "";
	protected String lng = "";
	protected TextView lb_map;
	protected GoogleMap ggmap;
	protected double d_lat = -1;
	protected double d_lng = -1;
	protected String mStreet, mCity, mLocality, mLocalityID, mPostalCode,
			mCountryName, mCountryID;
	protected ArrayList<CountryAllowed> mCountryAllowed;
	protected ArrayList<String> mListCountry;
	protected SimiCollection mCollection;
	protected MyAddress myAddress;

	public LocationPickupEditBlock(View view, Context context) {
		super(view, context);
	}

	public void setGgmap(GoogleMap ggmap) {
		this.ggmap = ggmap;
	}

	@Override
	public void initView() {
		super.initView();

		lb_map = (TextView) mView.findViewById(Rconfig.getInstance().id(
				"lb_map"));
		lb_map.setText(Config.getInstance().getText(
				"Touch the map until you get your desired address"));

		ggmap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng arg0) {
				ggmap.clear();
				start = new LatLng(arg0.latitude, arg0.longitude);
				lat = arg0.latitude + "";
				lng = arg0.longitude + "";
				ggmap.addMarker(new MarkerOptions().position(start).icon(
						BitmapDescriptorFactory.fromResource(Rconfig
								.getInstance().getIdDraw("maker_my"))));
				triggerLocation(arg0.latitude, arg0.longitude);
			}
		});

		String latlng = "";
		if (mAddressBookDetail.getJSONObject().has("latlng")) {
			try {
				latlng = mAddressBookDetail.getJSONObject().getString("latlng");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (!latlng.equals("") && !latlng.equals("null") && latlng != null) {
			ggmap.clear();
			if (latlng.contains(",")) {
				String[] s_latlng = latlng.split(",");
				String s_lat = s_latlng[0];
				String s_lng = s_latlng[1];

				double l_lat = -1;
				double l_lng = -1;
				try {
					l_lat = Double.parseDouble(s_lat);
					l_lng = Double.parseDouble(s_lng);
				} catch (Exception e) {
					// TODO: handle exception
				}
				start = new LatLng(l_lat, l_lng);
				ggmap.addMarker(new MarkerOptions().position(start).icon(
						BitmapDescriptorFactory.fromResource(Rconfig
								.getInstance().getIdDraw("maker_my"))));
				CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(
						start.latitude, start.longitude));
				CameraUpdate zoom = CameraUpdateFactory.zoomTo(12);
				ggmap.moveCamera(center);
				ggmap.animateCamera(zoom);
			}
		} else {
			// if (getLocation(mContext) != null) {
			// ggmap.clear();
			// start = new LatLng(getLocation(mContext).getLatitude(),
			// getLocation(mContext).getLongitude());
			// CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(
			// start.latitude, start.longitude));
			// CameraUpdate zoom = CameraUpdateFactory.zoomTo(12);
			// ggmap.moveCamera(center);
			// ggmap.animateCamera(zoom);
			// }
		}

		bt_detect = (ImageView) mView.findViewById(Rconfig.getInstance().id(
				"bt_detect_location"));
		bt_detect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (getLocation(mContext) != null) {
					ggmap.clear();
					start = new LatLng(getLocation(mContext).getLatitude(),
							getLocation(mContext).getLongitude());
					lat = getLocation(mContext).getLatitude() + "";
					lng = getLocation(mContext).getLongitude() + "";
					ggmap.addMarker(new MarkerOptions().position(start).icon(
							BitmapDescriptorFactory.fromResource(Rconfig
									.getInstance().getIdDraw("maker_my"))));
					triggerLocation(getLocation(mContext).getLatitude(),
							getLocation(mContext).getLongitude());
				}
				CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(
						start.latitude, start.longitude));
				CameraUpdate zoom = CameraUpdateFactory.zoomTo(17);
				ggmap.moveCamera(center);
				ggmap.animateCamera(zoom);
			}
		});
	}

	public static Location getLocation(Context context) {
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		boolean gps_enabled = false;
		boolean network_enable = false;
		try {
			gps_enabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
			network_enable = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (!gps_enabled && !network_enable) {
			showGpsError(context);
		} else {
			if (locationManager != null) {
				mLocation = locationManager
						.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				if (mLocation == null) {
					mLocation = locationManager
							.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					if (mLocation == null) {
						mLocation = locationManager
								.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
					}
				}
				return mLocation;
			} else {
				return null;
			}
		}
		return null;
	}

	public static void showGpsError(final Context contex) {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(contex);
		builder1.setTitle(Config.getInstance().getText(
				"Location services disabled"));
		builder1.setMessage(Config.getInstance().getText(
				"GPS is not enabled. Do you want to go to settings menu?"));
		builder1.setCancelable(true);
		builder1.setNegativeButton(Config.getInstance().getText("Ignore"),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		builder1.setPositiveButton(Config.getInstance().getText("Setting"),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						contex.startActivity(intent);
					}
				});

		AlertDialog alert11 = builder1.create();
		alert11.show();
	}

	@Override
	public MyAddress getAddressBookDetail() {
		// TODO Auto-generated method stub
		myAddress = super.getAddressBookDetail();
		myAddress.getBundle().putString("lat", lat);
		myAddress.getBundle().putString("long", lng);
		return myAddress;
	}

	public void triggerLocation(double lat, double lng) {
		d_lat = lat;
		d_lng = lng;
		AddressTaskLoad taskLoad = new AddressTaskLoad();
		taskLoad.execute();
	}

	private class AddressTaskLoad extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			GetAddress(d_lat, d_lng);

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			control();
		}
	}

	@Override
	public void updateView(SimiCollection collection) {
		super.updateView(collection);
		this.mCollection = collection;
		getListCountryAllowed();
	}

	public void GetAddress(double lat, double lon) {
		Geocoder geocoder = new Geocoder(mContext);
		try {
			List<Address> addresses = geocoder.getFromLocation(d_lat, d_lng, 1);
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
				// ret = "Can't get Address!";
			}
		} catch (IOException e) {
			// ret = "Can't get Address!";
		}
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

	protected View findViewById(String id) {
		View view = mView.findViewById(Rconfig.getInstance().id(id));
		return view;
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
		if (Utils.validateString(mCountryID)
				&& Utils.validateString(getCountryName(mCountryID))) {
			((TextView) findViewById("tv_country"))
					.setText(getCountryName(mCountryID));
			ArrayList<String> states = getStateFromCountry(
					getCountryName(mCountryID), mCountryAllowed);
			String _state = "";
			if (null != states && states.size() > 0) {
				_state = states.get(0);
				et_state.setVisibility(View.GONE);
				rl_state.setVisibility(View.VISIBLE);
				for (String stateItem : states) {
					if (Utils.validateString(mCity) && stateItem.equals(mCity)) {
						_state = stateItem;
					}
				}
				tv_state.setText(_state);
			} else {
				_state = "";
				et_state.setVisibility(View.VISIBLE);
				et_state.setHint(Config.getInstance().getText("State"));
				rl_state.setVisibility(View.GONE);
			}
		}
	}
}
