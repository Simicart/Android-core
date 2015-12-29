package com.simicart.core.common.options.single;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simicart.core.catalog.product.entity.ProductOption;
import com.simicart.core.common.Utils;
import com.simicart.core.common.options.base.OptionView;
import com.simicart.core.common.options.delegate.CacheOptionSingleDelegate;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;

public class OptionSingle extends OptionView {

	protected boolean isNone = false;
	protected TextView tv_title;
	protected LinearLayout ll_option;
	protected TextView tv_price;
	protected ImageView img_icon;
	protected boolean isChecked = true;
	protected boolean isDependent = false;

	public void setTypeNone(boolean none) {
		isNone = none;
	}

	public boolean getTypeNone() {
		return isNone;
	}

	public OptionSingle(ProductOption option, Context context,
			CacheOptionSingleDelegate delegate) {
		super(option, context, delegate);
	}

	@Override
	public View createView() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		mView = inflater.inflate(
				Rconfig.getInstance().layout("core_cache_option_item"), null,
				false);

		ll_option = (LinearLayout) mView.findViewById(Rconfig.getInstance().id(
				"ll_option"));

		tv_title = (TextView) mView.findViewById(Rconfig.getInstance().id(
				"tv_title"));
		tv_price = (TextView) mView.findViewById(Rconfig.getInstance().id(
				"tv_price"));
		img_icon = (ImageView) mView.findViewById(Rconfig.getInstance().id(
				"img_icon_option"));
		img_icon.setBackgroundResource(Rconfig.getInstance().drawable(
				"core_icon_option_single"));

		if (mOptions != null && mOptions.getDependence_options() != null) {
			isDependent = true;
			if (DataLocal.isCloud) {
				tv_price.setVisibility(View.GONE);
			} else {
				tv_price.setVisibility(View.VISIBLE);
			}
		}

		if (isNone) {
			String none = "<font color='grey'>"
					+ Config.getInstance().getText("None") + "</font>";
			tv_title.setText(Html.fromHtml(none));
			tv_price.setVisibility(View.INVISIBLE);
		} else {
			tv_title.setText(Html.fromHtml(getTitleOption()),
					TextView.BufferType.SPANNABLE);

			String price = getPriceOption();
			if (Utils.validateString(price)) {
				tv_price.setText(Html.fromHtml(price),
						TextView.BufferType.SPANNABLE);
			} else {
				tv_price.setVisibility(View.INVISIBLE);
			}

		}

		if (isNone) {

			ll_option.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (isChecked) {
						isChecked = false;
						mDelegate.clearCheckAll("none");
						mDelegate.updatePriceForHeader("");
						updateView(true);
					} else {
						isChecked = true;
						updateView(false);
					}
				}
			});

		} else {
			if (!checkSaved()) {
				checkDefault();
			}
			ll_option.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (isChecked) {
						isChecked = false;
						mDelegate.clearCheckAll(mOptions.getOptionId());
						mDelegate.updatePriceForHeader("");
						selectOption(true);
					} else {
						isChecked = true;
						selectOption(false);
					}

				}
			});
		}

		return mView;
	}

	@Override
	public void setBackgroundColor(int color) {
		ll_option.setBackgroundColor(color);
	}

	@Override
	public void updateView(boolean isSelected) {
		if (isSelected) {
			isChecked = false;
			img_icon.setBackgroundResource(Rconfig.getInstance().drawable(
					"core_icon_option_selected"));
		} else {
			isChecked = true;
			if (null != mOptions) {
				mOptions.setChecked(false);
			}
			img_icon.setBackgroundResource(Rconfig.getInstance().drawable(
					"core_icon_option_single"));
		}
	}

}
