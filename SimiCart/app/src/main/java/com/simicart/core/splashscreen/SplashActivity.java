package com.simicart.core.splashscreen;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.common.Utils;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.event.activity.CacheActivity;
import com.simicart.core.event.activity.EventActivity;
import com.simicart.core.event.base.EventListener;
import com.simicart.core.event.base.UtilsEvent;
import com.simicart.core.shortcutbadger.ShortcutBadgeException;
import com.simicart.core.shortcutbadger.ShortcutBadger;
import com.simicart.core.splashscreen.block.SplashBlock;
import com.simicart.core.splashscreen.controller.SplashController;
import com.simicart.core.splashscreen.delegate.SplashDelegate;
import com.simicart.core.splashscreen.model.DeepLinkModel;

public class SplashActivity extends Activity implements SplashDelegate {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Thread.setDefaultUncaughtExceptionHandler(new
		// UnCaughtException(this));
		Context context = getApplicationContext();
		SimiManager.getIntance().setCurrentContext(context);
		SimiManager.getIntance().setCurrentActivity(this);
		setContentView(Rconfig.getInstance().getId(context,
				"core_splash_screen", "layout"));
		// GCMIntentService.notificationData = null;
		DataLocal.isTablet = Utils.isTablet(context);
		if (DataLocal.isTablet) {
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else {
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}

		View view = findViewById(Rconfig.getInstance().getId(
				getApplicationContext(), "core_splash_screen", "id"));

		EventListener.listEvent.clear();
		UtilsEvent.itemsList.clear();
		EventListener.setEvent("simi_developer");
		// dispatch event for sent google analytic
		CacheActivity cacheActivity = new CacheActivity();
		cacheActivity.setActivity(this);
		EventActivity dispacth = new EventActivity();
		dispacth.dispatchEvent("com.simicart.splashscreen.onCreate",
				cacheActivity);
		// end dispatch

		SplashBlock block = new SplashBlock(view, context);
		block.initView();
		SplashController model = new SplashController(this, context);
		model.create();

		// Update badge
		try {
			ShortcutBadger.setBadge(context, 0);
		} catch (ShortcutBadgeException e) {

		}

		Intent intent = getIntent();
		Uri link = intent.getData();
		if (link == null)
			Log.e("Deep  link", "null");
		else {
			String deepLink = link.toString();
			Log.e("Deep  link", "--" + deepLink);
			if (deepLink != null && deepLink.contains(".html")) {
				deepLink = deepLink.substring(deepLink.lastIndexOf("/") + 1,
						deepLink.lastIndexOf(".html"));
				Log.e("Deep  link", "++" + deepLink);
				DataLocal.deepLinkItemName = deepLink;
				if (!DataLocal.deepLinkItemName.equals("")) {
					getDeepLinkItem();
				}
			}
		}
		// DataLocal.deepLinkItemName = "shoes";
		// getDeepLinkItem();
	}

	private void getDeepLinkItem() {
		final DeepLinkModel model = new DeepLinkModel();
		model.setDelegate(new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				// TODO Auto-generated method stub
				if (isSuccess) {
					Log.e("Deep link", "Success");
					ArrayList<SimiEntity> entity = model.getCollection()
							.getCollection();
					if (entity.size() > 0) {
						for (SimiEntity simiEntity : entity) {
							JSONObject obj = simiEntity.getJSONObject();
							if (obj.has("id")) {
								try {
									DataLocal.deepLinkItemID = obj
											.getString("id");
									Log.e("Deep Link", DataLocal.deepLinkItemID);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							if (obj.has("type")) {
								try {
									DataLocal.deepLinkItemType = obj
											.getInt("type");
									Log.e("Deep Link", "++"
											+ DataLocal.deepLinkItemType);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							if (obj.has("has_child")) {
								try {
									DataLocal.deepLinkItemHasChild = obj
											.getString("has_child");
									Log.e("Deep Link", "++"
											+ DataLocal.deepLinkItemHasChild);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		});
		model.addParam("link", DataLocal.deepLinkItemName);
		model.request();
	}

	@Override
	public void creatMain() {
		SimiManager.getIntance().toMainActivity();
	}

	@Override
	public void onBackPressed() {
	}

}
