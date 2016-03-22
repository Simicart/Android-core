package com.simicart.core.slidemenu.fragment;

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.slidemenu.adapter.TabSlideMenuAdapter;
import com.simicart.core.slidemenu.delegate.CloseSlideMenuDelegate;
import com.simicart.core.style.PagerSlidingTabStrip;

public class SlideMenuFragment extends SimiFragment implements
		CloseSlideMenuDelegate {

	protected View rootView;
	protected ActionBarDrawerToggle mDrawerToggle;
	protected DrawerLayout mDrawerLayout;
	protected View mFragmentContainerView;
	protected boolean isPhoneMenu = true;

	public static SlideMenuFragment newInstance() {
		SlideMenuFragment fragment = new SlideMenuFragment();
		return fragment;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(
				Rconfig.getInstance().getId(getActivity(),
						"core_slidemenu_layout", "layout"), container, false);
		// rootView.setBackgroundColor(Color.parseColor("#05292929"));
		return rootView;
	}

	public void setup(int fragmentId, DrawerLayout drawerLayout) {
		// ActionBar actionBar = getActionBar();
		// actionBar.setDisplayHomeAsUpEnabled(true);
		// actionBar.setHomeButtonEnabled(true);
		int mIdIconDrawer = Rconfig.getInstance().drawable("ic_menu");

		mFragmentContainerView = getActivity().findViewById(fragmentId);
		mDrawerLayout = (DrawerLayout) getActivity().findViewById(
				Rconfig.getInstance().id("drawer_layout"));
		mDrawerLayout.setDrawerShadow(
				Rconfig.getInstance().drawable("drawer_shadow"),
				GravityCompat.START);
		mDrawerLayout.setBackgroundColor(Config.getInstance()
				.getApp_backrground());

		mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout,
				mIdIconDrawer, Rconfig.getInstance().string(
						"navigation_drawer_open"), Rconfig.getInstance()
						.string("navigation_drawer_close")) {
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				// if (!isAdded()) {
				// return;
				// }
				getActivity().supportInvalidateOptionsMenu();
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				// if (!isAdded()) {
				// return;
				// }
				getActivity().supportInvalidateOptionsMenu();
			}
		};

		// mDrawerLayout.openDrawer(mFragmentContainerView);
		mDrawerLayout.post(new Runnable() {
			@Override
			public void run() {
				mDrawerToggle.syncState();
			}
		});
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		initView();
	}

	public void initView() {
		if (DataLocal.isTablet) {
			Log.e("SlideMenuFragment ", "initView Tablet");
			initSlideTablet();
		} else {
			PhoneSlideMenuFragment fragment = PhoneSlideMenuFragment.newInstance(this);
//			fragment.setCloseDelegate(this);
			replaceFragment(fragment);
		}
	}

	ViewPager mPager;

	public void initSlideTablet() {
		ArrayList<SimiFragment> simiFragments = new ArrayList<>();
		PhoneSlideMenuFragment phoneSlideMenuFragment =  PhoneSlideMenuFragment.newInstance(this);
//		phoneSlideMenuFragment.setCloseDelegate(this);
		simiFragments.add(phoneSlideMenuFragment);
		CateSlideMenuFragment categoryFragment = CateSlideMenuFragment
				.getIntance();
		categoryFragment.setSlideMenu(this);
		simiFragments.add(categoryFragment);

		TabSlideMenuAdapter adapter = new TabSlideMenuAdapter(
				getChildFragmentManager(), simiFragments);
		mPager = (ViewPager) rootView.findViewById(Rconfig.getInstance().id(
				"pager"));
		mPager.setAdapter(adapter);

		PagerSlidingTabStrip title_tab = (PagerSlidingTabStrip) rootView
				.findViewById(Rconfig.getInstance().id("pager_title_strip"));
		title_tab.setTextColor(Config.getInstance().getMenu_text_color());
		// title_tab.setTextSize(20);
		title_tab.setBackgroundColor(Config.getInstance().getMenu_background());
		title_tab.setDividerColor(Config.getInstance().getMenu_background());
		title_tab.setIndicatorColor(Config.getInstance().getMenu_text_color());
		title_tab.setIndicatorHeight(3);
		title_tab.setAllCaps(false);
		title_tab.setShouldExpand(true);
		title_tab.setViewPager(mPager);
		// title_tab.set
	}

	public void replaceFragment(SimiFragment fragment) {
		FragmentManager manager = getChildFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		int container = Rconfig.getInstance().id("contain_slidemenu");
		transaction.replace(container, fragment);
		transaction.commit();
	}

	public boolean isDrawerOpen() {
		return mDrawerLayout != null
				&& mDrawerLayout.isDrawerOpen(mFragmentContainerView);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// if (isDrawerOpen()) {
		// inflater.inflate(Rconfig.getInstance().getId("global", "menu"),
		// menu);
		// // showGlobalContextActionBar();
		// }
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// protected void showGlobalContextActionBar() {
	// ActionBar actionBar = getActionBar();
	// actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
	// }

	// protected ActionBar getActionBar() {
	// return ((ActionBarActivity) getActivity()).getSupportActionBar();
	// }

	public void openMenu() {
		mDrawerLayout.openDrawer(mFragmentContainerView);
	}

	public void openCategoryMenu() {
		mPager.setCurrentItem(1);
		mDrawerLayout.openDrawer(mFragmentContainerView);
	}

	@Override
	public void closeSlideMenu() {
		mDrawerLayout.closeDrawer(mFragmentContainerView);
	}
}
