package com.simicart.theme.matrixtheme.home.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.catalog.search.entity.TagSearch;
import com.simicart.core.catalog.search.fragment.ListProductFragment;
import com.simicart.core.catalog.search.model.ConstantsSearch;
import com.simicart.core.common.DrawableManager;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.theme.matrixtheme.home.entity.OrderProduct;

public class CategoryCustomAdapter extends BaseAdapter {

	private ArrayList<OrderProduct> mOrderProducts;
	private Context mContext;
	private LayoutInflater mInflater;

	public CategoryCustomAdapter(Context context,
			ArrayList<OrderProduct> products) {
		mContext = context;
		mOrderProducts = products;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mOrderProducts.size();
		// return 1;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parentView) {
		convertView = mInflater.inflate(
				Rconfig.getInstance().layout(
						"theme1_layout_item_category_bottom"), null);
		final OrderProduct product = mOrderProducts.get(pos);
		RelativeLayout rlt_animate = (RelativeLayout) convertView
				.findViewById(Rconfig.getInstance().id("rlt_animate"));

		if (DataLocal.isTablet) {
			LayoutParams params_rlt = new LayoutParams(500,
					LayoutParams.MATCH_PARENT);
			if (pos == 0) {
				params_rlt.setMargins(0, 0, 6, 0);
			} else if (pos == mOrderProducts.size() - 1) {
				params_rlt.setMargins(0, 0, 0, 0);
			} else {
				params_rlt.setMargins(0, 0, 6, 0);
			}
			rlt_animate.setLayoutParams(params_rlt);
		}

		LinearLayout ll_title = new LinearLayout(rlt_animate.getContext());
		RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params2.setMargins(0, 0, 0, 30);
		params2.addRule(RelativeLayout.CENTER_IN_PARENT);
		ll_title.setLayoutParams(params2);
		if (DataLocal.isTablet) {
			ll_title.setPadding(Utils.getValueDp(10), Utils.getValueDp(3),
					Utils.getValueDp(10), Utils.getValueDp(3));
		} else {
			ll_title.setPadding(Utils.getValueDp(5), Utils.getValueDp(1),
					Utils.getValueDp(5), Utils.getValueDp(1));
		}
		ll_title.setBackgroundColor(Color.parseColor("#88000000"));
		ll_title.setOrientation(LinearLayout.VERTICAL);

		TextView tv_title = new TextView(ll_title.getContext());

		LayoutParams params3 = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		tv_title.setLayoutParams(params3);
		tv_title.setGravity(Gravity.CENTER);
		String name = product.getSpotName();
		if (null != name) {
			tv_title.setText(Config.getInstance().getText(
					Utils.capitalizes(name)));
			if (DataLocal.isTablet) {
				tv_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
			} else {
				tv_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
			}
			tv_title.setTextColor(Color.parseColor("#ffffff"));
		}

		ViewFlipper viewflipper = new ViewFlipper(rlt_animate.getContext());
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		viewflipper.setLayoutParams(params);
		if (product.getSpotId().equals("fake")) {
			ImageView imageview = new ImageView(viewflipper.getContext());
			imageview.setScaleType(ScaleType.CENTER);
			if (DataLocal.isTablet) {
				imageview.setImageResource(Rconfig.getInstance().drawable(
						"theme1_fake_spot_tablet"));
			} else {
				imageview.setImageResource(Rconfig.getInstance().drawable(
						"theme1_fake_spot"));
			}
			viewflipper.addView(imageview);
		} else {
			ArrayList<String> urlImages = product.getUrlImage();

			if (null != urlImages && urlImages.size() > 0) {
				if (urlImages.size() == 1) {
					String url = urlImages.get(0);
					ImageView imageview = new ImageView(
							viewflipper.getContext());
					imageview.setScaleType(ScaleType.FIT_XY);
					DrawableManager.fetchItemDrawableOnThread(url, imageview);
					viewflipper.addView(imageview);
				} else {
					viewflipper.setInAnimation(mContext, Rconfig.getInstance()
							.getId("in_from_down", "anim"));
					viewflipper.setOutAnimation(mContext, Rconfig.getInstance()
							.getId("out_to_up", "anim"));
					viewflipper.setFlipInterval(5000 + (pos * 150));
					viewflipper.startFlipping();
					for (int i = 0; i < urlImages.size(); i++) {
						String url = urlImages.get(i);
						ImageView imageview = new ImageView(
								viewflipper.getContext());
						imageview.setScaleType(ScaleType.FIT_XY);
						DrawableManager.fetchItemDrawableOnThread(url,
								imageview);
						viewflipper.addView(imageview);
					}
				}
			}
		}

		rlt_animate.addView(viewflipper);
		ll_title.addView(tv_title);
		rlt_animate.addView(ll_title);
		rlt_animate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				viewOrderProduct(product);
			}
		});

		return convertView;
	}

	protected void viewOrderProduct(OrderProduct orderProduct) {
		if (orderProduct.getSpotKey() != null) {
			ListProductFragment fragment = ListProductFragment.newInstance();
			fragment.setKey(orderProduct.getSpotKey());
			fragment.setUrlSearch(ConstantsSearch.url_spot_matrixtheme);
			if (DataLocal.isTablet) {
				fragment.setTag_search(TagSearch.TAG_GRIDVIEW);
			}
			SimiManager.getIntance().addFragment(fragment);
		}
	}
}
