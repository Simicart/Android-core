/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.simicart.core.notification;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.magestore.simicart.R;
import com.simicart.MainActivity;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.notification.common.CommonUtilities;
import com.simicart.core.notification.common.ServerUtilities;
import com.simicart.core.notification.entity.NotificationEntity;
import com.simicart.core.notification.gcm.GCMBaseIntentService;
import com.simicart.core.notification.gcm.GCMRegistrar;
import com.simicart.core.shortcutbadger.ShortcutBadgeException;
import com.simicart.core.shortcutbadger.ShortcutBadger;
import com.simicart.core.splashscreen.SplashActivity;

/**
 * IntentService responsible for handling GCM messages.
 */
@SuppressLint("NewApi")
public class GCMIntentService extends GCMBaseIntentService {

	public static NotificationEntity notificationData;

	public GCMIntentService() {
		super(Config.getInstance().getSenderId());
	}

	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.e(TAG, "Device registered: regId = " + registrationId);
		CommonUtilities.displayMessage(context,
				"From GCM: device successfully registered!");
		ServerUtilities.register(context, registrationId);
	}

	@Override
	protected void onUnregistered(Context context, String registrationId) {
		CommonUtilities.displayMessage(context,
				"From GCM: device successfully unregistered!");
		if (GCMRegistrar.isRegisteredOnServer(context)) {
			Log.e(TAG, "Device unregistered");
			ServerUtilities.unregister(context, registrationId);
		} else {
			// This callback results from the call to unregister made on
			// ServerUtilities when the registration to the server failed.
			Log.e(TAG, "Ignoring unregister callback");
		}
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		String content = intent.getExtras().getString("message");
		String action = intent.getAction();
		NotificationEntity notificationTemp = null;

		if (action.equals("com.google.android.c2dm.intent.RECEIVE")) {
			try {
				JSONObject json = null;
				if (content != null && !content.equals("")) {
					json = new JSONObject(content);
				}
				if (json != null) {
					Log.e("MESS NHAN DUOC LA ", "A" + json.toString(4));
					notificationTemp = new NotificationEntity();
					if (json.has(Constants.MESSAGE)) {
						notificationTemp.setMessage(json
								.getString(Constants.MESSAGE));
					}
					if (json.has(Constants.TITLE)) {
						notificationTemp.setTitle(json
								.getString(Constants.TITLE));
					}
					if (json.has(Constants.URL)) {
						notificationTemp.setUrl(json.getString(Constants.URL));
					}
					if (json.has("type")) {
						notificationTemp.setType(json.getString("type"));
					}
					if (json.has("categoryID")) {
						notificationTemp.setCategoryID(json
								.getString("categoryID"));
					}
					if (json.has("categoryName")) {
						notificationTemp.setCategoryName(json
								.getString("categoryName"));
					}
					if (json.has("has_child")) {
						notificationTemp.setHasChild(json
								.getString("has_child"));
					}
					if (json.has("imageUrl")) {
						notificationTemp.setImage(json.getString("imageUrl"));
					}
					if (json.has("productID")) {
						notificationTemp.setProductID(json
								.getString("productID"));
					}
					if (json.has("show_popup")) {
						notificationTemp.setShowPopup(json
								.getString("show_popup"));
					}
				} else {
					return;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			// Log.e("MESS NHAN DUOC LA ", "11111111111");
			if (notificationTemp != null) {
				// Log.e("MESS NHAN DUOC LA ", "22222222");
				if (notificationData == null) {
					// Log.e("MESS NHAN DUOC LA ", "333333333");
					notificationData = notificationTemp;
					if (DataLocal.enableNotification(context)) {
						// Log.e("MESS NHAN DUOC LA ", "44444444");
						onRecieveMessage(context);
					}
				} else {
					// Log.e("MESS NHAN DUOC LA ", "55555555555");
					if (!notificationTemp.equals(notificationData)) {
						// Log.e("MESS NHAN DUOC LA ", "6666666666");
						notificationData = notificationTemp;
						if (DataLocal.enableNotification(context)) {
							// Log.e("MESS NHAN DUOC LA ", "777777777777");
							onRecieveMessage(context);
						}
					}
				}
			}
		}
		return;
	}

	private void onRecieveMessage(Context context) {
		// Sang man hinh khi co notification
		PowerManager pm = (PowerManager) getApplicationContext()
				.getSystemService(Context.POWER_SERVICE);
		WakeLock wakeLock = pm
				.newWakeLock(
						(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
								| PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP),
						"TAG");
		wakeLock.acquire(5000);

		// // Am thanh mac dinh
		// try {
		// Uri notification = RingtoneManager
		// .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		// Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),
		// notification);
		// r.play();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		// check app open or close
		if (MainActivity.context != null
				&& MainActivity.state != MainActivity.PAUSE
				&& notificationData.getShowPopup().equals("1")) {
			createNotification(context);
		} else {
			generateNotification(context, notificationData);
		}
	}

	private void createNotification(Context context) {
		Intent i = new Intent(context, NotificationActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.putExtra("NOTIFICATION_DATA", notificationData);
		context.startActivity(i);
	}

	@Override
	protected void onDeletedMessages(Context context, int total) {
		Log.e(TAG, "Received deleted messages notification");
		String message = ("From GCM: server deleted %1$d pending messages!" + total);
		CommonUtilities.displayMessage(context, message);
		// notifies user
		NotificationEntity notificationEntity = new NotificationEntity();
		notificationEntity.setMessage(message);
		notificationEntity.setTitle("DELETE MES");
		notificationEntity.setUrl("");
		generateNotification(context, notificationEntity);
	}

	@Override
	public void onError(Context context, String errorId) {
		Log.e(TAG, "Received error: " + errorId);
		CommonUtilities.displayMessage(context, ("From GCM: error" + errorId));
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		// log message
		Log.e(TAG, "Received recoverable error: " + errorId);
		CommonUtilities.displayMessage(context,
				("From GCM: recoverable error (%1$s)." + errorId));
		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */
	// private static int numMessages = 0;
	public static int notifyID = 0;

	private void generateNotification(Context context,
			NotificationEntity notificationData) {
		// Invoking the default notification service
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// Sets an ID for the notification, so it can be updated

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context).setContentTitle(notificationData.getTitle())
				.setContentText(notificationData.getMessage())
				.setSmallIcon(R.drawable.default_icon);

		mBuilder.setDefaults(Notification.DEFAULT_ALL);

		// mBuilder.setNumber(++numMessages);
		Intent resultIntent = new Intent(context, SplashActivity.class);
		if (notificationData.getShowPopup().equals("1")) {
			resultIntent.putExtra("NOTIFICATION_DATA", notificationData);
		}
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(SplashActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack

		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
				notifyID, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);

		mBuilder.setAutoCancel(true);// cancel after click notification
		// mId allows you to update the notification later on.
		mNotificationManager.notify(++notifyID, mBuilder.build());

		// Update badge
		try {
			ShortcutBadger.setBadge(context, notifyID);
		} catch (ShortcutBadgeException e) {
		}
	}
}
