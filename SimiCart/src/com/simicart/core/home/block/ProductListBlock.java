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
import com.simicart.core.adapter.ProductBaseAdapter;
import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.catalog.product.entity.ProductList;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.home.controller.ProductListListenerController;
import com.simicart.core.home.delegate.ProductListDelegate;

public class ProductListBlock extends SimiBlock implements ProductListDelegate {

	protected ProductListListenerController listner;

	public ProductListBlock(View view, Context context) {
		super(view, context);
	}

	@Override
	public void initView() {
	}

	public void showProduct(ArrayList<ProductList> productList) {
		int count = productList.size();

		for (int i = 0; i < count; i++) {
			RelativeLayout.LayoutParams title_lp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			LinearLayout ll_listProduct = (LinearLayout) mView
					.findViewById(Rconfig.getInstance().id("ll_spotproduct"));
			// name
			TextView tv_name = new TextView(mContext);
			tv_name.setLayoutParams(title_lp);
			int height = 0;

			if (DataLocal.isTablet) {
				tv_name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 21);
			} else {
				tv_name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
			}
			int padd = Utils.getValueDp(4);
			tv_name.setPadding(Utils.getValueDp(6), Utils.getValueDp(10),
					Utils.getValueDp(7), Utils.getValueDp(10));
			tv_name.setText(Config.getInstance().getText(
					productList.get(i).getTitle().toUpperCase()));
			tv_name.setTextColor(Config.getInstance().getContent_color());
			if (DataLocal.isTablet) {
				tv_name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 21);
			} else {
				tv_name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
			}
			if (DataLocal.isLanguageRTL) {
				tv_name.setGravity(Gravity.RIGHT);
			} else {
				tv_name.setGravity(Gravity.LEFT);
			}
			ll_listProduct.addView(tv_name);

			// product list
			LinearLayout llspot = new LinearLayout(mContext);
			if (DataLocal.isTablet) {
				height = Utils.getValueDp(250);
				LinearLayout.LayoutParams spot_ll = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT, height);
				llspot.setPadding(0, padd, 0, 0);
				llspot.setLayoutParams(spot_ll);
			} else {
				height = Utils.getValueDp(165);
				LinearLayout.LayoutParams spot_ll = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT, height);
				llspot.setPadding(0, padd, 0, 0);
				// before top is padd
				// spot_ll.setMargins(0, 0, Utils.getValueDp(7),
				// Utils.getValueDp(10));
				llspot.setLayoutParams(spot_ll);
			}
			HorizontalListView listview_spotproduct = new HorizontalListView(
					mContext, null);
			RelativeLayout.LayoutParams listh = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			listview_spotproduct.setLayoutParams(listh);

			if (null != productList.get(i).getSpotProduct()) {
				ProductBaseAdapter productAdapter = new ProductBaseAdapter(
						mContext, productList.get(i).getSpotProduct());
				productAdapter.setIsHome(true);
				listview_spotproduct.setAdapter(productAdapter);
			}
			listner = new ProductListListenerController();
			listner.setProductList(productList.get(i).getSpotProduct());
			listview_spotproduct.setOnItemClickListener(listner
					.createTouchProductList());
			llspot.addView(listview_spotproduct);
			ll_listProduct.addView(llspot);
			if (i != count - 1) {
				View view = new View(mContext);
				view.setBackgroundColor(Config.getInstance().getLine_color());
				LinearLayout.LayoutParams lp_view = new LinearLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT, 1);
				ll_listProduct.addView(view, lp_view);
			}
			YoYo.with(Techniques.Shake).duration(2000)
					.playOn(listview_spotproduct);
		}
	}

	@Override
	public void onUpdate(ArrayList<ProductList> productLists) {
		if (productLists == null || productLists.size() == 0) {
			ArrayList<Product> spotProducts = new ArrayList<>();
			for (int i = 0; i < 5; i++) {
				Product product = new Product();
				product.setId("fake");
				product.setName("Example Product " + i);
				spotProducts.add(product);
			}
			ProductList productList = new ProductList();
			productList.setSpotProduct(spotProducts);
			productList.setTitle("Feature Product");
			productLists = new ArrayList<>();
			productLists.add(productList);
		}
		showProduct(productLists);
	}
}
