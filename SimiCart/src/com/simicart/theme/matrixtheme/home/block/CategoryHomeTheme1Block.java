package com.simicart.theme.matrixtheme.home.block;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.ImageView.ScaleType;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.catalog.category.fragment.CategoryFragment;
import com.simicart.core.catalog.search.entity.TagSearch;
import com.simicart.core.catalog.search.fragment.ListProductFragment;
import com.simicart.core.common.DrawableManager;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.slidemenu.fragment.CateSlideMenuFragment;
import com.simicart.theme.matrixtheme.home.common.BannerUpDown;
import com.simicart.theme.matrixtheme.home.delegate.CategoryHomeTheme1Delegate;
import com.simicart.theme.matrixtheme.home.entity.Theme1Category;

public class CategoryHomeTheme1Block extends SimiBlock implements
		CategoryHomeTheme1Delegate {

	private TextView tv_Category1;
	private TextView tv_Category2;
	private TextView tv_Category3;
	private Button bt_Category1;
	private Button bt_Category2;
	private Button bt_Category3;
	private Button bt_AllCategory;
	private TextView tv_viewmore1;
	private TextView tv_viewmore2;
	private TextView tv_viewmore3;
	private TextView tv_view_all_category;

	public CategoryHomeTheme1Block(View view, Context context) {
		super(view, context);

		tv_Category1 = (TextView) mView.findViewById(Rconfig.getInstance().id(
				"tv_category1"));
		tv_Category2 = (TextView) mView.findViewById(Rconfig.getInstance().id(
				"tv_category2"));
		tv_Category3 = (TextView) mView.findViewById(Rconfig.getInstance().id(
				"tv_category3"));
		bt_Category1 = (Button) mView.findViewById(Rconfig.getInstance().id(
				"bt_category1"));
		bt_Category2 = (Button) mView.findViewById(Rconfig.getInstance().id(
				"bt_category2"));
		bt_Category3 = (Button) mView.findViewById(Rconfig.getInstance().id(
				"bt_category3"));
		bt_AllCategory = (Button) mView.findViewById(Rconfig.getInstance().id(
				"bt_all_category"));
		tv_view_all_category = (TextView) mView.findViewById(Rconfig
				.getInstance().id("tv_view_all_category"));
		tv_view_all_category.setText(Config.getInstance()
				.getText("VIEW ALL CATEGORIES").toUpperCase());
		TextView tv_viewnow = (TextView) mView.findViewById(Rconfig
				.getInstance().id("tv_viewnow"));
		tv_viewnow.setText(Config.getInstance().getText("View now"));
		tv_viewmore1 = (TextView) mView.findViewById(Rconfig.getInstance().id(
				"tv_viewmore1"));
		tv_viewmore2 = (TextView) mView.findViewById(Rconfig.getInstance().id(
				"tv_viewmore2"));
		tv_viewmore3 = (TextView) mView.findViewById(Rconfig.getInstance().id(
				"tv_viewmore3"));
		if (!DataLocal.isTablet) {
			tv_viewnow.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
			tv_viewmore1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
			tv_viewmore2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
			tv_viewmore3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
		}
		tv_viewmore1.setText(Config.getInstance().getText("View more") + ">>");
		tv_viewmore2.setText(Config.getInstance().getText("View more") + ">>");
		tv_viewmore3.setText(Config.getInstance().getText("View more") + ">>");
		tv_viewmore1.setTextColor(Color.parseColor("#4E4E4E"));
		tv_viewmore2.setTextColor(Color.parseColor("#4E4E4E"));
		tv_viewmore3.setTextColor(Color.parseColor("#4E4E4E"));
		tv_viewmore1.setVisibility(View.GONE);
		tv_viewmore2.setVisibility(View.GONE);
		tv_viewmore3.setVisibility(View.GONE);
		tv_Category1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
		tv_Category2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
		tv_Category3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
		tv_Category1.setText(Config.getInstance().getText("No category")
				.toUpperCase());
		tv_Category2.setText(Config.getInstance().getText("No category")
				.toUpperCase());
		tv_Category3.setText(Config.getInstance().getText("No category")
				.toUpperCase());
	}

	private void setCategory1View(final Theme1Category category) {
		if (category == null) {
			tv_Category1.setText("Category 1");
			ViewFlipper viewFlipper = (ViewFlipper) mView.findViewById(Rconfig
					.getInstance().id("vfp_category1"));
			ImageView imageView = new ImageView(mContext);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.MATCH_PARENT);
			imageView.setLayoutParams(lp);
			imageView.setImageResource(Rconfig.getInstance().drawable(
					"theme1_fake_category1"));
			imageView.setScaleType(ScaleType.FIT_XY);
			viewFlipper.addView(imageView);
			return;
		}

		tv_viewmore1.setVisibility(View.VISIBLE);

		String name = category.getCategoryName();
		if (null != name) {
			name = name.toUpperCase();
			tv_Category1.setText(name);
			if (DataLocal.isTablet) {
				tv_Category1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
			} else {
				tv_Category1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
			}
			tv_Category1.setTextColor(Color.parseColor("#000000"));
		}

		ArrayList<String> urlImages = category.getUrlImages();
		if (null == urlImages || urlImages.size() == 0
				|| (urlImages.size() == 1)) {
			ViewFlipper viewFlipper = (ViewFlipper) mView.findViewById(Rconfig
					.getInstance().id("vfp_category1"));
			ImageView imageView = new ImageView(mContext);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.MATCH_PARENT);
			imageView.setLayoutParams(lp);
			if (urlImages.get(0) == null) {
				imageView.setImageResource(Rconfig.getInstance().drawable(
						"default_logo"));
			} else {

				DrawableManager.fetchDrawableOnThread(urlImages.get(0),
						imageView);
			}
			viewFlipper.addView(imageView);
		} else {
			ViewFlipper viewFlipper = (ViewFlipper) mView.findViewById(Rconfig
					.getInstance().id("vfp_category1"));

			BannerUpDown bannerUpDown = new BannerUpDown(mContext, viewFlipper,
					4500);

			for (int i = 0; i < urlImages.size(); i++) {
				String url = urlImages.get(i);
				LayoutInflater inflater = (LayoutInflater) mView.getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				LinearLayout bannerView = (LinearLayout) inflater.inflate(
						Rconfig.getInstance().layout("theme1_banner_category"),
						null, false);
				ImageView imageView = (ImageView) bannerView
						.findViewById(Rconfig.getInstance().id(
								"image_banner_category"));

				DrawableManager.fetchDrawableOnThread(url, imageView);
				viewFlipper.addView(bannerView);
				bannerUpDown.onTouchEvent(url, bannerView);
			}
		}

		bt_Category1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				viewCategory(category);

			}
		});
	}

	private void setCategory2View(final Theme1Category category) {
		if (category == null) {
			tv_Category2.setText("Category 2");
			ViewFlipper viewFlipper = (ViewFlipper) mView.findViewById(Rconfig
					.getInstance().id("vfp_category2"));
			ImageView imageView = new ImageView(mContext);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.MATCH_PARENT);
			imageView.setLayoutParams(lp);
			imageView.setImageResource(Rconfig.getInstance().drawable(
					"theme1_fake_category2"));
			imageView.setScaleType(ScaleType.FIT_XY);
			viewFlipper.addView(imageView);
			return;
		}

		tv_viewmore2.setVisibility(View.VISIBLE);

		String name = category.getCategoryName();
		if (null != name) {
			name = name.toUpperCase();
			tv_Category2.setText(name);
			if (DataLocal.isTablet) {
				tv_Category2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
			} else {
				tv_Category2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
			}
			tv_Category2.setTextColor(Color.parseColor("#000000"));
		}

		ArrayList<String> urlImages = category.getUrlImages();
		if (null == urlImages || urlImages.size() == 0
				|| (urlImages.size() == 1)) {
			ViewFlipper viewFlipper = (ViewFlipper) mView.findViewById(Rconfig
					.getInstance().id("vfp_category2"));

			ImageView imageView = new ImageView(mContext);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.MATCH_PARENT);
			imageView.setLayoutParams(lp);
			if (urlImages.get(0) == null) {
				imageView.setImageResource(Rconfig.getInstance().drawable(
						"default_logo"));
			} else {
				DrawableManager.fetchDrawableOnThread(urlImages.get(0),
						imageView);
			}
			viewFlipper.addView(imageView);
		} else {
			ViewFlipper viewFlipper = (ViewFlipper) mView.findViewById(Rconfig
					.getInstance().id("vfp_category2"));

			BannerUpDown bannerUpDown = new BannerUpDown(mView.getContext(),
					viewFlipper, 4700);

			for (int i = 0; i < urlImages.size(); i++) {
				String url = urlImages.get(i);
				LayoutInflater inflater = (LayoutInflater) mView.getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				LinearLayout bannerView = (LinearLayout) inflater.inflate(
						Rconfig.getInstance().layout("theme1_banner_category"),
						null, false);
				ImageView imageView = (ImageView) bannerView
						.findViewById(Rconfig.getInstance().id(
								"image_banner_category"));

				DrawableManager.fetchDrawableOnThread(url, imageView);
				viewFlipper.addView(bannerView);
				bannerUpDown.onTouchEvent(url, bannerView);
			}
		}

		bt_Category2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				viewCategory(category);
			}
		});
	}

	private void setCategory3View(final Theme1Category category) {
		if (category == null) {
			tv_Category3.setText("Category 3");
			ViewFlipper viewFlipper = (ViewFlipper) mView.findViewById(Rconfig
					.getInstance().id("vfp_category3"));
			ImageView imageView = new ImageView(mContext);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.MATCH_PARENT);
			imageView.setLayoutParams(lp);
			imageView.setImageResource(Rconfig.getInstance().drawable(
					"theme1_fake_category2"));
			imageView.setScaleType(ScaleType.FIT_XY);
			viewFlipper.addView(imageView);
			return;
		}

		tv_viewmore3.setVisibility(View.VISIBLE);

		String name = category.getCategoryName();
		if (null != name) {
			name = name.toUpperCase();
			tv_Category3.setText(name);
			if (DataLocal.isTablet) {
				tv_Category3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
			} else {
				tv_Category3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
			}
			tv_Category3.setTextColor(Color.parseColor("#000000"));
		}

		ArrayList<String> urlImages = category.getUrlImages();
		if (null == urlImages || urlImages.size() == 0
				|| (urlImages.size() == 1)) {
			ViewFlipper viewFlipper = (ViewFlipper) mView.findViewById(Rconfig
					.getInstance().id("vfp_category3"));

			ImageView imageView = new ImageView(mContext);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.MATCH_PARENT);
			imageView.setLayoutParams(lp);
			if (urlImages.get(0) == null) {
				imageView.setImageResource(Rconfig.getInstance().drawable(
						"default_logo"));
			} else {
				DrawableManager.fetchDrawableOnThread(urlImages.get(0),
						imageView);
			}
			viewFlipper.addView(imageView);
		} else {
			ViewFlipper viewFlipper = (ViewFlipper) mView.findViewById(Rconfig
					.getInstance().id("vfp_category3"));

			BannerUpDown bannerUpDown = new BannerUpDown(mView.getContext(),
					viewFlipper, 4900);

			for (int i = 0; i < urlImages.size(); i++) {
				String url = urlImages.get(i);
				LayoutInflater inflater = (LayoutInflater) mView.getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				LinearLayout bannerView = (LinearLayout) inflater.inflate(
						Rconfig.getInstance().layout("theme1_banner_category"),
						null, false);
				ImageView imageView = (ImageView) bannerView
						.findViewById(Rconfig.getInstance().id(
								"image_banner_category"));

				DrawableManager.fetchDrawableOnThread(url, imageView);
				viewFlipper.addView(bannerView);
				bannerUpDown.onTouchEvent(url, bannerView);
			}
		}

		bt_Category3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				viewCategory(category);
			}
		});
	}

	private void setAllCategoryView(Theme1Category category) {
		if (category == null) {
			return;
		}

		String name = category.getCategoryName();
		if (null != name) {
			name = name.toUpperCase();
			tv_view_all_category.setText(Config.getInstance().getText(name));
			if (DataLocal.isTablet) {
				tv_view_all_category
						.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
			} else {
				tv_view_all_category
						.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
			}
			tv_view_all_category.setTextColor(Color.parseColor("#FFFFFF"));
		}

		ArrayList<String> urlImages = category.getUrlImages();
		if (null == urlImages || urlImages.size() == 0
				|| (urlImages.size() == 1)) {
			ViewFlipper viewFlipper = (ViewFlipper) mView.findViewById(Rconfig
					.getInstance().id("vfp_allcategory"));

			ImageView imageView = new ImageView(mContext);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.MATCH_PARENT);
			imageView.setLayoutParams(lp);
			if (urlImages.get(0) == null) {
				imageView.setImageResource(Rconfig.getInstance().drawable(
						"default_logo"));
			} else {
				DrawableManager.fetchDrawableOnThread(urlImages.get(0),
						imageView);
			}
			viewFlipper.addView(imageView);
		} else {
			ViewFlipper viewFlipper = (ViewFlipper) mView.findViewById(Rconfig
					.getInstance().id("vfp_allcategory"));

			BannerUpDown bannerUpDown = new BannerUpDown(mView.getContext(),
					viewFlipper, 5000);

			for (int i = 0; i < urlImages.size(); i++) {
				String url = urlImages.get(i);
				LayoutInflater inflater = (LayoutInflater) mView.getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				LinearLayout bannerView = (LinearLayout) inflater.inflate(
						Rconfig.getInstance().layout("theme1_banner_category"),
						null, false);
				ImageView imageView = (ImageView) bannerView
						.findViewById(Rconfig.getInstance().id(
								"image_banner_category"));

				DrawableManager.fetchDrawableOnThread(url, imageView);
				viewFlipper.addView(bannerView);
				bannerUpDown.onTouchEvent(url, bannerView);
			}
		}

		bt_AllCategory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				viewAllCategory();
			}
		});

	}

	private void viewCategory(final Theme1Category category) {
		SimiFragment fragment = null;
		if (category.isHasChild()) {
			fragment = CategoryFragment.newInstance(category.getCategoryName(),
					category.getCategoryID());
			if (DataLocal.isTablet) {
				CateSlideMenuFragment.getIntance().replaceFragmentCategoryMenu(
						fragment);
				CateSlideMenuFragment.getIntance().openMenu();
			} else {
				SimiManager.getIntance().replaceFragment(fragment);
			}
		} else {
			fragment = ListProductFragment.newInstance();
			((ListProductFragment) fragment).setCategoryId(category
					.getCategoryID());
			((ListProductFragment) fragment).setCategoryName(category
					.getCategoryName());
			if (category.getCategoryID().equals("-1")) {
				((ListProductFragment) fragment)
						.setUrlSearch(Constants.GET_ALL_PRODUCTS);
			} else {
				((ListProductFragment) fragment)
						.setUrlSearch(Constants.GET_CATEGORY_PRODUCTS);
			}
			if (DataLocal.isTablet) {
				((ListProductFragment) fragment)
						.setTag_search(TagSearch.TAG_GRIDVIEW);
			}
			SimiManager.getIntance().removeDialog();
			SimiManager.getIntance().replaceFragment(fragment);
		}
	}

	private void viewAllCategory() {
		if (DataLocal.isTablet) {
			CategoryFragment fr_Category = CategoryFragment.newInstance(Config
					.getInstance().getText("all categories"), "-1");
			CateSlideMenuFragment.getIntance().replaceFragmentCategoryMenu(
					fr_Category);
			CateSlideMenuFragment.getIntance().openMenu();
		} else {
			CategoryFragment fr_Category = CategoryFragment.newInstance(Config
					.getInstance().getText("all categories"), "-1");
			SimiManager.getIntance().replacePopupFragment(fr_Category);
		}
	}

	@Override
	public void drawView(SimiCollection collection) {
		ArrayList<SimiEntity> entity = collection.getCollection();
		Theme1Category category1 = null;
		Theme1Category category2 = null;
		Theme1Category category3 = null;
		Theme1Category category4 = null;
		ArrayList<Theme1Category> categories = new ArrayList<Theme1Category>(4);
		if (null != entity && entity.size() > 0) {
			for (SimiEntity simiEntity : entity) {
				Theme1Category category = (Theme1Category) simiEntity;
				categories.add(category);
			}

			int size = categories.size();
			if (size > 0) {
				category1 = categories.get(0);
				if (size > 1) {
					category2 = categories.get(1);
					if (size > 2) {
						category3 = categories.get(2);
						if (size > 3) {
							category4 = categories.get(3);
						}
					}
				}
			}
		}
		setCategory1View(category1);
		setCategory2View(category2);
		setCategory3View(category3);
		setAllCategoryView(category4);
	}
}
