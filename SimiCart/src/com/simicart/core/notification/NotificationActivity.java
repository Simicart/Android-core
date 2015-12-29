package com.simicart.core.notification;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.simicart.core.notification.controller.NotificationController;
import com.simicart.core.notification.entity.NotificationEntity;

public class NotificationActivity extends FragmentActivity {

	NotificationEntity notificationData;

	protected void onCreate(Bundle savedInstanceState) {
		// ------------------notification--------------
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			NotificationEntity notificationData = (NotificationEntity) extras
					.getSerializable("NOTIFICATION_DATA");
			NotificationController notification = new NotificationController(
					this);
			if (notificationData != null) {
				notification.showNotification(this, notificationData);
			} else {
				finish();
			}
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {

	}
}