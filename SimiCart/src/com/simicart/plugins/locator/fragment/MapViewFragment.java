package com.simicart.plugins.locator.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
		map.storeObject = storeObject;
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
		store_maker = new ArrayList<StoreObject>();
		try {
			MapsInitializer.initialize(getActivity());
		} catch (Exception e) {
		}
		mMapView = (MapView) view.findViewById(Rconfig.getInstance()
				.getIdLayout("map"));
		mMapView.onCreate(bundle);
		mMapView.onResume();
		start = new LatLng(Double.parseDouble(storeObject.getLatitude()),
				Double.parseDouble(storeObject.getLongtitude()));

		ggmap = mMapView.getMap();
		if (ggmap == null) {
			new ShowMapError(mActivity).showDiagloError(
					Config.getInstance().getText("Error"),
					Config.getInstance().getText(
							"First, You must update Google Maps."));
			;
			Log.e("ggmap", "hhee");
			return view;
		}
		ggmap.getUiSettings().setMyLocationButtonEnabled(false);
		ggmap.setMyLocationEnabled(true);
		ggmap.addMarker(new MarkerOptions().position(start).icon(
				BitmapDescriptorFactory.fromResource(Rconfig.getInstance()
						.getIdDraw("plugins_locator_maker_store"))));
		CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(
				start.latitude, start.longitude));
		CameraUpdate zoom = CameraUpdateFactory.zoomTo(2);
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
								"plugins_storelocator_info_window_layout",
								"layout"), null);
				v.setMinimumWidth(200);
				v.setBackgroundColor(getResources().getColor(
						android.R.color.white));
				TextView tvName = (TextView) v.findViewById(Rconfig
						.getInstance().getIdLayout("tv_name"));
				TextView tvAddress = (TextView) v.findViewById(Rconfig
						.getInstance().getIdLayout("tv_address"));
				ImageView img = (ImageView) v.findViewById(Rconfig
						.getInstance().getIdLayout("img_store"));
				LatLng latLng = arg0.getPosition();
				for (int i = 0; i < store_maker.size(); i++) {
					if (Long.parseLong(store_maker.get(i).getLatitude()
							.replaceAll("\\.", "")) != 0
							&& Long.parseLong(store_maker.get(i)
									.getLongtitude().replaceAll("\\.", "")) != 0) {
						if (MathForDummies.round(Double.parseDouble(store_maker
								.get(i).getLatitude()), 5) == MathForDummies
								.round(arg0.getPosition().latitude, 5)
								&& MathForDummies.round(Double
										.parseDouble(store_maker.get(i)
												.getLongtitude()), 5) == MathForDummies
										.round(arg0.getPosition().longitude, 5)) {
							if (latLng
									.equals(StoreLocatorFragment.currrentLocation)) {
								if (StoreLocatorFragment.currrentLocation != null) {
									tvName.setText(Config.getInstance()
											.getText("You are here!"));
									tvAddress.setText("");
								}
							} else {
								tvName.setText(store_maker.get(i).getName());
								tvAddress.setText(DataLocator
										.convertAddress(store_maker.get(i)));
							}
							if (store_maker.get(i).getImage_icon() != null
									&& !store_maker.get(i).getImage_icon()
											.equals("")) {
								DrawableManager
										.fetchItemDrawableOnThread(store_maker
												.get(i).getImage_icon(), img);
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
		return view;
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
			if (!storeObject.getLatitude().equals(
					store_maker.get(i).getLatitude())
					&& !storeObject.getLongtitude().equals(
							store_maker.get(i).getLongtitude())) {
				LatLng end = new LatLng(Double.parseDouble(store_maker.get(i)
						.getLatitude()), Double.parseDouble(store_maker.get(i)
						.getLongtitude()));
				MarkerOptions options = new MarkerOptions().position(end).icon(
						BitmapDescriptorFactory.fromResource(Rconfig
								.getInstance().getIdDraw(
										"plugins_locator_maker_default")));
				ggmap.addMarker(options);
			}
		}

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
				addMaker();
			}
			super.onPostExecute(result);
		}

	}

}
