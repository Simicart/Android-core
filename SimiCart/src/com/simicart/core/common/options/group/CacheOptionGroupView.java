package com.simicart.core.common.options.group;

import android.text.Html;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simicart.core.catalog.product.entity.CacheOption;
import com.simicart.core.catalog.product.entity.ProductOption;
import com.simicart.core.common.ViewIdGenerator;
import com.simicart.core.common.options.base.CacheOptionView;
import com.simicart.core.common.price.ProductPriceView;
import com.simicart.core.common.price.ProductPriceViewDetail;
import com.simicart.core.config.Rconfig;

public class CacheOptionGroupView extends CacheOptionView {

	protected float mScale;

	public CacheOptionGroupView(CacheOption cacheOption) {
		super(cacheOption);
		mScale = mContext.getResources().getDisplayMetrics().density;
	}

	@Override
	protected void createCacheOption() {

		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		// add value
		int scale0 = (int) (mScale * 0 + 0.5f);
		int scale10 = (int) (mScale * 10 + 0.5f);
		int scale5 = (int) (mScale * 5 + 0.5f);
		int scale80 = (int) (mScale * 80 + 0.5f);
		int scale30 = (int) (mScale * 30 + 0.5f);
		int scale20 = (int) (mScale * 20 + 0.5f);

		for (final ProductOption option : mCacheOption.getAllOption()) {
			final RelativeLayout rl_value = new RelativeLayout(mContext);
			rl_value.setLayoutParams(param);
			rl_value.setBackgroundResource(Rconfig.getInstance().drawable(
					"bottom_line_border"));

			rl_value.setPadding(scale10, scale10, scale5, scale10);
			ll_body.addView(rl_value);

			// textView value
			if (option.isShow_price_v2() || (option.getShowPriceV2() != null)) {
				LinearLayout ll_layout = new LinearLayout(mContext);
				LinearLayout.LayoutParams ll_lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				ll_layout.setOrientation(LinearLayout.VERTICAL);
				ll_layout.setLayoutParams(ll_lp);
				rl_value.addView(ll_layout);

				TextView tv_value = new TextView(mContext);
				String value = option.getOptionValue();
				tv_value.setText(Html.fromHtml(value),
						TextView.BufferType.SPANNABLE);
				RelativeLayout.LayoutParams params_value = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				params_value.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				params_value.addRule(RelativeLayout.CENTER_VERTICAL);
				tv_value.setLayoutParams(params_value);
				tv_value.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
				tv_value.setGravity(RelativeLayout.CENTER_VERTICAL);
				tv_value.setPadding(scale0, scale0, scale80, scale0);
				ll_layout.addView(tv_value);

				ProductPriceViewDetail viewPrice = new ProductPriceViewDetail(
						option.getShowPriceV2());
				viewPrice.setTypeProduct(ProductPriceView.PRODUCT_PRICE_TYPE_1);
				View view = viewPrice.getViewPrice();
				if (null != view) {
					ll_layout.addView(view);
				}

			} else {
				TextView tv_value = new TextView(mContext);
				tv_value.setText(Html.fromHtml(showValueOption(option)),
						TextView.BufferType.SPANNABLE);
				RelativeLayout.LayoutParams tvValue_lp = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				tvValue_lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				tvValue_lp.addRule(RelativeLayout.CENTER_VERTICAL);
				tv_value.setLayoutParams(tvValue_lp);
				tv_value.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
				tv_value.setGravity(RelativeLayout.CENTER_VERTICAL);
				tv_value.setPadding(scale0, scale0, scale80, scale0);
				rl_value.addView(tv_value);
			}

			// image add
			ImageView im_add = new ImageView(mContext);
			im_add.setId(ViewIdGenerator.generateViewId());
			RelativeLayout.LayoutParams imAdd_lp = new RelativeLayout.LayoutParams(
					scale30, scale30);
			imAdd_lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			imAdd_lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			im_add.setLayoutParams(imAdd_lp);
			im_add.setBackgroundResource(Rconfig.getInstance().drawable("add"));
			rl_value.addView(im_add);
			im_add.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					option.setOptionCart(option.getOptionId());
					mCacheOption.setNumber(mCacheOption.getNumber() + 1);
					
					updatePriceHeader("x" + mCacheOption.getNumber());
					updatePriceForParent(option, ADD_OPERATOR);
				}
			});

			// image sub
			ImageView im_sub = new ImageView(mContext);
			RelativeLayout.LayoutParams imSub_lp = new RelativeLayout.LayoutParams(
					scale30, scale30);
			imSub_lp.addRule(RelativeLayout.LEFT_OF, im_add.getId());
			imSub_lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			imSub_lp.setMargins(0, 0, scale20, 0);
			im_sub.setLayoutParams(imSub_lp);
			im_sub.setBackgroundResource(Rconfig.getInstance().drawable("sub"));
			if(mCacheOption.getNumber() > 0){
				updatePriceHeader("x" + mCacheOption.getNumber());
			}
			rl_value.addView(im_sub);
			im_sub.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mCacheOption.getNumber() > 0) {
						mCacheOption.setNumber(mCacheOption.getNumber() - 1);
						updatePriceHeader("x" + mCacheOption.getNumber());
						updatePriceForParent(option, SUB_OPERATOR);
					}
					if (mCacheOption.getNumber() == 0) {
						tv_required.setText("");
					}
				}
			});
		}

	}

}
