package com.simicart.plugins.locator.fragment;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.common.DrawableManager;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.locator.DataLocator;
import com.simicart.plugins.locator.MathForDummies;
import com.simicart.plugins.locator.RoundedImageView;
import com.simicart.plugins.locator.ShowMapError;
import com.simicart.plugins.locator.StoreParser;
import com.simicart.plugins.locator.entity.StoreObject;

public class MapViewFragment extends SimiFragment {
	private View view;
	MapView mMapView = null;
	private Bundle bundle;
	GoogleMap ggmap;
	StoreObject storeObject;
	private int page = 0;
	List<StoreObject> store_maker;
	LatLng start;
	Activity mActivity;

	public static MapViewFragment newInstance(StoreObject storeObject) {
		MapViewFragment map = new MapViewFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable(Constants.KeyData.STORE_OBJECT, storeObject);
		map.setArguments(bundle);
		// map.storeObject = storeObject;
		return map;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		bundle = savedInstanceState;
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(
				Rconfig.getInstance().getId("plugins_storelocator_map_view",
						"layout"), null);
		if (getArguments() != null) {
			storeObject = (StoreObject) getArguments().getSerializable(
					Constants.KeyData.STORE_OBJECT);
		}

		store_maker = new ArrayList<StoreObject>();
		try {
			MapsInitializer.initialize(getActivity());
		} catch (Exception e) {
		}
		mMapView = (MapView) view.findViewById(Rconfig.getInstance()
				.getIdLayout("map"));
		mMapView.onCreate(bundle);
		mMapView.onResume();

		Double dLat = getLatDoubleWithStore(storeObject);
		Double dLong = getLongDoubleWithStore(storeObject);

		start = new LatLng(dLat, dLong);

		ggmap = mMapView.getMap();
		if (ggmap == null) {
			new ShowMapError(mActivity).showDiagloError(
					Config.getInstance().getText("Error"),
					Config.getInstance().getText(
							"First, You must update Google Maps."));
			;
			return view;
		}
		ggmap.getUiSettings().setMyLocationButtonEnabled(false);
		ggmap.setMyLocationEnabled(true);
		ggmap.addMarker(new MarkerOptions().position(start).icon(
				BitmapDescriptorFactory.fromResource(Rconfig.getInstance()
						.getIdDraw("plugins_locator_maker_store"))));
		CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(
				start.latitude, start.longitude));
		CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
		ggmap.moveCamera(center);
		ggmap.animateCamera(zoom);
		ggmap.setOnCameraChangeListener(new OnCameraChangeListener() {

			@Override
			public void onCameraChange(CameraPosition camera) {
				TaskLoadMaker loadMaker = new TaskLoadMaker();
				loadMaker.data = putData(
						String.valueOf(camera.target.latitude),
						String.valueOf(camera.target.longitude),
						String.valueOf(page * 10));
				loadMaker.execute();
			}
		});
		ggmap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

