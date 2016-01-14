package com.simicart.core.catalog.category.block;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.devsmart.android.ui.HorizontalListView;
import com.simicart.core.adapter.ProductBaseAdapter;
import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.home.controller.ProductListListenerController;
import com.simicart.core.slidemenu.fragment.CateSlideMenuFragment;

public class CategoryDetailBlock extends SimiBlock implements SimiDelegate {
	protected String mName;
	protected String mID;
	protected TextView tv_CategoryName;
	protected TextView tv_viewmore;
	protected ImageView iv_showmore;
	protected LinearLayout ll_listParent;
	protected RelativeLayout rl_categoryParentLable;
	private RelativeLayout layout_showmore;

	private LinearLayout layout_category;

	public CategoryDetailBlock(View view, Context context) {
		super(view, context);
		if (DataLocal.isTablet) {
			view.setBackgroundColor(Config.getInstance().getMenu_background());
		}
	}

	public void setCategoryName(String mID, String name) {
		this.mID = mID;
		mName = name;
	}

	public void setClickViewMore(OnClickListener clicker) {
		layout_showmore.setOnClickListener(clicker);
	}

	public void setClickCategoryNameViewMore(OnClickListener clicker) {
		tv_CategoryName.setOnClickListener(clicker);
	}

	@Override
	public void initView() {
		layout_category = (LinearLayout) mView.findViewById(Rconfig
				.getInstance().id("ll_categories"));
		rl_categoryParentLable = (RelativeLayout) mView.findViewById(Rconfig
				.getInstance().id("rl_categoryParentLable"));
		tv_CategoryName = (TextView) mView.findViewById(Rconfig.getInstance()
				.id("tv_category"));
		tv_CategoryName.setText(Config.getInstance().getText(mName)
				.toUpperCase());
		if (DataLocal.isLanguageRTL) {
			tv_CategoryName.setGravity(Gravity.RIGHT);
		}
		tv_viewmore = (TextView) mView.findViewById(Rconfig.getInstance().id(
				"tv_viewmore"));
		if (DataLocal.isTablet) {
			tv_viewmore.setTextColor(Config.getInstance().getMenu_text_color());
			tv_CategoryName.setTextColor(Config.getInstance()
					.getMenu_text_color());
		} else {
			tv_viewmore.setTextColor(Config.getInstance().getContent_color());
			tv_CategoryName.setTextColor(Config.getInstance()
					.getContent_color());
		}
		tv_viewmore.setText(Config.getInstance().getText("View more"));
		iv_showmore = (ImageView) mView.findViewById(Rconfig.getInstance().id(
				"iv_showmore"));
		Drawable icon = mContext.getResources().getDrawable(
				Rconfig.getInstance().drawable("ic_view_all"));
		icon.setColorFilter(Config.getInstance().getContent_color(),
				PorterDuff.Mode.SRC_ATOP);
		iv_showmore.setImageDrawable(icon);

		ll_listParent = (LinearLayout) mView.findViewById(Rconfig.getInstance()
				.id("ll_listParent"));
		layout_showmore = (RelativeLayout) mView.findViewById(Rconfig
				.getInstance().id("layout_showmore"));

		View v_line = (View) mView.findViewById(Rconfig.getInstance().id(
				"v_line"));
		if (DataLocal.isTablet) {
			v_line.setBackgroundColor(Config.getInstance().getMenu_line_color());
		} else {
			v_line.setBackgroundColor(Config.getInstance().getLine_color());
		}
	}

	@Override
	public void drawView(SimiCollection collection) {
		if (DataLocal.isTablet) {
			tv_viewmore.setVisibility(View.VISIBLE);
			ll_listParent.setVisibility(View.GONE);
			iv_showmore.setVisibility(View.VISIBLE);

			ImageView iv_back = (ImageView) mView.findViewById(Rconfig
					.getInstance().id("iv_back"));
			Drawable icon_back = mContext.getResources().getDrawable(
					Rconfig.getInstance().drawable("ic_back"));
			icon_back.setColorFilter(Config.getInstance().getMenu_icon_color(),
					PorterDuff.Mode.SRC_ATOP);
			iv_back.setImageDrawable(icon_back);

			if (mID.equals("-1")) {
				iv_back.setVisibility(View.GONE);
			}

			iv_back.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					CateSlideMenuFragment.getIntance()
							.backFragmentCategoryMenu();
				}
			});
			Drawable icon = mContext.getResources().getDrawable(
					Rconfig.getInstance().drawable("ic_view_all"));
			icon.setColorFilter(Config.getInstance().getMenu_icon_color(),
					PorterDuff.Mode.SRC_ATOP);
			iv_showmore.setImageDrawable(icon);
		} else {
			ArrayList<SimiEntity> entity = collection.getCollection();
			if (null != entity && entity.size() > 0) {
				ArrayList<Product> products = new ArrayList<Product>();
				for (SimiEntity simiEntity : entity) {
					Product product = (Product) simiEntity;
					products.add(product);
				}

				if (products.size() > 0) {
					HorizontalListView listview_spotproduct = new HorizontalListView(
							mContext, null);
					RelativeLayout.LayoutParams listh = new RelativeLayout.LayoutParams(
							RelativeLayout.LayoutParams.MATCH_PARENT,
							RelativeLayout.LayoutParams.WRAP_CONTENT);
					listview_spotproduct.setLayoutParams(listh);

					ProductBaseAdapter productAdapter = new ProductBaseAdapter(
							mContext, products);
					productAdapter.setIsHome(true);
					listview_spotproduct.setAdapter(productAdapter);
					ProductListListenerController listner = new ProductListListenerController();
					listner.setProductList(products);
					listview_spotproduct.setOnItemClickListener(listner
							.createTouchProductList());
					ll_listParent.addView(listview_spotproduct);

					YoYo.with(Techniques.Shake).duration(2000)
							.playOn(listview_spotproduct);
					layout_category.setVisibility(View.VISIBLE);
				}

				tv_viewmore.setVisibility(View.VISIBLE);
				ll_listParent.setVisibility(View.VISIBLE);
				iv_showmore.setVisibility(View.VISIBLE);
				tv_CategoryName.setEnabled(true);
			} else {
				tv_CategoryName.setEnabled(false);
				tv_viewmore.setVisibility(View.GONE);
				ll_listParent.setVisibility(View.GONE);
				iv_showmore.setVisibility(View.GONE);
			}
		}
	}
}
