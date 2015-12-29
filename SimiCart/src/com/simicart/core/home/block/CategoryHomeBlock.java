package com.simicart.core.home.block;

import java.util.ArrayList;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.devsmart.android.ui.HorizontalListView;
import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.catalog.category.entity.Category;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.home.adapter.HomeCategoryAdapter;
import com.simicart.core.home.controller.CategoryHomeListener;
import com.simicart.core.home.delegate.CategoryHomeDelegate;

public class CategoryHomeBlock extends SimiBlock implements
		CategoryHomeDelegate {

	private View mView;
	private CategoryHomeListener categoryHomeListener;

	public CategoryHomeBlock(View view, Context context) {
		super(view, context);
		this.mView = view;
	}

	public void showCategorys(ArrayList<Category> listCategory) {
		LinearLayout ll_category = (LinearLayout) mView;
		// name
		RelativeLayout.LayoutParams title_lp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		TextView tv_name = new TextView(mContext);
		tv_name.setLayoutParams(title_lp);
		int height = 0;
		if (DataLocal.isTablet) {
			tv_name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 21);
		} else {
			tv_name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
		}
		int padd = Utils.getValueDp(2);
		tv_name.setPadding(Utils.getValueDp(7), Utils.getValueDp(10),
				Utils.getValueDp(7), Utils.getValueDp(10));
		tv_name.setText(Config.getInstance().getText("Category").toUpperCase());
		if (DataLocal.isLanguageRTL) {
			tv_name.setGravity(Gravity.RIGHT);
		}
		tv_name.setTextColor(Config.getInstance().getContent_color());

		LinearLayout ll_cat = new LinearLayout(mContext);
		if (DataLocal.isTablet) {
			height = Utils.getValueDp(250);
			LinearLayout.LayoutParams spot_ll = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, height);
			ll_cat.setPadding(0, padd, 0, 0);
			ll_cat.setLayoutParams(spot_ll);
		} else {
			height = Utils.getValueDp(140);
			LinearLayout.LayoutParams spot_ll = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, height);
			ll_cat.setPadding(0, padd, 0, 0);
			ll_cat.setLayoutParams(spot_ll);
		}
		HorizontalListView listview_category = new HorizontalListView(mContext,
				null);
		RelativeLayout.LayoutParams listh = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		listview_category.setLayoutParams(listh);

		HomeCategoryAdapter adapter = new HomeCategoryAdapter(mContext,
				listCategory);
		listview_category.setAdapter(adapter);
		categoryHomeListener = new CategoryHomeListener();
		categoryHomeListener.setListCategory(listCategory);
		listview_category.setOnItemClickListener(categoryHomeListener
				.createOnTouchCategory());

		ll_cat.addView(listview_category);
		ll_category.addView(tv_name);
		ll_category.addView(ll_cat);

		View view = new View(mContext);
		view.setBackgroundColor(Config.getInstance().getLine_color());
		LinearLayout.LayoutParams lp_view = new LinearLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, 1);
		ll_category.addView(view, lp_view);

		YoYo.with(Techniques.Shake).duration(2000).playOn(listview_category);

	}

	private void showFakeCategorys() {
		ArrayList<Category> categories = new ArrayList<Category>();
		for (int i = 0; i < 4; i++) {
			Category category1 = new Category();
			category1.setCategoryId("fake");
			category1.setCategoryName("Category " + i);
			category1.setChild(false);
			category1.setCategoryImage("fake_category");
			categories.add(category1);
		}
		showCategorys(categories);
	}

	@Override
	public void drawView(SimiCollection collection) {
		ArrayList<SimiEntity> entity = collection.getCollection();
		if (null != entity && entity.size() > 0) {
			ArrayList<Category> categories = new ArrayList<Category>();
			for (SimiEntity simiEntity : entity) {
				Category category = (Category) simiEntity;
				categories.add(category);
			}
			if (categories.size() > 0) {
				showCategorys(categories);
			}
		} else {
			if (Config.getInstance().getDemoEnable().equals("DEMO_ENABLE")
					|| Config.getInstance().getDemoEnable().toUpperCase()
							.equals("YES")) {
				showFakeCategorys();
			} else {
				mView.setVisibility(View.GONE);
			}
		}
	}
}
