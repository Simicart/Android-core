package com.simicart.theme.matrixtheme.home.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.nineoldandroids.view.ViewHelper;
import com.simicart.core.banner.entity.BannerEntity;
import com.simicart.core.config.Rconfig;
import com.simicart.theme.matrixtheme.home.common.Common;
import com.simicart.theme.matrixtheme.home.common.FragmentPagerBanner;
import com.simicart.theme.matrixtheme.home.common.MyLinearLayout;

public class Theme1PagerAdapter extends FragmentPagerAdapter implements
		ViewPager.OnPageChangeListener {
	private boolean swipedLeft = false;
	private int lastPage = 0;
	private MyLinearLayout cur = null;
	private MyLinearLayout next = null;
	private MyLinearLayout prev = null;
	private MyLinearLayout nextnext = null;
	private Context context;
	private FragmentManager fm;
	private float scale;
	private boolean IsBlured;
	private static float minAlpha = 0.6f;
	private static float maxAlpha = 1.0f;
	private static float minDegree = 15.0f;
	private ViewPager mViewPager;
	private ArrayList<BannerEntity> mBanners;

	public void setViewPager(ViewPager vp) {
		mViewPager = vp;
	}

	public static float getMinDegree() {
		return minDegree;
	}

	public static float getMinAlpha() {
		return minAlpha;
	}

	public static float getMaxAlpha() {
		return maxAlpha;
	}

	public Theme1PagerAdapter(Context context, FragmentManager fm,
			ArrayList<BannerEntity> banners) {
		super(fm);
		lastPage = ((banners.size() * 200) / 2);
		this.fm = fm;
		this.context = context;
		mBanners = banners;
	}

	@Override
	public Fragment getItem(int position) {
		if (position == ((mBanners.size() * 200) / 2)) {
			scale = Common.BIG_SCALE;
		} else {
			scale = Common.SMALL_SCALE;
			IsBlured = false;

		}
		Fragment curFragment = FragmentPagerBanner.newInstance(context,
				position, scale, IsBlured);

		int realPosition = (position % mBanners.size());
		((FragmentPagerBanner) curFragment).setBannerEntity(mBanners
				.get(realPosition));
		// String url = mBanners.get(realPosition).getImage();
		// if (url != null) {
		// ((FragmentPagerBanner) curFragment).setUrlImage(url);
		// }
		// String urlAd = mBanners.get(realPosition).getUrl();
		// if (null != urlAd) {
		// ((FragmentPagerBanner) curFragment).setUrlAd(urlAd);
		// }
		return curFragment;
	}

	@Override
	public int getCount() {
		return mBanners.size() * 200;
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		if (positionOffset < 0 || positionOffset >= 1f) {
			ViewHelper.setAlpha(cur, maxAlpha);
		}
		if (positionOffset >= 0f && positionOffset <= 1f) {
			positionOffset = positionOffset * positionOffset;
			cur = getRootView(position);
			next = getRootView(position + 1);
			prev = getRootView(position - 1);
			nextnext = getRootView(position + 2);

			ViewHelper.setAlpha(cur, maxAlpha);
			ViewHelper.setAlpha(next, maxAlpha);
			ViewHelper.setAlpha(prev, maxAlpha);
			if (nextnext != null) {
				ViewHelper.setAlpha(nextnext, maxAlpha);
				ViewHelper.setRotationY(nextnext, -minDegree);
			}
			if (cur != null) {
				cur.setScaleBoth(Common.BIG_SCALE - Common.DIFF_SCALE
						* positionOffset);
				ViewHelper.setRotationY(cur, 0);
			}

			if (next != null) {
				next.setScaleBoth(Common.SMALL_SCALE + Common.DIFF_SCALE
						* positionOffset);
				ViewHelper.setRotationY(next, -minDegree);
			}
			if (prev != null) {
				ViewHelper.setRotationY(prev, minDegree);
			}

			if (swipedLeft) {
				if (next != null)
					ViewHelper.setRotationY(next, -minDegree + minDegree
							* positionOffset);
				if (cur != null)
					ViewHelper
							.setRotationY(cur, 0 + minDegree * positionOffset);
			} else {
				if (next != null)
					ViewHelper.setRotationY(next, -minDegree + minDegree
							* positionOffset);
				if (cur != null) {
					ViewHelper
							.setRotationY(cur, 0 + minDegree * positionOffset);
				}
			}
		}

	}

	@Override
	public void onPageSelected(int position) {
		if (lastPage <= position) {
			swipedLeft = true;
		} else if (lastPage > position) {
			swipedLeft = false;
		}
		lastPage = position;
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	}

	private MyLinearLayout getRootView(int position) {
		MyLinearLayout ly;
		try {
			ly = (MyLinearLayout) fm
					.findFragmentByTag(this.getFragmentTag(position)).getView()
					.findViewById(Rconfig.getInstance().id("root"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}
		if (ly != null)
			return ly;
		return null;
	}

	private String getFragmentTag(int position) {
		return "android:switcher:" + mViewPager.getId() + ":" + position;
	}
}
