package com.simicart.core.notification.controller;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.simicart.MainActivity;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.catalog.category.fragment.CategoryFragment;
import com.simicart.core.catalog.product.fragment.ProductDetailParentFragment;
import com.simicart.core.catalog.search.fragment.ListProductFragment;
import com.simicart.core.catalog.search.model.ConstantsSearch;
import com.simicart.core.common.DrawableManager;
import com.simicart.core.common.GPSTracker;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.notification.GCMIntentService;
import com.simicart.core.notification.common.ServerUtilities;
import com.simicart.core.notification.entity.NotificationEntity;
import com.simicart.core.notification.fragment.WebviewFragment;
import com.simicart.core.notification.gcm.GCMRegistrar;
import com.simicart.core.slidemenu.fragment.CateSlideMenuFragment;

public class NotificationController {

	private String TAG = "Notification";
	public static final String PROPERTY_REG_ID = "registration_id";
	String s_latitude = "";
	String s_longitude = "";

	FragmentActivity mActivity;
	Context mContext;

	String regId;
	GoogleCloudMessaging gcm;

	AsyncTask<Void, Void, Void> mRegisterTask;

	public NotificationController(FragmentActivity activity) {
		TAG = getClass().getName();
		this.mActivity = activity;
		this.mContext = MainActivity.context;

		GPSTracker gps = new GPSTracker(activity);
		// check if GPS enabled
		if (gps.canGetLocation()) {
			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();
			s_latitude = Double.toString(latitude);
			s_longitude = Double.toString(longitude);
			Log.e(TAG, "Your Location is - \nLat: " + s_latitude + "\nLong: "
					+ s_longitude);
		} else {
			Log.e("ServerUtilities",
					"GPS is not enabled. Please enable it to get your current location");
		}
	}

	public void registerNotification() {
		checkNotNull(Config.getInstance().getSenderId(), "SENDER_ID");
		Log.e(getClass().getName(), "SENDER_ID:   "
				+ Config.getInstance().getSenderId());
		GCMRegistrar.checkDevice(mContext);
		GCMRegistrar.checkManifest(mContext);

		regId = GCMRegistrar.getRegistrationId(mContext);
		Log.e(getClass().getName(), "regId:   " + regId);
		// send deviceid to server
		if (regId.equals("")) {
			// Automatically registers application on startup.
			GCMRegistrar.register(mContext, Config.getInstance().getSenderId());
			Log.e(getClass().getName(),
					"Automatically registers application on startup.");
		} else {
			// Device is already registered on GCM, check server.
			// Log.e(getClass().getName(),
			// "Device is already registered on GCM, check server.");
			// if (GCMRegistrar.isRegisteredOnServer(mContext)) {
			// // Skips registration.
			// Log.e(getClass().getName(),
			// "The device is registered On Server");
			// } else {
			Log.e(getClass().getName(), "Try to register again");
			// Try to register again, but not in the UI thread.
			// It's also necessary to cancel the thread onDestroy(),
			// hence the use of AsyncTask instead of a raw thread.
			mRegisterTask = new AsyncTask<Void, Void, Void>() {

				@Override
				protected Void doInBackground(Void... params) {

					// At this point all attempts to register with the app
					// server failed, so we need to unregister the device
					// from GCM - the app will try to register again when
					// it is restarted. Note that GCM will send an
					// unregistered callback upon completion, but
					// GCMIntentService.onUnregistered() will ignore it.
					// GCMRegistrar.unregister(mContext);

					// Log.e(getClass().getName(), "Try to register again");
					// if (getRegistrationId(mContext).equals("")) {
					// storeRegistrationId(mContext, regId);
					// } else {
					// regId = getRegistrationId(mContext);
					// }
					ServerUtilities.register(mContext, regId, s_latitude,
							s_longitude);
					return null;
				}

				@Override
				protected void onPostExecute(Void result) {
					mRegisterTask = null;
				}

			};
			mRegisterTask.execute(null, null, null);
		}
		// }
	}

	private void checkNotNull(Object reference, String name) {
		if (reference == null) {
			throw new NullPointerException(
					"Please set the %1$s constant and recompile the app."
							+ name);
		}
	}

