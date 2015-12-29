package com.simicart;

import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.magestore.simicart.R;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.checkout.controller.ConfigCheckout;
import com.simicart.core.common.FontsOverride;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.controller.AutoSignInController;
import com.simicart.core.event.activity.CacheActivity;
import com.simicart.core.event.activity.EventActivity;
import com.simicart.core.event.base.UtilsEvent;
import com.simicart.core.event.block.EventBlock;
import com.simicart.core.event.controller.EventController;
import com.simicart.core.menutop.fragment.FragmentMenuTop;
import com.simicart.core.notification.NotificationActivity;
import com.simicart.core.notification.common.CommonUtilities;
import com.simicart.core.notification.controller.NotificationController;
import com.simicart.core.reportbug.UnCaughtException;
import com.simicart.core.shortcutbadger.ShortcutBadgeException;
import com.simicart.core.shortcutbadger.ShortcutBadger;
import com.simicart.core.slidemenu.fragment.SlideMenuFragment;

@SuppressLint("DefaultLocale")
public class MainActivity extends FragmentActivity {

	public final static int PAUSE = 2;
	public final static int RESUME = 1;
	public final static int START = 3;
	public final static int CREATE = 4;
	public final static int DESTROY = 5;
	public final static int BACK = 6;

	private SlideMenuFragment mNavigationDrawerFragment;
	public static Activity context;
	private NotificationController notification;
	public static int state = 0;

	public static boolean mCheckToDetailAfterScan = false;
	public static int mBackEntryCountDetail = 0;
	public static boolean checkBackScan = false;
	public static MainActivity instance;
	private int requestCode;
	private int resultCode;
	private Intent data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// report bug
		Thread.setDefaultUncaughtExceptionHandler(new UnCaughtException(this));
		SimiManager.getIntance().setCurrentActivity(this);
		SimiManager.getIntance().setCurrentContext(getApplicationContext());
		getActionBar().hide();
		setContentView(R.layout.core_main_activity);
		if (DataLocal.isSignInComplete()) {
			autoSignin();
		}
		instance = this;
		context = this;
		// checkTheme();
		SimiManager.getIntance().setManager(getSupportFragmentManager());
		// dispatch event for sent google analytic
		CacheActivity cacheActivity = new CacheActivity();
		cacheActivity.setActivity(this);
		EventActivity dispacth = new EventActivity();
		dispacth.dispatchEvent("com.simicart.mainActivity.onCreate",
				cacheActivity);
		// end dispatch
		if (DataLocal.isTablet) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		mNavigationDrawerFragment = (SlideMenuFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);

