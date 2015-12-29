package com.simicart.core.catalog.product.fragment;

import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.common.price.ProductPriceViewDetail;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.event.block.CacheBlock;
import com.simicart.core.event.block.EventBlock;

public class BasicInforFragment extends SimiFragment {
	protected Product mProduct;

	public static BasicInforFragment newInstance() {
		BasicInforFragment fragment = new BasicInforFragment();
		return fragment;
	}

	public void setProduct(Product product) {
		mProduct = product;
	}

	public Product getProduct() {
		return mProduct;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(
				Rconfig.getInstance().layout(
						"core_information_basic_inf_layout"), container, false);

		TextView tv_Name = (TextView) rootView.findViewById(Rconfig
				.getInstance().id("tv_Name"));
		tv_Name.setText(mProduct.getName().trim());
		tv_Name.setTextColor(Config.getInstance().getContent_color());

		// price
		LinearLayout ll_price = (LinearLayout) rootView.findViewById(Rconfig
				.getInstance().id("ll_price"));
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		ProductPriceViewDetail viewPrice = new ProductPriceViewDetail(mProduct);
		View view = viewPrice.getViewPrice();
		if (null != view) {
			if (DataLocal.isLanguageRTL) {
				params.gravity = Gravity.RIGHT;
				ll_price.setGravity(Gravity.RIGHT);
			}
			ll_price.removeAllViewsInLayout();
			ll_price.addView(view, params);
		}

		TextView tv_Stock = (TextView) rootView.findViewById(Rconfig
				.getInstance().id("tv_Stock"));
		tv_Stock.setTextColor(Config.getInstance().getContent_color());
		if (mProduct.getStock()) {
			tv_Stock.setText(Config.getInstance().getText("In Stock") + ".");
		} else {
			tv_Stock.setText(Config.getInstance().getText("Out Stock") + ".");
		}

		TextView tv_shortDescription = (TextView) rootView.findViewById(Rconfig
				.getInstance().id("tv_descrip"));
		tv_Name.setTextColor(Config.getInstance().getContent_color());
		if (mProduct.getShortDecripition() != null
				&& !mProduct.getShortDecripition().toLowerCase().equals("null")) {
			tv_shortDescription.setText(Html.fromHtml("<font color='"
					+ Config.getInstance().getContent_color_string() + "'>"
					+ mProduct.getShortDecripition() + "</font>"));
		}

		SimiCollection simiCollection = new SimiCollection();
		simiCollection.setJSON(mProduct.getJSONObject());
		CacheBlock cache = new CacheBlock();
		cache.setSimiCollection(simiCollection);
		cache.setView(rootView);
		EventBlock event = new EventBlock();
		event.dispatchEvent(
				"com.simicart.core.catalog.fragment.BasicInforFragment", cache);

		rootView.setBackgroundColor(Config.getInstance().getApp_backrground());

		return rootView;
	}
}