	public void showNotification(final Activity activity,
			final NotificationEntity notificationData) {

		Dialog alertboxDowload = new Dialog(activity);
		LayoutInflater inflater = activity.getLayoutInflater();
		View view = inflater.inflate(
				Rconfig.getInstance().layout("core_notification_layout"), null);

		ImageView im_notification = (ImageView) view.findViewById(Rconfig
				.getInstance().id("im_notification"));
		TextView tv_notification = (TextView) view.findViewById(Rconfig
				.getInstance().id("tv_notification"));

		if (notificationData.getImage() != null
				&& !notificationData.getImage().equals("")
				&& !notificationData.getImage().toLowerCase().equals("null")) {
			im_notification.setVisibility(View.VISIBLE);
			DrawableManager.fetchDrawableOnThread(notificationData.getImage(),
					im_notification);
		} else {
			im_notification.setVisibility(View.GONE);
		}

		if (notificationData.getMessage() != null
				&& !notificationData.getMessage().equals("")
				&& !notificationData.getMessage().toLowerCase().equals("null")) {
			tv_notification.setVisibility(View.VISIBLE);
			String mes = notificationData.getMessage();
			if (im_notification.getVisibility() == View.VISIBLE) {
				if (mes.length() > 253)
					mes = mes.substring(0, 250) + "...";
			} else {
				if (mes.length() > 503)
					mes = mes.substring(0, 500) + "...";
			}
			tv_notification.setText(mes);
		} else {
			tv_notification.setVisibility(View.GONE);
		}

		alertboxDowload.setContentView(view);
		alertboxDowload.setTitle(Config.getInstance().getText(
				notificationData.getTitle()));
		alertboxDowload.setCancelable(false);

		TextView tv_close = (TextView) view.findViewById(Rconfig.getInstance()
				.id("tv_close"));
		tv_close.setText(Config.getInstance().getText("CLOSE"));
		tv_close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				activity.finish();
				GCMIntentService.notificationData = null;
			}
		});
		TextView tv_show = (TextView) view.findViewById(Rconfig.getInstance()
				.id("tv_show"));
		tv_show.setText(Config.getInstance().getText("SHOW"));
		tv_show.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				activity.finish();
				GCMIntentService.notificationData = null;
				openNotificationDetail(notificationData);
			}
		});

		alertboxDowload.show();
	}

	public void openNotificationSetting(Context context) {
		// String packageName = context.getPackageName();
		// try {
		// // Open the specific App Info page:
		// Intent intent = new Intent(
		// android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		// intent.setData(Uri.parse("package:" + packageName));
		// context.startActivity(intent);
		//
		// } catch (ActivityNotFoundException e) {
		// e.printStackTrace();
		//
		// // Open the generic Apps page:
		// Intent intent = new Intent(
		// android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
		// context.startActivity(intent);
		// }

		if (DataLocal.enableNotification()) {
			DataLocal.saveNotificationSet(false);
			SimiManager.getIntance().showToast("Disable recieve notification");
		} else {
			DataLocal.saveNotificationSet(true);
			SimiManager.getIntance().showToast("Enable recieve notification");
		}
	}

	protected void openNotificationDetail(NotificationEntity notificationData) {
		SimiFragment fragment = null;
		if (notificationData.getType().equals("1")) {
			if (notificationData.getProductID() != null
					&& !notificationData.getProductID().equals("")
					&& !notificationData.getProductID().toLowerCase()
							.equals("null")) {
				fragment = ProductDetailParentFragment.newInstance();
				((ProductDetailParentFragment) fragment)
						.setProductID(notificationData.getProductID());
			}
		} else if (notificationData.getType().equals("2")) {
			if (notificationData.getCategoryID() != null
					&& !notificationData.getCategoryID().toLowerCase()
							.equals("null")) {
				if (notificationData.getHasChild().equals("1")) {
					if (DataLocal.isTablet) {
						fragment = CategoryFragment.newInstance(
								notificationData.getCategoryName(),
								notificationData.getCategoryID());
						CateSlideMenuFragment.getIntance()
								.replaceFragmentCategoryMenu(fragment);
						CateSlideMenuFragment.getIntance().openMenu();
						return;
					} else {
						fragment = CategoryFragment.newInstance(
								notificationData.getCategoryName(),
								notificationData.getCategoryID());
					}
				} else {
					fragment = ListProductFragment.newInstance();
					((ListProductFragment) fragment).setCategoryId(notificationData
							.getCategoryID());
					((ListProductFragment) fragment)
							.setCategoryName(notificationData.getCategoryName());
					((ListProductFragment) fragment)
							.setUrlSearch(ConstantsSearch.url_category);
				}
			}
		} else {
			if (notificationData.getUrl() != null
					&& !notificationData.getUrl().equals("")
					&& !notificationData.getUrl().toLowerCase().equals("null")) {
				if (notificationData.getUrl().contains("http")) {
					fragment = WebviewFragment.newInstance(notificationData
							.getUrl());
				} else {
					fragment = WebviewFragment.newInstance("http://"
							+ notificationData.getUrl());
				}
			}
		}

		if (fragment != null) {
			fragment = SimiManager.getIntance().eventFragment(fragment);
			FragmentTransaction fragmentTransaction = SimiManager.getIntance()
					.getManager().beginTransaction();
			fragmentTransaction
					.replace(Rconfig.getInstance().id("container"), fragment)
					.addToBackStack(null).commitAllowingStateLoss();
		}
	}

	public void destroy() {
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
	}
}
