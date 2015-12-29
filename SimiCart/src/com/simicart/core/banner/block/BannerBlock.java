package com.simicart.core.banner.block;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.URLUtil;

import com.simicart.MainActivity;
import com.simicart.core.banner.animation.SliderLayout;
import com.simicart.core.banner.animation.Animations.DescriptionAnimation;
import com.simicart.core.banner.animation.Indicators.PagerIndicator.IndicatorVisibility;
import com.simicart.core.banner.animation.SliderTypes.BaseSliderView;
import com.simicart.core.banner.animation.SliderTypes.BaseSliderView.OnSliderClickListener;
import com.simicart.core.banner.animation.SliderTypes.DefaultSliderView;
import com.simicart.core.banner.animation.Transformers.BaseTransformer;
import com.simicart.core.banner.delegate.BannerDelegate;
import com.simicart.core.banner.entity.BannerEntity;
import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.catalog.category.fragment.CategoryFragment;
import com.simicart.core.catalog.product.fragment.ProductDetailParentFragment;
import com.simicart.core.catalog.search.entity.TagSearch;
import com.simicart.core.catalog.search.fragment.ListProductFragment;
import com.simicart.core.catalog.search.model.ConstantsSearch;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.event.controller.EventController;
import com.simicart.core.slidemenu.fragment.CateSlideMenuFragment;

public class BannerBlock extends SimiBlock implements BannerDelegate {

	// protected ViewFlipper mBannerFlipper;
	protected SliderLayout mSliderLayout;
	protected View mView;
	protected Context mContext;
	protected Activity activity;
	private String TYPE_PRODUCT = "1";
	private String TYPE_CATEGORY = "2";
	private String TYPE_WEB = "3";

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public BannerBlock(View view, Context context) {
		this.mView = view;
		this.mContext = context;
	}

	public SliderLayout getSliderLayout() {
		return mSliderLayout;
	}

	public void setContext(Context context) {
		mContext = context;
	}

	public void setRootView(View rootView) {
		mView = rootView;
	}

	@Override
	public void initView() {
		// mBannerFlipper = (ViewFlipper)
		// mView.findViewById(Rconfig.getInstance()
		// .id("banner_slider"));
		mSliderLayout = (SliderLayout) mView.findViewById(Rconfig.getInstance()
				.id("banner_slider"));
	}

	@Override
	public void drawView(SimiCollection collection) {
		ArrayList<SimiEntity> listBanner = collection.getCollection();
		System.out.println(listBanner);
		if (listBanner == null || listBanner.size() == 0) {
			showBannersFake();
		} else {
			// showBanners(listBanner);
			showBannersNew(listBanner);
		}
	}

	private void showBannersFake() {
		for (int i = 0; i < 3; i++) {
			final BannerEntity bannerEntity = new BannerEntity();
			bannerEntity.setUrl("");
			bannerEntity.setImage("");
			bannerEntity.setType("");
			bannerEntity.setCategoryName("");
			bannerEntity.setCategoryId("");
			bannerEntity.setHasChild("");
			bannerEntity.setProductId("");
			DefaultSliderView textSliderView = new DefaultSliderView(
					MainActivity.instance);
			textSliderView.image(Rconfig.getInstance().drawable("fake_banner"))
					.setScaleType(BaseSliderView.ScaleType.Fit)
					.setOnSliderClickListener(new OnSliderClickListener() {

						@Override
						public void onSliderClick(BaseSliderView slider) {
							final BannerEntity banner_ad = bannerEntity;
							onSliderClickListener(banner_ad);
						}
					});
			mSliderLayout.addSlider(textSliderView);
			mSliderLayout
					.setPresetTransformer(SliderLayout.Transformer.Default);
			mSliderLayout.setIndicatorVisibility(IndicatorVisibility.Invisible);
			mSliderLayout.setCustomAnimation(new DescriptionAnimation());
			mSliderLayout.setDuration(3000);
		}
	}

