package com.simicart.core.base.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.simicart.MainActivity;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.network.request.SimiRequestQueue;
import com.simicart.core.base.networkcloud.request.multi.SimiRequestQueueCloud;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.event.block.EventBlock;
import com.simicart.core.event.fragment.CacheFragment;
import com.simicart.core.event.fragment.EventFragment;
import com.simicart.core.home.fragment.HomeFragment;
import com.simicart.core.menutop.controller.MenuTopController;
import com.simicart.core.slidemenu.controller.PhoneSlideMenuController;
import com.simicart.core.splashscreen.SplashActivity;
import com.simicart.core.style.FragmentDialogHandle;

import java.util.List;

public class SimiManager {

	private Activity mCurrentActivity;
	private Context mCurrentContext;
	private FragmentManager mManager;
	private static SimiManager instance;
	private PhoneSlideMenuController mSlideMenuController;
	private MenuTopController mMenuTopController;
	private FragmentManager mChildFragmentManager;

	protected SimiRequestQueue mRequestQueue;
	protected SimiRequestQueueCloud mRequestQueueCloud;
	protected Boolean isShowedNotify = false;
	protected boolean isRefreshCart = true;
	protected int mQtyCartPrevious;

	public SimiRequestQueueCloud getRequestQueueCloud() {
		return mRequestQueueCloud;
	}

	public SimiRequestQueue getRequestQueue() {
		return mRequestQueue;
	}

	private SimiManager() {
		mRequestQueue = new SimiRequestQueue();
		mRequestQueue.start();
		mRequestQueueCloud = new SimiRequestQueueCloud();
		mRequestQueueCloud.start();
	}

	public static SimiManager getIntance() {
		if (null == instance) {
			instance = new SimiManager();
		}

		return instance;
	}

	public Activity getCurrentActivity() {
		return mCurrentActivity;
	}

	public void hideKeyboard() {
		try {
			if (mCurrentActivity != null) {
				InputMethodManager imm = (InputMethodManager) mCurrentActivity
						.getSystemService(Service.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mCurrentActivity.getCurrentFocus()
						.getWindowToken(), 0);
			}
		} catch (Exception e) {
		}
	}

	public void setCurrentActivity(Activity mCurrentActivity) {
		this.mCurrentActivity = mCurrentActivity;
	}

	public Context getCurrentContext() {
		return mCurrentContext;
	}

	public void setCurrentContext(Context mCurrentContext) {
		this.mCurrentContext = mCurrentContext;
	}

	public FragmentManager getManager() {
		return mManager;
	}

	public void setManager(FragmentManager manager) {
		mManager = manager;
	}

	public void setSlideMenuController(PhoneSlideMenuController controller) {
		mSlideMenuController = controller;
	}

	public void setMenuTopController(MenuTopController controller) {
		mMenuTopController = controller;
	}

	public void onUpdateCartQty(String qty) {
		int i_qty = 0;
		try {
			qty = qty.trim();
			i_qty = Integer.parseInt(qty);

		} catch (Exception e) {
			Log.e("SimiManager ", "onUpdateCartQty " + e.getMessage());
		}

		Log.e("SimiManager ", "onUpdateCartQty " + "Previous "
				+ mQtyCartPrevious + "Current " + i_qty);

		if (mQtyCartPrevious != i_qty) {
			mQtyCartPrevious = i_qty;
			Log.e("SimiManager ", "onUpdateCartQty  TRUEEEEEEEEEEE");
			isRefreshCart = true;
		} else {
			isRefreshCart = false;
		}

		mMenuTopController.updateCartQty(qty);
	}

	public boolean isRereshCart() {
		return isRefreshCart;
	}

	public void setIsRefreshCart(boolean is_refresh_cart) {
		isRefreshCart = is_refresh_cart;
	}

	public void showCartLayout(boolean show) {
		if (mMenuTopController != null) {
			mMenuTopController.showCartLayout(show);
		}
	}

	public void onUpdateItemSignIn() {
		if (null != mSlideMenuController) {
			mSlideMenuController.updateSignIn();
		}
	}

	public void setChildFragment(FragmentManager childFragment) {
		mChildFragmentManager = childFragment;
	}

