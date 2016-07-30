package com.simicart.plugins.locator.block;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.common.DrawableManager;
import com.simicart.core.common.GPSTracker;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.locator.common.DataLocator;
import com.simicart.plugins.locator.delegate.StoreLocatorMapDelegate;
import com.simicart.plugins.locator.entity.StoreObject;
import com.simicart.plugins.locator.fragment.StoreDetailFragment;
import com.simicart.plugins.locator.style.RoundedImageView;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class StoreLocatorMapBlock extends SimiBlock implements StoreLocatorMapDelegate {

	protected MapView map;
	protected TextView tvDistance;
	protected Bundle bundle;
	protected ArrayList<StoreObject> listStore;
	protected GoogleMap googleMap;
	protected Location currrentLocation = null;
	protected LatLng end;

	public StoreLocatorMapBlock(View view, Context context) {
		super(view, context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		map = (MapView) mView.findViewById(Rconfig.getInstance().getIdLayout("map"));
		tvDistance = (TextView) mView.findViewById(Rconfig.getInstance().getIdLayout("txt_km"));
		listStore = new ArrayList<>();

		Handler mapSetupHandler = new Handler();
		Runnable mapSetupRunnable = new Runnable() {
			public void run() {
				try {
					MapsInitializer.initialize(mContext);
				} catch (Exception e) {
				}
				map.onCreate(bundle);
				map.onResume();
				googleMap = map.getMap();
				googleMap.getUiSettings().setMyLocationButtonEnabled(false);
				googleMap.setMyLocationEnabled(true);
				// map.getMyLocation().getLatitude();
				googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {

					@Override
					public boolean onMarkerClick(Marker marker) {
						marker.showInfoWindow();
						return true;
					}
				});
				googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

					@Override
					public void onInfoWindowClick(Marker maker) {
						for (int i = 0; i < listStore.size(); i++) {

							StoreObject store = listStore.get(i);
							String latitude = store.getLatitude();
							String longtitude = store.getLongtitude();
							if (Utils.validateString(latitude) && Utils.validateString(longtitude)) {
								latitude = latitude.trim();
								longtitude = longtitude.trim();

								String dLat = latitude;
								String dLong = longtitude;

								String makerLat = String.valueOf(maker.getPosition().latitude);
								String makerLong = String.valueOf(maker.getPosition().longitude);

								// Double roundLat = MathForDummies
								// .round(dLat, 5);
								// Double roundLong = MathForDummies
								// .round(dLong, 5);

								if (dLat.equals(makerLat) && dLong.equals(makerLong)) {
									StoreDetailFragment detail = StoreDetailFragment.newInstance(store);
									if (DataLocal.isTablet) {
										SimiManager.getIntance().addPopupFragment(detail);
									} else {
										SimiManager.getIntance().replaceFragment(detail);
									}
									return;
								}

							}

						}
					}
				});
				googleMap.setInfoWindowAdapter(new InfoWindowAdapter() {

					@Override
					public View getInfoWindow(Marker arg0) {
						return null;
					}

					@Override
					public View getInfoContents(Marker marker) {
						return gotInforContent(marker);
					}
				});

				if (currrentLocation == null) {
					currrentLocation = new Location("");
				}
				GPSTracker gpsTracker = new GPSTracker(mContext);
				Location location = gpsTracker.getLocation();
				if (location != null) {
					currrentLocation.setLongitude(location.getLongitude());
					currrentLocation.setLatitude(location.getLatitude());
				}
				if (currrentLocation != null) {
					googleMap.addMarker(new MarkerOptions()
							.position(new LatLng(currrentLocation.getLatitude(), currrentLocation.getLongitude()))
							.icon(BitmapDescriptorFactory.fromResource(Rconfig.getInstance().getIdDraw("maker_my"))));

//					googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
//							new LatLng(currrentLocation.getLatitude(), currrentLocation.getLongitude()), 14));
				}

			}
		};
		mapSetupHandler.post(mapSetupRunnable);

	}

	@Override
	public void drawView(SimiCollection collection) {
		// TODO Auto-generated method stub
	}

	private void getAllPointFromStoreMarker(List<StoreObject> mStore_maker) {
		try {
			if (currrentLocation == null) {
				currrentLocation = new Location("");
			}
			double dLong = 0;
			double dLat = 0;
			Log.e("abc", "++" + mStore_maker.size());
			if (mStore_maker.size() > 0) {
				for (int i = 0; i < mStore_maker.size(); i++) {
					StoreObject store = mStore_maker.get(i);

					String longtitude = store.getLongtitude();
					String latitude = store.getLatitude();
					if (Utils.validateString(longtitude) && Utils.validateString(latitude)) {
						longtitude = longtitude.trim();
						latitude = latitude.trim();

						dLong += convertToDouble(longtitude);
						dLat += convertToDouble(latitude);

					}
				}
				dLong = dLong / mStore_maker.size();
				dLat = dLat / mStore_maker.size();
				currrentLocation.setLongitude(dLong);
				currrentLocation.setLatitude(dLat);
			} else {
				GPSTracker gpsTracker = new GPSTracker(mContext);
				Location location = gpsTracker.getLocation();
				if (location != null) {
					currrentLocation.setLongitude(location.getLongitude());
					currrentLocation.setLatitude(location.getLatitude());
				}
			}
		} catch (Exception e) {

		}
	}

	private void addMaker(ArrayList<StoreObject> listStores) {
		// if (currrentLocation != null) {
		// googleMap.addMarker(new MarkerOptions()
		// .position(new LatLng(currrentLocation.getLatitude(),
		// currrentLocation.getLongitude()))
		// .icon(BitmapDescriptorFactory.fromResource(Rconfig.getInstance().getIdDraw("maker_my"))));
		// }
		for (int i = 0; i < listStores.size(); i++) {

			StoreObject store = listStores.get(i);
			String latitude = store.getLatitude();
			String longtitude = store.getLongtitude();
			if (Utils.validateString(latitude) && Utils.validateString(longtitude)) {

				latitude = latitude.trim();
				longtitude = longtitude.trim();
				try {

					Double dLat = convertToDouble(latitude);
					Double dLong = convertToDouble(longtitude);

					LatLng end = new LatLng(dLat, dLong);
					MarkerOptions options = new MarkerOptions().position(end).icon(BitmapDescriptorFactory
							.fromResource(Rconfig.getInstance().drawable("plugins_locator_maker_default")));
					googleMap.addMarker(options);
				} catch (Exception e) {
					Log.e("StoreLocatorFragment ", "=====================> addMaker Exceptin " + e.getMessage());
				}
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
			Log.e("MapViewFragment ", "=======================> convertToDouble " + e.getMessage());
		}

		return target;
	}

	protected View gotInforContent(Marker marker) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		int idView = Rconfig.getInstance().layout("plugins_store_locator_info_window_layout");
		View v = inflater.inflate(idView, null);
		v.setMinimumWidth(200);
		v.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
		TextView tvName = (TextView) v.findViewById(Rconfig.getInstance().getIdLayout("tv_name"));
		TextView tvAddress = (TextView) v.findViewById(Rconfig.getInstance().getIdLayout("tv_address"));
		RoundedImageView img = (RoundedImageView) v.findViewById(Rconfig.getInstance().getIdLayout("img_store"));

		LatLng latLngMarker = marker.getPosition();
		String latMarker = String.valueOf(latLngMarker.latitude);
		String longMarker = String.valueOf(latLngMarker.longitude);
		String latCurrent = String.valueOf(currrentLocation.getLatitude());
		String longCurrent = String.valueOf(currrentLocation.getLongitude());

		Log.e("latMarker", "" + latMarker);
		Log.e("longMarker", "" + longMarker);

		// Double roundLatMarker = MathForDummies.round(latMarker, 6);
		// Double roundLongMarker = MathForDummies.round(longMarker, 6);
		// Double roundLatCurrent = MathForDummies.round(latCurrent, 6);
		// Double roundLongCurrent = MathForDummies.round(longCurrent, 6);

		if (latMarker.equals(latCurrent) && longMarker.equals(longCurrent)) {
			tvName.setText(Config.getInstance().getText("You are here"));
			tvAddress.setText("");
		} else if (!latMarker.equals(latCurrent) && !longMarker.equals(longCurrent)) {

			for (StoreObject store : listStore) {

				// StoreObject store = listStore.get(i);

				String latStore = store.getLatitude();
				String longStore = store.getLongtitude();

				// Double roundLatStore = MathForDummies.round(latStore, 6);
				// Double roundLongStore = MathForDummies.round(longStore, 6);

				if (latMarker.equals(latStore) && longMarker.equals(longStore)) {
					tvName.setText(store.getName());
					tvAddress.setText(DataLocator.convertAddress(store));

					String urlImage = store.getImage_icon();

					if (Utils.validateString(urlImage)) {
						DrawableManager.fetchItemDrawableOnThread(urlImage, img);
					}

					return v;

				} else {
					img.setImageDrawable(mContext.getResources()
							.getDrawable(Rconfig.getInstance().getIdDraw("plugins_locator_ic_store_android")));
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

	@Override
	public void addMarkerToMap(ArrayList<StoreObject> listStores) {
		// TODO Auto-generated method stub
		googleMap.clear();
		listStore = listStores;

		// getAllPointFromStoreMarker(listStore);
		addMaker(listStores);

		// CameraUpdate zoom = CameraUpdateFactory.zoomTo(5);
		// googleMap.animateCamera(zoom);
		// if (currrentLocation != null) {
		// CameraUpdate center = CameraUpdateFactory
		// .newLatLng(new LatLng(currrentLocation.getLatitude(),
		// currrentLocation.getLongitude()));
		//
		// googleMap.moveCamera(center);
		// zoom = CameraUpdateFactory.zoomTo(5);
		// Log.d("quangdd123", "currrentLocation==" +
		// currrentLocation.toString());
		//
		// }

		LatLngBounds.Builder builder = new LatLngBounds.Builder();
		LatLng position;
		for (int i = 0; i < listStores.size(); i++) {
			StoreObject store = listStores.get(i);
			Log.e("abc", "Lat====" + store.getLatitude() + "====Long====" + store.getLongtitude());
			position = new LatLng(getLatDoubleWithStore(store), getLongDoubleWithStore(store));
			builder.include(new LatLng(position.latitude, position.longitude));
		}
		LatLngBounds bounds = builder.build();
		googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 5));

	}

}
