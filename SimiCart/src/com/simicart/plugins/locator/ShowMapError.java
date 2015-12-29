package com.simicart.plugins.locator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;

import com.simicart.core.config.Config;


public class ShowMapError {
	private Activity mActivity;

	public ShowMapError(Activity activity) {
		mActivity = activity;
	}

	public void showDiagloError(String title, String message) {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(mActivity);
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

	public void showGpsError() {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(mActivity);
		builder1.setTitle(Config.getInstance().getText(
				"Location services disabled"));
		builder1.setMessage(Config.getInstance().getText("Store Locator")
				+ Config.getInstance()
						.getText(
								" needs access to your location. Please turn on location access."));
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
						mActivity.startActivity(intent);
					}
				});

		AlertDialog alert11 = builder1.create();
		alert11.show();
	}
}