	private void showBannersNew(ArrayList<SimiEntity> listBanner) {
		for (int i = 0; i < listBanner.size(); i++) {
			SimiEntity entity = listBanner.get(i);
			final BannerEntity bannerEntity = new BannerEntity();
			bannerEntity.setUrl(entity.getData(Constants.URL));
			bannerEntity.setImage(listBanner.get(i).getData(
					Constants.IMAGE_PATH));
			bannerEntity.setType(entity.getData("type"));
			bannerEntity.setCategoryName(entity.getData("categoryName"));
			bannerEntity.setCategoryId(entity.getData("categoryID"));
			bannerEntity.setHasChild(entity.getData(Constants.HAS_CHILD));
			bannerEntity.setProductId(entity.getData("productID"));
			DefaultSliderView textSliderView = new DefaultSliderView(
					MainActivity.instance);
			textSliderView.image(bannerEntity.getImage())
					.setOnSliderClickListener(new OnSliderClickListener() {

						@Override
						public void onSliderClick(BaseSliderView slider) {
							final BannerEntity banner_ad = bannerEntity;
							onSliderClickListener(banner_ad);
						}
					});
			if (DataLocal.isTablet) {
				textSliderView
						.setScaleType(BaseSliderView.ScaleType.CenterInside);
			} else {
				textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
			}
			mSliderLayout.addSlider(textSliderView);
		}
		mSliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);
		mSliderLayout
				.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
		mSliderLayout.setIndicatorVisibility(IndicatorVisibility.Invisible);
		mSliderLayout.setCustomAnimation(new DescriptionAnimation());
		if (listBanner.size() < 2) {
			// disable scroll, tranfer
			mSliderLayout.stopAutoCycle();
			mSliderLayout.setPagerTransformer(false, new BaseTransformer() {
				@Override
				protected void onTransform(View view, float v) {
				}
			});
		}
		// mSliderLayout.set
		mSliderLayout.setDuration(3000);

	}

	public void onSliderClickListener(BannerEntity banner_ad) {
		SimiManager.getIntance().hideKeyboard();
		EventController dispacth = new EventController();
		dispacth.dispatchEvent("com.simicart.banner.touchEvent",
				banner_ad.getUrl());
		// end dispatch
		SimiFragment fragment = null;
		if (banner_ad.getType() != null) {
			if (banner_ad.getType().equals(TYPE_PRODUCT)) {
				if (banner_ad.getProductId() != null
						&& !banner_ad.getProductId().equals("")
						&& !banner_ad.getProductId().toLowerCase()
								.equals("null")) {
					fragment = ProductDetailParentFragment.newInstance();
					((ProductDetailParentFragment) fragment)
							.setProductID(banner_ad.getProductId());
					SimiManager.getIntance().addFragment(fragment);
				}
			} else if (banner_ad.getType().equals(TYPE_CATEGORY)) {
				if (banner_ad.getCategoryId() != null
						&& !banner_ad.getCategoryId().equals("")
						&& !banner_ad.getCategoryId().toLowerCase()
								.equals("null")) {
					if (banner_ad.getHasChild() != null
							&& !banner_ad.getHasChild().equals("")
							&& !banner_ad.getHasChild().toLowerCase()
									.equals("null")) {
						if (banner_ad.getHasChild().equals("1")) {
							if (DataLocal.isTablet) {
								fragment = CategoryFragment.newInstance(
										banner_ad.getCategoryName(),
										banner_ad.getCategoryId());
								CateSlideMenuFragment.getIntance()
										.replaceFragmentCategoryMenu(fragment);
								CateSlideMenuFragment.getIntance().openMenu();
							} else {
								fragment = CategoryFragment.newInstance(
										banner_ad.getCategoryName(),
										banner_ad.getCategoryId());
								SimiManager.getIntance().addFragment(fragment);
							}
						} else {
							fragment = ListProductFragment.newInstance();
							((ListProductFragment) fragment)
									.setCategoryId(banner_ad.getCategoryId());
							((ListProductFragment) fragment)
									.setUrlSearch(ConstantsSearch.url_category);
							if (DataLocal.isTablet) {
								((ListProductFragment) fragment)
										.setTag_search(TagSearch.TAG_GRIDVIEW);
							}
							SimiManager.getIntance().addFragment(fragment);
						}
					}
				}
			} else if (banner_ad.getType().equals(TYPE_WEB)) {
				if (banner_ad.getUrl() != null
						&& !banner_ad.getUrl().equals("null")
						&& !banner_ad.getUrl().equals("")
						&& URLUtil.isValidUrl(banner_ad.getUrl())) {
					try {
						Intent browserIntent = new Intent(Intent.ACTION_VIEW,
								Uri.parse(banner_ad.getUrl()));
						mContext.startActivity(browserIntent);
					} catch (Exception e) {
					}
				}
			} else {
				if (banner_ad.getUrl() != null
						&& !banner_ad.getUrl().equals("null")
						&& !banner_ad.getUrl().equals("")
						&& URLUtil.isValidUrl(banner_ad.getUrl())) {
					try {
						Intent browserIntent = new Intent(Intent.ACTION_VIEW,
								Uri.parse(banner_ad.getUrl()));
						mContext.startActivity(browserIntent);
					} catch (Exception e) {
					}
				}
			}
		}
	}

}
