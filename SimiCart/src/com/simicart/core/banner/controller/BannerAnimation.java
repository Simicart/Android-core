package com.simicart.core.banner.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.URLUtil;
import android.widget.ViewFlipper;

import com.simicart.core.banner.entity.BannerEntity;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.catalog.category.fragment.CategoryFragment;
import com.simicart.core.catalog.product.fragment.ProductDetailParentFragment;
import com.simicart.core.catalog.search.entity.TagSearch;
import com.simicart.core.catalog.search.fragment.ListProductFragment;
import com.simicart.core.catalog.search.model.ConstantsSearch;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.event.controller.EventController;
import com.simicart.core.slidemenu.fragment.CateSlideMenuFragment;

@SuppressLint("ClickableViewAccessibility")
public class BannerAnimation {
	View parent_view;
	float lastX;
	ViewFlipper mBannerFlipper;
	Context context;
	protected boolean isAnimation = true;
	private String TYPE_PRODUCT = "1";
	private String TYPE_CATEGORY = "2";
	private String TYPE_WEB = "3";

	public void isAnimation(boolean isAni) {
		isAnimation = isAni;
	}

	public BannerAnimation(View view, ViewFlipper mBannerFlipper) {
		this.parent_view = view;
		this.mBannerFlipper = mBannerFlipper;
		this.mBannerFlipper.setFlipInterval(4500);
		this.context = view.getContext();
		if (isAnimation) {
			this.mBannerFlipper.setInAnimation(view.getContext(), Rconfig
					.getInstance().getId("in_from_right", "anim"));
			this.mBannerFlipper.setOutAnimation(view.getContext(), Rconfig
					.getInstance().getId("out_to_left", "anim"));
			this.mBannerFlipper.startFlipping();
		}
	}

	public BannerAnimation(View view, ViewFlipper mBannerFlipper, boolean isAn) {
		this.parent_view = view;
		this.mBannerFlipper = mBannerFlipper;
		this.mBannerFlipper.setFlipInterval(10500);
		this.context = view.getContext();
		isAnimation = isAn;
		if (isAnimation) {
			this.mBannerFlipper.setInAnimation(view.getContext(), Rconfig
					.getInstance().getId("in_from_right", "anim"));
			this.mBannerFlipper.setOutAnimation(view.getContext(), Rconfig
					.getInstance().getId("out_to_left", "anim"));
			this.mBannerFlipper.startFlipping();
		}
	}

	public void onTouchEvent(BannerEntity banner, View bannerView) {
		final BannerEntity banner_ad = banner;
		bannerView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent touchevent) {
				switch (touchevent.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					lastX = touchevent.getX();
					break;
				}

				case MotionEvent.ACTION_UP: {
					SimiManager.getIntance().hideKeyboard();
					float currentX = touchevent.getX();
					if (lastX == currentX) {
						// dispatch event for sent google analytic
						EventController dispacth = new EventController();
						dispacth.dispatchEvent(
								"com.simicart.banner.touchEvent",
								banner_ad.getUrl());
						// end dispatch
						SimiFragment fragment = null;
						if (banner_ad.getType() != null) {
							if (banner_ad.getType().equals(TYPE_PRODUCT)) {
								if (banner_ad.getProductId() != null
										&& !banner_ad.getProductId().equals("")
										&& !banner_ad.getProductId()
												.toLowerCase().equals("null")) {
									fragment = ProductDetailParentFragment
											.newInstance();
									((ProductDetailParentFragment) fragment)
											.setProductID(banner_ad
													.getProductId());
									SimiManager.getIntance().addFragment(
											fragment);
								}
							} else if (banner_ad.getType()
									.equals(TYPE_CATEGORY)) {
								if (banner_ad.getCategoryId() != null
										&& !banner_ad.getCategoryId()
												.equals("")
										&& !banner_ad.getCategoryId()
												.toLowerCase().equals("null")) {
									if (banner_ad.getHasChild() != null
											&& !banner_ad.getHasChild().equals(
													"")
											&& !banner_ad.getHasChild()
													.toLowerCase()
													.equals("null")) {
										if (banner_ad.getHasChild().equals("1")) {
											if (DataLocal.isTablet) {
												fragment = CategoryFragment.newInstance(
														banner_ad
																.getCategoryName(),
														banner_ad
																.getCategoryId());
												CateSlideMenuFragment
														.getIntance()
														.replaceFragmentCategoryMenu(
																fragment);
												CateSlideMenuFragment
														.getIntance()
														.openMenu();
											} else {
												fragment = CategoryFragment.newInstance(
														banner_ad
																.getCategoryName(),
														banner_ad
																.getCategoryId());
												SimiManager.getIntance()
														.addFragment(fragment);
											}
										} else {
											fragment = ListProductFragment
													.newInstance();
											((ListProductFragment) fragment)
													.setCategoryId(banner_ad
															.getCategoryId());
											((ListProductFragment) fragment).setUrlSearch(ConstantsSearch.url_category);
											if (DataLocal.isTablet) {
												((ListProductFragment) fragment)
														.setTag_search(TagSearch.TAG_GRIDVIEW);
											}
											SimiManager.getIntance()
													.addFragment(fragment);
										}
									}
								}
							} else if (banner_ad.getType().equals(TYPE_WEB)) {
								if (banner_ad.getUrl() != null
										&& !banner_ad.getUrl().equals("null")
										&& !banner_ad.getUrl().equals("")
										&& URLUtil.isValidUrl(banner_ad
												.getUrl())) {
									try {
										Intent browserIntent = new Intent(
												Intent.ACTION_VIEW, Uri
														.parse(banner_ad
																.getUrl()));
										context.startActivity(browserIntent);
									} catch (Exception e) {
									}
								}
							} else {
								if (banner_ad.getUrl() != null
										&& !banner_ad.getUrl().equals("null")
										&& !banner_ad.getUrl().equals("")
										&& URLUtil.isValidUrl(banner_ad
												.getUrl())) {
									try {
										Intent browserIntent = new Intent(
												Intent.ACTION_VIEW, Uri
														.parse(banner_ad
																.getUrl()));
										context.startActivity(browserIntent);
									} catch (Exception e) {
									}
								}
							}
						}
						break;
					}
					if (isAnimation) {
						if (lastX < currentX) {
							mBannerFlipper.setOutAnimation(null);
							mBannerFlipper.setInAnimation(null);
							mBannerFlipper.setInAnimation(
									v.getContext(),
									Rconfig.getInstance().getId("in_from_left",
											"anim"));
							mBannerFlipper.setOutAnimation(
									v.getContext(),
									Rconfig.getInstance().getId("out_to_right",
											"anim"));
							mBannerFlipper.showNext();
						}
						if (lastX > currentX) {
							mBannerFlipper.setOutAnimation(null);
							mBannerFlipper.setInAnimation(null);
							mBannerFlipper.setInAnimation(
									v.getContext(),
									Rconfig.getInstance().getId(
											"in_from_right", "anim"));
							mBannerFlipper.setOutAnimation(
									v.getContext(),
									Rconfig.getInstance().getId("out_to_left",
											"anim"));
							mBannerFlipper.showPrevious();
						}
					}
					break;
				}
				}
				return true;
			}
		});
	}
}
