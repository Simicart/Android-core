package com.simicart.theme.matrixtheme.home.common;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nineoldandroids.view.ViewHelper;
import com.simicart.core.banner.entity.BannerEntity;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.catalog.category.fragment.CategoryFragment;
import com.simicart.core.catalog.product.fragment.ProductDetailParentFragment;
import com.simicart.core.catalog.search.entity.TagSearch;
import com.simicart.core.catalog.search.fragment.ListProductFragment;
import com.simicart.core.catalog.search.model.ConstantsSearch;
import com.simicart.core.common.DrawableManager;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.slidemenu.fragment.CateSlideMenuFragment;
import com.simicart.theme.matrixtheme.home.adapter.Theme1PagerAdapter;

public class FragmentPagerBanner extends Fragment {

	private String mUrlImage;
	private String mUrlAd;
	private Context context;
	private BannerEntity bannerEntity;
	private String TYPE_PRODUCT = "1";
	private String TYPE_CATEGORY = "2";
	private String TYPE_WEB = "3";

	public void setUrlImage(String url) {
		mUrlImage = url;
	}

	public void setUrlAd(String url) {
		mUrlAd = url;
	}

	public void setBannerEntity(BannerEntity bannerEntity) {
		this.bannerEntity = bannerEntity;
	}

	public static Fragment newInstance(Context context, int pos, float scale,
			boolean IsBlured) {
		Bundle b = new Bundle();
		b.putInt("pos", pos);
		b.putFloat("scale", scale);
		b.putBoolean("IsBlured", IsBlured);
		return Fragment.instantiate(context,
				FragmentPagerBanner.class.getName(), b);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}

		context = getActivity();

		LinearLayout rootView = (LinearLayout) inflater.inflate(Rconfig
				.getInstance().layout("theme1_item_viewpager"), container,
				false);

		ImageView imageview = (ImageView) rootView.findViewById(Rconfig
				.getInstance().id("banner"));
		if (bannerEntity.getImage() != null) {
			DrawableManager.fetchDrawableOnThread(bannerEntity.getImage(),
					imageview);

		}
		imageview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SimiFragment fragment = null;
				if (bannerEntity.getType() != null) {
					if (bannerEntity.getType().equals(TYPE_PRODUCT)) {
						if (bannerEntity.getProductId() != null
								&& !bannerEntity.getProductId().equals("")
								&& !bannerEntity.getProductId().toLowerCase()
										.equals("null")) {
							fragment = ProductDetailParentFragment
									.newInstance();
							((ProductDetailParentFragment) fragment)
									.setProductID(bannerEntity.getProductId());
							SimiManager.getIntance().addFragment(fragment);
						}
					} else if (bannerEntity.getType().equals(TYPE_CATEGORY)) {
						if (bannerEntity.getCategoryId() != null
								&& !bannerEntity.getCategoryId().equals("")
								&& !bannerEntity.getCategoryId().toLowerCase()
										.equals("null")) {
							if (bannerEntity.getHasChild() != null
									&& !bannerEntity.getHasChild().equals("")
									&& !bannerEntity.getHasChild()
											.toLowerCase().equals("null")) {
								if (bannerEntity.getHasChild().equals("1")) {
									if (DataLocal.isTablet) {
										fragment = CategoryFragment.newInstance(
												bannerEntity.getCategoryName(),
												bannerEntity.getCategoryId());
										CateSlideMenuFragment.getIntance()
												.replaceFragmentCategoryMenu(
														fragment);
									} else {
										fragment = CategoryFragment.newInstance(
												bannerEntity.getCategoryName(),
												bannerEntity.getCategoryId());
										SimiManager.getIntance().addFragment(
												fragment);
									}
								} else {
									fragment = ListProductFragment.newInstance();
									((ListProductFragment) fragment)
											.setCategoryId(bannerEntity
													.getCategoryId());
									((ListProductFragment) fragment)
											.setUrlSearch(ConstantsSearch.url_category);
									if (DataLocal.isTablet) {
										((ListProductFragment) fragment)
												.setTag_search(TagSearch.TAG_GRIDVIEW);
									}
									SimiManager.getIntance().addFragment(
											fragment);
								}
							}
						}
					} else if (bannerEntity.getType().equals(TYPE_WEB)) {
						if (null != bannerEntity.getUrl()
								&& !bannerEntity.getUrl().equals("")
								&& !bannerEntity.getUrl().equals("null")
								&& URLUtil.isValidUrl(bannerEntity.getUrl())) {
							try {
								Intent browserIntent = new Intent(
										Intent.ACTION_VIEW, Uri
												.parse(bannerEntity.getUrl()));
								context.startActivity(browserIntent);
							} catch (Exception e) {

							}
						}
					} else {
						if (null != bannerEntity.getUrl()
								&& !bannerEntity.getUrl().equals("")
								&& !bannerEntity.getUrl().equals("null")
								&& URLUtil.isValidUrl(bannerEntity.getUrl())) {
							try {
								Intent browserIntent = new Intent(
										Intent.ACTION_VIEW, Uri
												.parse(bannerEntity.getUrl()));
								context.startActivity(browserIntent);
							} catch (Exception e) {

							}
						}
					}
				}
			}
		});

		MyLinearLayout root = (MyLinearLayout) rootView.findViewById(Rconfig
				.getInstance().id("root"));
		float scale = this.getArguments().getFloat("scale");
		root.setScaleBoth(scale);
		boolean isBlured = this.getArguments().getBoolean("IsBlured");
		if (isBlured) {
			ViewHelper.setAlpha(root, Theme1PagerAdapter.getMinAlpha());
			ViewHelper.setRotationY(root, Theme1PagerAdapter.getMinDegree());
		}
		return rootView;
	}

	@Override
	public void onDestroy() {
		super.onDestroyView();
	}

}
