package com.simicart.core.slidemenu.fragment;

import java.lang.reflect.Field;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.catalog.category.fragment.CategoryFragment;
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;

public class CateSlideMenuFragment extends SimiFragment {

	protected static CateSlideMenuFragment instance;

	private FragmentManager manager;
	protected SlideMenuFragment mNavigationDrawerFragment;

	public static CateSlideMenuFragment getIntance() {
		if (null == instance) {
			instance = new CateSlideMenuFragment();
		}
		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				Rconfig.getInstance().layout("core_cate_slidemenu"), container,
				false);
		view.setBackgroundColor(Config.getInstance().getMenu_background());
		CategoryFragment fragment = CategoryFragment.newInstance(
				"all categories", "-1");
		replaceFragmentCategoryMenu(fragment);
		return view;
	}

	public void setSlideMenu(SlideMenuFragment mNavigationDrawerFragment) {
		this.mNavigationDrawerFragment = mNavigationDrawerFragment;

	}

	public void openMenu() {
		mNavigationDrawerFragment.openCategoryMenu();
	}

	public void replaceFragmentCategoryMenu(SimiFragment fragment) {
		try {
			if (instance == null) {
				instance = new CateSlideMenuFragment();
			}
			manager = instance.getChildFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			int container = Rconfig.getInstance().id("contain_cate_slidemenu");
			transaction.replace(container, fragment).addToBackStack(null);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class
					.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public void backFragmentCategoryMenu() {
		if (enableBackCatFragment()) {
			manager.popBackStack();
		}
	}

	public boolean enableBackCatFragment() {
		if (manager.getBackStackEntryCount() > 1) {
			return true;
		}
		return false;
	}
}
