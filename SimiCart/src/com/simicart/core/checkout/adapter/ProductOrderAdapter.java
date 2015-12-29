package com.simicart.core.checkout.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.simicart.core.checkout.entity.Cart;
import com.simicart.core.common.DrawableManager;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;

public class ProductOrderAdapter extends BaseAdapter {
	ArrayList<Cart> mListCart;
	View rootView;
	Context context;
	LayoutInflater inflater;
	protected String mCurrecySymbol;

	public void setCurrencySymbol(String symbol) {
		mCurrecySymbol = symbol;
	}

	public ProductOrderAdapter(Context context, ArrayList<Cart> listProductOrder) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.mListCart = listProductOrder;
	}

	@Override
	public int getCount() {
		return mListCart.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (DataLocal.isLanguageRTL) {
			convertView = this.inflater.inflate(
					Rconfig.getInstance().layout(
							"rtl_listitem_product_orderhis"), null);
		} else {
			convertView = this.inflater.inflate(
					Rconfig.getInstance().layout(
							"core_listitem_product_orderhis"), null);
		}
		convertView.setBackgroundColor(Config.getInstance().getApp_backrground());

		Cart cart = mListCart.get(position);

		TextView name = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("name_product"));
		name.setTextColor(Config.getInstance().getContent_color());
		if (DataLocal.isLanguageRTL) {
			name.setGravity(Gravity.RIGHT);
		}
		name.setText(cart.getProduct_name());
		TextView tv_price = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("price_product"));
		if (DataLocal.isLanguageRTL) {
			tv_price.setGravity(Gravity.RIGHT);
		}
		tv_price.setTextColor(Color.parseColor(Config.getInstance()
				.getPrice_color()));
		String price = Config.getInstance().getPrice(
				Float.toString(cart.getProduct_price()));
		if (null != mCurrecySymbol) {
			price = Config.getInstance().getPrice(
					Float.toString(cart.getProduct_price()), mCurrecySymbol);
		}

		tv_price.setText(price);
		TextView qty = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("qty_product"));
		qty.setTextColor(Config.getInstance().getContent_color());
		qty.setText("" + cart.getQty());

		ImageView image = (ImageView) convertView.findViewById(Rconfig
				.getInstance().id("image_product"));
		String img = cart.getProduct_image();
		DrawableManager.fetchDrawableOnThread(img, image);

		return convertView;
	}

}
