package com.simicart.plugins.locator.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.location.Location;
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
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.locator.DataLocator;
import com.simicart.plugins.locator.MathForDummies;
import com.simicart.plugins.locator.RoundedImageView;
import com.simicart.plugins.locator.StoreParser;
import com.simicart.plugins.locator.entity.StoreObject;

public class MyMapFragment extends SimiFragment {
	View view;
	TextView txt_km;
	MapView mMapView = null;
	private Bundle bundle;
	GoogleMap ggmap;
	Location _mLocation;
	LatLng _mLatLng;
	int page = 1;
	List<StoreObject> store_maker;

	public static MyMapFragment newInstance(Location mLocation) {
		MyMapFragment map = new MyMapFragment();
		map._mLocation = mLocation;
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
				Rconfig.getInstance().getId("plugins_storelocator_map_view", "layout"),
				null);
		store_maker = new ArrayList<StoreObject>();
		txt_km = (TextView) view.findViewById(Rconfig.getInstance()
				.getIdLayout("txt_km"));
		try {
			MapsInitializer.initialize(getActivity());
		} catch (Exception e) {
		}
		mMapView = (MapView) view.findViewById(Rconfig.getInstance()
				.getIdLayout("map"));
		mMapView.onCreate(bundle);
		mMapView.onResume();
		ggmap = mMapView.getMap();
		if (ggmap != null) {
			setUpMap();
		}
		return view;
	}

	private void addMaker() {
		Log.e("Add maker","GGG" + store_maker.size());
		try {
			if (_mLatLng != null) {
				ggmap.addMarker(new MarkerOptions().position(_mLatLng).icon(
						BitmapDescriptorFactory.fromResource(Rconfig
								.getInstance().getIdDraw("maker_my"))));
			}
			for (int i = 0; i < store_maker.size(); i++) {
				LatLng end = new LatLng(Double.parseDouble(store_maker.get(i)
						.getLatitude()), Double.parseDouble(store_maker.get(i)
						.getLongtitude()));
				MarkerOptions options = new MarkerOptions().position(end).icon(
						BitmapDescriptorFactory.fromResource(Rconfig
								.getInstance().getIdDraw("plugins_locator_maker_default")));
				ggmap.addMarker(options);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		if (ggmap != null)
			setUpMap();

		if (ggmap == null) {
			try {
				MapsInitializer.initialize(getActivity());
			} catch (Exception e) {
			}
			mMapView = (MapView) view.findViewById(Rconfig.getInstance()
					.getIdLayout("map"));
			mMapView.onCreate(bundle);
			mMapView.onResume();
			ggmap = mMapView.getMap();
			// Check if we were successful in obtaining the map.
			if (ggmap != null)
				setUpMap();
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (ggmap != null) {
			ggmap = null;
		}
	}

	public void setUpMap() {
		ggmap.getUiSettings().setMyLocationButtonEnabled(false);
		ggmap.setMyLocationEnabled(true);
		if (_mLocation == null) {
			_mLatLng = new LatLng(53.6846608, 9.9843296);
		} else {
			_mLatLng = new LatLng(_mLocation.getLatitude(),
					_mLocation.getLongitude());
		}
		try {
			Log.e("add marker","SEtupmap");
			addMaker();
		} catch (Exception e) {
			e.printStackTrace();
		}

		CameraUpdate center = CameraUpdateFactory.newLatLng(_mLatLng);
		CameraUpdate zoom = CameraUpdateFactory.zoomTo(2);

		ggmap.moveCamera(center);
		ggmap.animateCamera(zoom);
		ggmap.setOnCameraChangeListener(new OnCameraChangeListener() {

			@Override
			public void onCameraChange(CameraPosition camera) {
				TaskLoadMaker taskLoad = new TaskLoadMaker();
				taskLoad.data = putData(String.valueOf(camera.target.latitude),
						String.valueOf(camera.target.longitude),
						String.valueOf(page * 10));
				taskLoad.execute();
			}
		});
		ggmap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

			@Override
			public void onInfoWindowClick(Marker maker) {
				for (int i = 0; i < store_maker.size(); i++) {
					if (MathForDummies.round(Double.parseDouble(store_maker
							.get(i).getLatitude()), 5) == MathForDummies.round(
							maker.getPosition().latitude, 5)
							&& MathForDummies.round(Double
									.parseDouble(store_maker.get(i)
											.getLongtitude()), 5) == MathForDummies
									.round(maker.getPosition().longitude, 5)) {
						StoreDetail detail = StoreDetail
								.newInstance(store_maker.get(i));
						SimiManager.getIntance().addFragmentSub(detail);
						break;
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
			public View getInfoContents(Marker arg0) {
				LayoutInflater inflater = (LayoutInflater) getActivity()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View v = inflater.inflate(
						Rconfig.getInstance().getId(
								"plugins_storelocator_info_window_layout", "layout"),
						null);
				v.setMinimumWidth(200);
				v.setBackgroundColor(getResources().getColor(
						android.R.color.white));
				TextView tvName = (TextView) v.findViewById(Rconfig
						.getInstance().getIdLayout("tv_name"));
				TextView tvAddress = (TextView) v.findViewById(Rconfig
						.getInstance().getIdLayout("tv_address"));
				RoundedImageView img = (RoundedImageView) v
						.findViewById(Rconfig.getInstance().getIdLayout(
								"img_store"));
				if (MathForDummies.round(arg0.getPosition().latitude, 5) == MathForDummies
						.round(_mLatLng.latitude, 5)
						&& MathForDummies.round(arg0.getPosition().longitude, 5) == MathForDummies
								.round(_mLatLng.longitude, 5)) {
					tvName.setText(Config.getInstance().getText("You are here"));
					tvAddress.setText("");
				} else {
					for (int i = 0; i < store_maker.size(); i++) {
						if (Long.parseLong(store_maker.get(i).getLatitude()
								.replaceAll("\\.", "")) != 0
								&& Long.parseLong(store_maker.get(i)
										.getLongtitude().replaceAll("\\.", "")) != 0) {
							if (MathForDummies.round(Double
									.parseDouble(store_maker.get(i)
											.getLatitude()), 5) == MathForDummies
									.round(arg0.getPosition().latitude, 5)
									&& MathForDummies.round(
											Double.parseDouble(store_maker.get(
													i).getLongtitude()), 5) == MathForDummies.round(
											arg0.getPosition().longitude, 5)) {

								tvName.setText(store_maker.get(i).getName());
								tvAddress.setText(DataLocator
										.convertAddress(store_maker.get(i)));
								if (store_maker.get(i).getImage_icon() != null
										&& !store_maker.get(i).getImage_icon()
												.equals("")) {
									DrawableManager.fetchItemDrawableOnThread(
											store_maker.get(i).getImage_icon(),
											img);
								}
							} else {
								img.setImageDrawable(getResources()
										.getDrawable(
												Rconfig.getInstance()
														.getIdDraw(
																"plugins_locator_ic_store_android")));
							}
						}
					}
				}

				return v;
			}
		});

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
			if (parser.getResult(result) != null
					&& parser.getResult(result).size() != 0) {
				for (int i = 0; i < parser.getResult(result).size(); i++) {
					if (StoreLocatorFragment.check(parser.getResult(result)
							.get(i), store_maker) == 0) {
						if (Long.parseLong(parser.getResult(result).get(i)
								.getLatitude().replaceAll("\\.", "")) != 0
								&& Long.parseLong(parser.getResult(result)
										.get(i).getLongtitude()
										.replaceAll("\\.", "")) != 0) {
							store_maker.add(parser.getResult(result).get(i));
						}
					}
				}
				Log.e("addd maker", "load xong");
				addMaker();
			}
			super.onPostExecute(result);
		}

	}

}