			@Override
			public void onInfoWindowClick(Marker maker) {
				for (int i = 0; i < store_maker.size(); i++) {

					StoreObject store = store_maker.get(i);
					Double dLat = getLatDoubleWithStore(store);
					Double dLong = getLongDoubleWithStore(store);

					LatLng latLng = maker.getPosition();
					Double roundLatMaker = MathForDummies.round(
							latLng.latitude, 5);
					Double roundLongMaker = MathForDummies.round(
							latLng.longitude, 5);

					Double roundLat = MathForDummies.round(dLat, 5);
					Double roundLong = MathForDummies.round(dLong, 5);

					if (roundLatMaker == roundLat
							&& roundLongMaker == roundLong) {
						StoreDetailFragment detail = StoreDetailFragment
								.newInstance(store);
						SimiManager.getIntance().addFragmentSub(detail);
					}

				}
			}
		});
		ggmap.setInfoWindowAdapter(new InfoWindowAdapter() {

			@Override
			public View getInfoWindow(Marker arg0) {
				return null;
			}

			@Override
			public View getInfoContents(Marker marker) {
				return gotInforContent(marker);
			}
		});
		return view;
	}

	protected View gotInforContent(Marker marker) {
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		int idView = Rconfig.getInstance().layout(
				"plugins_storelocator_info_window_layout");
		View v = inflater.inflate(idView, null);
		v.setMinimumWidth(200);
		v.setBackgroundColor(getResources().getColor(android.R.color.white));
		TextView tvName = (TextView) v.findViewById(Rconfig.getInstance()
				.getIdLayout("tv_name"));
		TextView tvAddress = (TextView) v.findViewById(Rconfig.getInstance()
				.getIdLayout("tv_address"));
		RoundedImageView img = (RoundedImageView) v.findViewById(Rconfig
				.getInstance().getIdLayout("img_store"));

		LatLng latLngMarker = marker.getPosition();
		Double latMarker = latLngMarker.latitude;
		Double longMarker = latLngMarker.longitude;
		Double latCurrent = StoreLocatorFragment.currrentLocation.getLatitude();
		Double longCurrent = StoreLocatorFragment.currrentLocation
				.getLongitude();

		Double roundLatMarker = MathForDummies.round(latMarker, 5);
		Double roundLongMarker = MathForDummies.round(longMarker, 5);
		Double roundLatCurrent = MathForDummies.round(latCurrent, 5);
		Double roundLongCurrent = MathForDummies.round(longCurrent, 5);

		if (roundLatMarker == roundLatCurrent
				&& roundLongMarker == roundLongCurrent) {
			tvName.setText(Config.getInstance().getText("You are here"));
			tvAddress.setText("");
		} else if (roundLatMarker != roundLatCurrent
				&& roundLongMarker != roundLongCurrent) {
			for (int i = 0; i < store_maker.size(); i++) {

				StoreObject store = store_maker.get(i);

				Double latStore = getLatDoubleWithStore(store);
				Double longStore = getLongDoubleWithStore(store);

				Double roundLatStore = MathForDummies.round(latStore, 5);
				Double roundLongStore = MathForDummies.round(longStore, 5);

				if (roundLatStore == roundLatMarker
						&& roundLongStore == roundLongMarker) {
					tvName.setText(store_maker.get(i).getName());
					tvAddress.setText(DataLocator.convertAddress(store));

					String urlImage = store.getImage_icon();

					if (Utils.validateString(urlImage)) {
						DrawableManager
								.fetchItemDrawableOnThread(urlImage, img);
					}
				} else {
					img.setImageDrawable(getResources().getDrawable(
							Rconfig.getInstance().getIdDraw(
									"plugins_locator_ic_store_android")));
				}

			}
		}

		return v;
	}

	protected Double getLatDoubleWithStore(StoreObject store) {
		String latitude = store.getLatitude();
		if (Utils.validateString(latitude)) {
			latitude = latitude.trim();

			Double dLat = convertToDouble(latitude);
			return dLat;
		}

		return null;
	}

	protected Double getLongDoubleWithStore(StoreObject store) {
		String longtitude = store.getLongtitude();
		if (Utils.validateString(longtitude)) {
			longtitude = longtitude.trim();
			Double dLong = convertToDouble(longtitude);
			return dLong;
		}

		return null;
	}

	private JSONObject putData(String lat, String lng, String offset) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("lat", lat);
			jsonObject.put("lng", lng);
			jsonObject.put("offset", offset);
			jsonObject.put("limit", "10");
		} catch (Exception e) {
		}
		return jsonObject;
	}

	private void addMaker() {

		for (int i = 0; i < store_maker.size(); i++) {

			StoreObject store = store_maker.get(i);
			String latitude = store.getLatitude();
			String longtitude = store.getLongtitude();

			if (Utils.validateString(latitude)
					&& Utils.validateString(longtitude)) {

				latitude = latitude.trim();
				longtitude = longtitude.trim();

				Double dLat = convertToDouble(latitude);
				Double dLong = convertToDouble(longtitude);

				LatLng end = new LatLng(dLat, dLong);
				MarkerOptions options = new MarkerOptions().position(end).icon(
						BitmapDescriptorFactory.fromResource(Rconfig
								.getInstance().drawable(
										"plugins_locator_maker_default")));
				ggmap.addMarker(options);
			}

		}

	}

	protected Double convertToDouble(String source) {
		DecimalFormat df = new DecimalFormat();
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		Double target = null;
		try {
			target = df.parse(source).doubleValue();
		} catch (ParseException e) {
			Log.e("MapViewFragment ",
					"=======================> convertToDouble "
							+ e.getMessage());
		}

		return target;
	}

	public class TaskLoadMaker extends AsyncTask<Void, Void, JSONObject> {
		JSONObject data;

		@Override
		protected JSONObject doInBackground(Void... params) {
			return StoreLocatorFragment.getJon(data,
					StoreLocatorFragment.url_list_store);

		}

		@Override
		protected void onPostExecute(JSONObject result) {
			StoreParser parser = new StoreParser();
			List<StoreObject> stores = parser.getResult(result);

			if (null != stores && stores.size() > 0) {
				for (int i = 0; i < stores.size(); i++) {
					StoreObject store = stores.get(i);

					if (StoreLocatorFragment.check(store, store_maker) == 0) {

						String langtitude = store.getLatitude();
						String longtitude = store.getLongtitude();
						if (Utils.validateString(langtitude)
								&& Utils.validateString(longtitude)) {
							langtitude = langtitude.trim();
							longtitude = longtitude.trim();
							Double dLang = convertToDouble(langtitude);
							Double dLong = convertToDouble(longtitude);
							if (dLang != 0 && dLong != 0) {
								store_maker.add(store);
							}

						}
					}

				}
				addMaker();
			}
			super.onPostExecute(result);
		}

	}

}