	public FragmentManager getChilFragmentManager() {
		return mChildFragmentManager;
	}

	public void toMainActivity() {
		Intent i = new Intent(mCurrentActivity, MainActivity.class);
		Bundle extras = mCurrentActivity.getIntent().getExtras();
		if (extras != null) {
			i.putExtras(extras);
		}
		mCurrentActivity.startActivity(i);
		mCurrentActivity.finish();
	}

	public void changeStoreView() {
		Intent intent = new Intent(mCurrentActivity.getApplicationContext(),
				SplashActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mCurrentActivity.finish();
		mCurrentActivity.startActivity(intent);
	}

	public Fragment getCurrentFragment() {
		List<Fragment> fragments = mManager.getFragments();
		for (Fragment fragment : fragments) {
			if (fragment != null && fragment.isVisible()) {
				return fragment;
			}
		}
		return null;
	}

	public SimiFragment eventFragment(SimiFragment fragment) {
		String nameFragment = fragment.getClass().getName();
		CacheFragment cache = new CacheFragment();
		cache.setFragment(fragment);
		EventFragment event = new EventFragment();
		event.dispatchEvent(nameFragment, cache);
		fragment = cache.getFragment();
		return fragment;
		// end event
	}

	public void addFragment(SimiFragment fragment) {
		if (null != mManager) {
			String nameFragment = fragment.getClass().getName();
			// event check barcode
			Constants.NAME_FRAGMENT = nameFragment;
			EventBlock block = new EventBlock();
			block.dispatchEvent("com.simicart.leftmenu.slidemenucontroller.onnavigate.checkdirectdetail");
			fragment = eventFragment(fragment);
			FragmentTransaction ft = mManager.beginTransaction();
			ft.setCustomAnimations(
					Rconfig.getInstance().getId("in_from_right", "anim"),
					Rconfig.getInstance().getId("out_to_left", "anim"), Rconfig
							.getInstance().getId("in_from_left", "anim"),
					Rconfig.getInstance().getId("out_to_right", "anim"));
			ft.replace(Rconfig.getInstance().id("container"), fragment);
			ft.addToBackStack(nameFragment);
			ft.commit();
			mManager.executePendingTransactions();
		}
	}

	public void replaceFragment(SimiFragment fragment) {
		if (null != mManager) {
			String nameFragment = fragment.getClass().getName();
			fragment = eventFragment(fragment);

			boolean isHome = false;
			String screen_name = fragment.getScreenName();
			if (null != screen_name && screen_name.equals("Home Screen")) {
				isHome = true;
			}

			mManager.popBackStack(nameFragment,
					FragmentManager.POP_BACK_STACK_INCLUSIVE);
			FragmentTransaction ft = mManager.beginTransaction();

			if (!isHome) {
				ft.setCustomAnimations(
						Rconfig.getInstance().getId("in_from_right", "anim"),
						Rconfig.getInstance().getId("out_to_left", "anim"),
						Rconfig.getInstance().getId("in_from_left", "anim"),
						Rconfig.getInstance().getId("out_to_right", "anim"));

			}
			ft.replace(Rconfig.getInstance().id("container"), fragment);
			ft.addToBackStack(nameFragment);
			ft.commit();
			if (mSlideMenuController != null) {
				mSlideMenuController.closeSlideMenuTablet();
			}
			// mManager.executePendingTransactions();
		}

	}

	public void replaceFragmentRightToLeft(SimiFragment fragment) {
		if (null != mManager) {
			String nameFragment = fragment.getClass().getName();
			Constants.NAME_FRAGMENT = nameFragment;

			fragment = eventFragment(fragment);

			boolean isHome = false;
			String screen_name = fragment.getScreenName();
			if (null != screen_name && screen_name.equals("Home Screen")) {
				isHome = true;
			}

			mManager.popBackStack(nameFragment,
					FragmentManager.POP_BACK_STACK_INCLUSIVE);
			FragmentTransaction ft = mManager.beginTransaction();

			if (!isHome) {

				// enter, exit, pop enter, pop exit
				ft.setCustomAnimations(
						Rconfig.getInstance().getId("in_from_left", "anim"),
						Rconfig.getInstance().getId("out_to_right", "anim"),
						Rconfig.getInstance().getId("in_from_right", "anim"),
						Rconfig.getInstance().getId("out_to_left", "anim"));
			}
			ft.replace(Rconfig.getInstance().id("container"), fragment);
			ft.addToBackStack(nameFragment);
			ft.commit();
			// mManager.executePendingTransactions();
		}
	}

	public void addFragmentSub(final SimiFragment fragment) {

		new Handler().post(new Runnable() {
			public void run() {
				SimiFragment fragment1 = eventFragment(fragment);
				if (DataLocal.isTablet) {
					FragmentTransaction ft2;
					if (null != mChildFragmentManager) {
						ft2 = mChildFragmentManager.beginTransaction();
					} else {
						ft2 = mManager.beginTransaction();
					}
					ft2.setCustomAnimations(
							Rconfig.getInstance()
									.getId("in_from_right", "anim"),
							Rconfig.getInstance().getId("out_to_left", "anim"),
							Rconfig.getInstance().getId("in_from_left", "anim"),
							Rconfig.getInstance().getId("out_to_right", "anim"));
					int idContainer2 = Rconfig.getInstance().id("container2");
					if (idContainer2 != 0) {
						ft2.replace(idContainer2, fragment1);
						ft2.commit();
						mChildFragmentManager.executePendingTransactions();
					}
				} else {
					FragmentTransaction ft = mManager.beginTransaction();
					ft.setCustomAnimations(
							Rconfig.getInstance()
									.getId("in_from_right", "anim"),
							Rconfig.getInstance().getId("out_to_left", "anim"),
							Rconfig.getInstance().getId("in_from_left", "anim"),
							Rconfig.getInstance().getId("out_to_right", "anim"));
					ft.replace(Rconfig.getInstance().id("container"), fragment1);
					ft.addToBackStack(null).commit();
				}

			}
		});
	}

	public void removeFragmentSub(SimiFragment fragment) {
		FragmentTransaction ft2;
		if (null != mChildFragmentManager) {
			ft2 = mChildFragmentManager.beginTransaction();
		} else {
			ft2 = mManager.beginTransaction();
		}

		ft2.remove(fragment).commit();
	}

	public void addFragmentSubTab(final SimiFragment fragment) {
		new Handler().post(new Runnable() {
			public void run() {
				SimiFragment fragment1 = eventFragment(fragment);
				if (DataLocal.isTablet) {
					FragmentTransaction ft2;
					if (null != mChildFragmentManager) {
						ft2 = mChildFragmentManager.beginTransaction();
					} else {
						ft2 = mManager.beginTransaction();
					}
					ft2.setCustomAnimations(
							Rconfig.getInstance()
									.getId("in_from_right", "anim"),
							Rconfig.getInstance().getId("out_to_left", "anim"),
							Rconfig.getInstance().getId("in_from_left", "anim"),
							Rconfig.getInstance().getId("out_to_right", "anim"));

					int idContainer2 = Rconfig.getInstance().id("container2");
					if (idContainer2 != 0) {
						ft2.replace(idContainer2, fragment1);
						ft2.addToBackStack(fragment.getClass().getName());
						ft2.commit();
						mChildFragmentManager.executePendingTransactions();
					}
				}
			}
		});

	}

	public void replacePopupFragment(SimiFragment fragment) {
		fragment = eventFragment(fragment);
		if (null != mManager) {
			if (DataLocal.isTablet) {
				fragment = eventFragment(fragment);
				Fragment prev = mManager.findFragmentByTag("dialog");
				if (prev != null) {
					DialogFragment df = (DialogFragment) prev;
					ImageView back = (ImageView) df.getView().findViewById(
							Rconfig.getInstance().id("bt_back"));
					back.setVisibility(View.VISIBLE);
					FragmentTransaction ft = df.getChildFragmentManager()
							.beginTransaction();
					ft.replace(Rconfig.getInstance().id("popup_container"),
							fragment);
					String nameFragment = fragment.getClass().getName();
					ft.addToBackStack(nameFragment).commit();
				} else {
					removeDialog();
					DialogFragment newFragment = FragmentDialogHandle
							.newInstance(fragment);
					FragmentTransaction ft = mManager.beginTransaction();
					newFragment.show(ft, "dialog");
				}
			} else {
				replaceFragment(fragment);
			}
		}
	}

	public void addPopupFragment(SimiFragment fragment) {
		fragment = eventFragment(fragment);
		if (null != mManager) {
			if (DataLocal.isTablet) {
				Fragment prev = mManager.findFragmentByTag("dialog");
				if (prev != null) {
					DialogFragment df = (DialogFragment) prev;
					ImageView back = (ImageView) df.getView().findViewById(
							Rconfig.getInstance().id("bt_back"));
					back.setVisibility(View.VISIBLE);

					FragmentTransaction ft = df.getChildFragmentManager()
							.beginTransaction();
					ft.add(Rconfig.getInstance().id("popup_container"),
							fragment);
					String nameFragment = fragment.getClass().getName();
					ft.addToBackStack(nameFragment).commit();
				} else {
					removeDialog();
					DialogFragment newFragment = FragmentDialogHandle
							.newInstance(fragment);
					FragmentTransaction ft = mManager.beginTransaction();
					newFragment.show(ft, "dialog");
				}
			} else {
				addFragment(fragment);
			}
		}
	}

	public void removeDialog() {
		Fragment prev = mManager.findFragmentByTag("dialog");
		if (prev != null) {
			DialogFragment df = (DialogFragment) prev;
			Log.e("SimiManager ", "removeDialog" + df.getClass().getName());
			df.dismiss();
		}
	}

	public void backPreviousFragment() {
		if (DataLocal.isTablet) {
			popFragmentDialog();
		} else {
			mManager.popBackStack();
		}
	}

	public void backToHomeFragment() {
		mManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
		if (DataLocal.isTablet) {
			removeDialog();
		}
		HomeFragment fragment = HomeFragment.newInstance();
		replaceFragment(fragment);
	}

	public void clearAllFragment() {
		mManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
		if (DataLocal.isTablet) {
			removeDialog();
		}
	}

	public void popFragmentDialog() {
		Fragment prev = mManager.findFragmentByTag("dialog");
		if (prev != null) {
			DialogFragment df = (DialogFragment) prev;
			df.getChildFragmentManager().popBackStack();
			if (df.getChildFragmentManager().getBackStackEntryCount() == 1) {
				ImageView back = (ImageView) df.getView().findViewById(
						Rconfig.getInstance().id("bt_back"));
				back.setVisibility(View.GONE);
			}
		}
	}

	public void clearAllChidFragment() {
		Fragment prev = mManager.findFragmentByTag("dialog");
		if (prev != null) {
			DialogFragment df = (DialogFragment) prev;
			df.getChildFragmentManager().popBackStack(null,
					FragmentManager.POP_BACK_STACK_INCLUSIVE);
		}
	}

	public void showError(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mCurrentActivity);
		builder.setTitle(Config.getInstance().getText("Warning..."));
		builder.setMessage(message)
				.setCancelable(true)
				.setPositiveButton(Config.getInstance().getText("OK"),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	public void showNotify(String message) {

		synchronized (isShowedNotify) {
			if (!isShowedNotify) {
				isShowedNotify = true;
				AlertDialog.Builder builder = new AlertDialog.Builder(
						mCurrentActivity);
				builder.setTitle(Config.getInstance().getText("Warning..."));
				builder.setMessage(message)
						.setCancelable(true)
						.setPositiveButton(Config.getInstance().getText("OK"),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.dismiss();
										isShowedNotify = false;
									}
								});
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		}

	}

	public void showNotify(String title, String message, String sPositiveButton) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mCurrentActivity);
		builder.setTitle(Config.getInstance().getText(title));
		builder.setMessage(Config.getInstance().getText(message))
				.setCancelable(true)
				.setPositiveButton(
						Config.getInstance().getText(sPositiveButton),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						});
		AlertDialog dialog = builder.create();
		dialog.show();

	}

	public void showToast(String mes) {
		Toast toast = Toast.makeText(mCurrentContext, Config.getInstance()
				.getText(mes), Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
}
