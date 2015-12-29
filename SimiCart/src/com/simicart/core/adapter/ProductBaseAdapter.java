package com.simicart.core.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.common.DrawableManager;
import com.simicart.core.common.price.ProductPriceView;
import com.simicart.core.common.price.ProductPriceViewProductGridV03;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.event.block.EventBlock;

@SuppressLint("ViewHolder")
public class ProductBaseAdapter extends BaseAdapter {

	protected ArrayList<Product> mProductList;
	protected Context mContext;
	protected boolean isHome;

	public ProductBaseAdapter(Context context, ArrayList<Product> ProductList) {
		this.mContext = context;
		this.mProductList = ProductList;
	}

	public void setProductList(ArrayList<Product> products) {
		mProductList = products;
	}

	public void setIsHome(boolean isHome) {
		this.isHome = isHome;
	}

	@Override
	public int getCount() {
		return this.mProductList.size();
	}

	@Override
	public Product getItem(int position) {
		return this.mProductList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(
					Rconfig.getInstance().layout("core_product_list_details"),
					null);
			holder = new ViewHolder();
			holder.tv_name = (TextView) convertView.findViewById(Rconfig
					.getInstance().id("product_list_name"));
			holder.ll_price = (LinearLayout) convertView.findViewById(Rconfig
					.getInstance().id("layout_price"));
			holder.img_avartar = (ImageView) convertView.findViewById(Rconfig
					.getInstance().id("product_list_image"));
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Product product = getItem(position);

		// name
		holder.tv_name.setText(product.getName());
		holder.tv_name.setTextColor(Config.getInstance().getContent_color());
		if (isHome) {
			if (DataLocal.isLanguageRTL) {
				holder.tv_name.setGravity(Gravity.RIGHT);
			} else {
				holder.tv_name.setGravity(Gravity.LEFT);
			}
		}
		// price
		if (isHome) {
			if (DataLocal.isLanguageRTL) {
				holder.ll_price.setGravity(Gravity.RIGHT);
			} else {
				holder.ll_price.setGravity(Gravity.LEFT);
			}
		}

		if (product.getPriceV2() != null) {
			ProductPriceViewProductGridV03 viewPrice = new ProductPriceViewProductGridV03(
					product);
			viewPrice.setShowOnePrice(true);
			View view = viewPrice.getViewPrice();
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			if (null != view) {
				holder.ll_price.removeAllViewsInLayout();
				holder.ll_price.addView(view, params);
			}
		} else {
			Log.e("ProductBaseAdapter ", " get Price Home");
			ProductPriceView viewPrice = new ProductPriceView(product);
			View view = viewPrice.getViewPriceHome();
			if (null != view) {
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				if (DataLocal.isLanguageRTL) {
					holder.ll_price.setGravity(Gravity.RIGHT);
				} else {
					holder.ll_price.setGravity(Gravity.LEFT);
				}
				holder.ll_price.removeAllViewsInLayout();
				holder.ll_price.addView(view, params);
			}
		}

		// image
		if (product.getId().equals("fake")) {
			// set anh fake
			holder.img_avartar.setImageResource(Rconfig.getInstance().drawable(
					"fake_product"));
			holder.img_avartar.setScaleType(ScaleType.FIT_XY);
		} else {
			String urlImage = product.getImage();
			if (urlImage != null) {
				DrawableManager.fetchDrawableOnThread(urlImage,
						holder.img_avartar);
			}
		}

		// dispatchevent
		RelativeLayout rl_product_list = (RelativeLayout) convertView
				.findViewById(Rconfig.getInstance().id("rel_product_list_spot"));

		EventBlock eventBlock = new EventBlock();
		eventBlock.dispatchEvent("com.simicart.image.product.home",
				rl_product_list, product);

		return convertView;
	}

	static class ViewHolder {
		ImageView img_avartar;
		TextView tv_name;
		LinearLayout layoutStock;
		LinearLayout ll_price;
	}

}
