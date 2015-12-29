package com.simicart.plugins.locationpickup.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.fragment.NewAddressBookFragment;
import com.simicart.plugins.locationpickup.block.LocationPickupBlock;
import com.simicart.plugins.locationpickup.controller.LocationPickupController;

public class LocationPickupFragment extends NewAddressBookFragment {
	protected GoogleMap ggmap;
	protected MapView map;
	protected ScrollView scroll;
	protected LocationPickupBlock mBlock;
	protected LocationPickupController mController;

	public static LocationPickupFragment newInstance() {
		LocationPickupFragment fragment = new LocationPickupFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MapsInitializer.initialize(getActivity());
		LocationPickupBlock.setLattitude("");
		LocationPickupBlock.setLongtitude("");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				Rconfig.getInstance().layout("plugins_locationpickup_layout"),
				container, false);
		Context context = getActivity();
		SimiManager.getIntance().getChilFragmentManager();
		scroll = (ScrollView) view.findViewById(Rconfig.getInstance().id(
				"scrollView"));
		map = (MapView) view.findViewById(Rconfig.getInstance().id("map"));
		map.onCreate(savedInstanceState);
		ggmap = map.getMap();
		ggmap.getUiSettings().setMyLocationButtonEnabled(false);
		ggmap.getUiSettings().setZoomControlsEnabled(false);
		ggmap.setMyLocationEnabled(false);
		if (ggmap == null) {
			showDiagloError(
					Config.getInstance().getText("Error"),
					Config.getInstance().getText(
							"First, You must update Google Maps."));
			return rootView;
		}

		mBlock = new LocationPickupBlock(view, context);
		mBlock.setAfterController(afterControl);
		mBlock.setGgmap(ggmap);
		mBlock.initView();

		if (mController == null) {
			mController = new LocationPickupController();
			mController.setDelegate(mBlock);
			mController.setAfterController(afterControl);
			mController.setAddressFor(this.addressFor);
			mController.setBillingAddress(mBillingAddress);
			mController.setShippingAddress(mShippingAddress);
			mController.onStart();
		} else {
			mController.setDelegate(mBlock);
			mController.onResume();
		}

		mBlock.setControllerDelegate(mController);
		mBlock.setSaveAddress(mController.getClickSave());
		mBlock.setChooseCountry(mController.getChooseCountry());
		mBlock.setChooseStates(mController.getChooseStates());
		return view;
	}

	public void showDiagloError(String title, String message) {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
		builder1.setTitle(title);
		builder1.setMessage(message);
		builder1.setCancelable(true);
		builder1.setPositiveButton(Config.getInstance().getText("OK"),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		AlertDialog alert11 = builder1.create();
		alert11.show();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		map.onResume();
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		map.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		map.onLowMemory();
	}
}