		mNavigationDrawerFragment.setup(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		changeFont();

		String qtyCart = DataLocal.qtyCartAuto;
		if (Utils.validateString(qtyCart)) {
			SimiManager.getIntance().onUpdateCartQty(qtyCart);
		}

		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				CommonUtilities.DISPLAY_MESSAGE_ACTION));
		notification = new NotificationController(this);
		notification.registerNotification();

		recieveNotification();

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		FragmentMenuTop fragment = FragmentMenuTop
				.newInstance(mNavigationDrawerFragment);
		ft.replace(Rconfig.getInstance().id("menu_top"), fragment);
		ft.commit();
	}

	private void autoSignin() {
		AutoSignInController controller = new AutoSignInController();
		controller.onStart();
	}

	private void recieveNotification() {
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			Intent i = new Intent(context, NotificationActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i.putExtras(extras);
			context.startActivity(i);
		}
	}

	@Override
	protected void onPause() {
		state = PAUSE;
		super.onPause();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onResume() {
		if ((null == Config.getInstance().getCurrencyCode())
				|| (Config.getInstance().getCurrencyCode().equals("null"))) {
			UtilsEvent.itemsList.clear();
			DataLocal.mSharedPre = getApplicationContext()
					.getSharedPreferences(DataLocal.NAME_REFERENCE,
							Context.MODE_PRIVATE);
			SimiManager.getIntance().changeStoreView();
		}
		state = RESUME;
		SimiManager.getIntance().setCurrentActivity(this);
		SimiManager.getIntance().setCurrentContext(getApplicationContext());
		SimiManager.getIntance().setManager(getSupportFragmentManager());
		// Update badge
		try {
			ShortcutBadger.setBadge(context, 0);
		} catch (ShortcutBadgeException e) {
		}
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// if (!mNavigationDrawerFragment.isDrawerOpen()) {
		// getMenuInflater()
		// .inflate(
		// Rconfig.getInstance().getId("main_activity2",
		// "menu"), menu);
		// restoreActionBar();
		// return true;
		// // }

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			// Toast.makeText(this, "Add", Toast.LENGTH_SHORT).show();
			// notification.openNotificastionSetting(this);
			return true;
			// case R.id.notification:
			// notification.openNotificationSetting(this);
			// return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void changeFont() {
		FontsOverride.setDefaultFont(this, "DEFAULT", Config.getInstance()
				.getFontCustom());
		FontsOverride.setDefaultFont(this, "NORMAL", Config.getInstance()
				.getFontCustom());
		FontsOverride.setDefaultFont(this, "MONOSPACE", Config.getInstance()
				.getFontCustom());
		FontsOverride.setDefaultFont(this, "SERIF", Config.getInstance()
				.getFontCustom());
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		// Checks the orientation of the screen
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			// Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
		}
	}

	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(
					CommonUtilities.EXTRA_MESSAGE);
			Log.e(getClass().getName() + newMessage, "EXTRA_MESSAGE");
		}
	};

	@Override
	public void onBackPressed() {
		Log.e("Onback", "MainActivity");

		EventBlock eventBlock = new EventBlock();
		eventBlock
				.dispatchEvent("com.simicart.leftmenu.mainactivity.onbackpress.checkentrycount");
		int count = SimiManager.getIntance().getManager()
				.getBackStackEntryCount();
		if (count > 0) {
			try {
				if (count == 1) {
					if (checkBackScan == true) {
						checkBackScan = false;
						eventBlock
								.dispatchEvent("com.simicart.leftmenu.mainactivity.onbackpress.backtoscan");
					} else {
						// out app
						AlertDialog.Builder alertboxDowload = new AlertDialog.Builder(
								this);
						alertboxDowload.setTitle(Config.getInstance()
								.getText("CLOSE APPLICATION").toUpperCase());
						alertboxDowload.setMessage(Config.getInstance()
								.getText("Are you sure you want to exit?"));
						alertboxDowload.setCancelable(false);
						alertboxDowload.setPositiveButton(Config.getInstance()
								.getText("OK").toUpperCase(),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										context = null;
										SimiManager.getIntance().getManager()
												.popBackStack();
										android.os.Process
												.killProcess(android.os.Process
														.myPid());
										finish();
									}
								});
						alertboxDowload.setNegativeButton(Config.getInstance()
								.getText("CANCEL").toUpperCase(),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
									}
								});
						alertboxDowload.show();
					}

				} else {
					try {
						if (count > 2) {
							List<Fragment> list = SimiManager.getIntance()
									.getManager().getFragments();
							Fragment fragment = SimiManager.getIntance()
									.getManager().getFragments()
									.get(list.size() - 1);
							if (fragment != null) {
								int tag = fragment.getTargetRequestCode();
								if (tag == ConfigCheckout.TARGET_REVIEWORDER) {
									SimiManager.getIntance()
											.backToHomeFragment();
								} else {
									SimiManager.getIntance().getManager()
											.popBackStack();
								}
							} else {
								SimiManager.getIntance().getManager()
										.popBackStack();
							}
						} else {
							SimiManager.getIntance().getManager()
									.popBackStack();
						}
						if (checkBackScan == true) {
							checkBackScan = false;
							eventBlock
									.dispatchEvent("com.simicart.leftmenu.mainactivity.onbackpress.backtoscan");
						}
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} else {
			super.onBackPressed();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

		// Get the intent that started this Activity.
		Intent intent = this.getIntent();
		Uri uri = intent.getData();

		// Send a screenview using any available campaign or referrer data.
		MapBuilder.createAppView().setAll(getReferrerMapFromUri(uri));
	}

	// This examples assumes the use of Google Analytics campaign
	// "utm" parameters, like "utm_source"
	private static final String CAMPAIGN_SOURCE_PARAM = "utm_source";

	/*
	 * Given a URI, returns a map of campaign data that can be sent with any GA
	 * hit.
	 * 
	 * @param uri A hierarchical URI that may or may not have campaign data
	 * stored in query parameters.
	 * 
	 * @return A map that may contain campaign or referrer that may be sent with
	 * any Google Analytics hit.
	 */
	Map<String, String> getReferrerMapFromUri(Uri uri) {

		MapBuilder paramMap = new MapBuilder();

		// If no URI, return an empty Map.
		if (uri == null) {
			return paramMap.build();
		}

		// Source is the only required campaign field. No need to continue if
		// not
		// present.
		if (uri.getQueryParameter(CAMPAIGN_SOURCE_PARAM) != null) {

			// MapBuilder.setCampaignParamsFromUrl parses Google Analytics
			// campaign
			// ("UTM") parameters from a string URL into a Map that can be set
			// on
			// the Tracker.
			paramMap.setCampaignParamsFromUrl(uri.toString());

			// If no source parameter, set authority to source and medium to
			// "referral".
		} else if (uri.getAuthority() != null) {

			paramMap.set(Fields.CAMPAIGN_MEDIUM, "referral");
			paramMap.set(Fields.CAMPAIGN_SOURCE, uri.getAuthority());
		}

		return paramMap.build();
	}

	@Override
	protected void onDestroy() {
		notification.destroy();
		if (mHandleMessageReceiver != null) {
			unregisterReceiver(mHandleMessageReceiver);
		}

		System.gc();
		Runtime.getRuntime().freeMemory();

		super.onDestroy();
	}

	public int getRequestCode() {
		return requestCode;
	}

	public int getResultCode() {
		return resultCode;
	}

	public Intent getData() {
		return data;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		this.requestCode = requestCode;
		this.resultCode = resultCode;
		this.data = data;
		// event form signin
		EventController dispatch = new EventController();
		dispatch.dispatchEvent("com.simicart.MainActivity.onActivityResult",
				this);
		EventBlock block = new EventBlock();
		if (requestCode == 64209) {
			block = new EventBlock();
			block.dispatchEvent("com.simicart.core.catalog.product.block.ProductBlock.resultfacebook.checkresultcode");
		}
		if (requestCode == Constants.RESULT_BARCODE) {
			block = new EventBlock();
			block.dispatchEvent("com.simicart.leftmenu.mainactivity.onactivityresult.resultbarcode");
		}
	}
}
