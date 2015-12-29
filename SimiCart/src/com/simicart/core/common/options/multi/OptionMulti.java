package com.simicart.core.common.options.multi;

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
import com.simicart.core.common.options.base.CacheOptionView;
import com.simicart.core.common.options.base.OptionView;
import com.simicart.core.common.options.delegate.CacheOptionMultiDelegate;
import com.simicart.core.common.options.delegate.CacheOptionSingleDelegate;
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;

public class OptionMulti extends OptionView {

	protected boolean isSave = false;
	protected TextView tv_title;
	protected ImageView img_icon;
	protected LinearLayout ll_option;
	protected boolean isChecked = true;
	protected TextView tv_price;

	public OptionMulti(ProductOption option, Context context,
			CacheOptionSingleDelegate delegate) {
		super(option, context, delegate);
		// TODO Auto-generated constructor stub
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
				"core_icon_option_multi"));

		tv_title.setText(Html.fromHtml(getTitleOption()),
				TextView.BufferType.SPANNABLE);

		String price = getPriceOption();
		if (Utils.validateString(price)) {
			tv_price.setText(Html.fromHtml(price),
					TextView.BufferType.SPANNABLE);
		} else {
			tv_price.setVisibility(View.INVISIBLE);
		}

		if (!checkSaved()) {
			checkDefault();
		}
		ll_option.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isChecked) {
					isChecked = false;
					// mDelegate.clearCheckAll(mOptions.getOptionId());
					selectOption(true);

				} else {
					isChecked = true;
					selectOption(false);
				}

			}
		});

		return mView;
	}

	@Override
	public boolean checkSaved() {
		if (mOptions.isChecked()) {
			isSave = true;
			selectOption(true);
			isSave = false;
			return true;
		}
		return false;
	}

	@Override
	public boolean checkDefault() {
		if (mOptions.getIsDefault().equals("1")) {
			mOptions.setIsDefault("0");

			selectOption(true);
			return true;
		}
		return false;
	}

	@Override
	public void selectOption(boolean isSelected) {

		float optionPrice = mOptions.getOptionPrice();
		int i_qty = 0;
		String optionQty = mOptions.getOptionQty();
		if (null != optionQty && !optionQty.equals("")
				&& !optionQty.equals("null")) {
			i_qty = (int) Float.parseFloat(optionQty);
		}
		if (i_qty > 1) {
			optionPrice = optionPrice * i_qty;
		}

		if (isSelected) {
			updateView(true);
			mOptions.setChecked(true);
			mDelegate.updateStateCacheOption(mOptions.getOptionId(), true);
			if (!isSave) {
				((CacheOptionMultiDelegate) mDelegate).updatePriceMulti(
						optionPrice, CacheOptionView.ADD_OPERATOR);
			}

			mOptions.setOptionCart(mOptions.getOptionId());
			if (!Config.getInstance().isShow_zero_price()
					&& ((CacheOptionMultiDelegate) mDelegate).getPriceMulti() == 0) {
				mDelegate.updatePriceForHeader("");
			} else {
				mDelegate
						.updatePriceForHeader(""
								+ Config.getInstance()
										.getPrice(
												""
														+ ((CacheOptionMultiDelegate) mDelegate)
																.getPriceMulti()));
			}

			mDelegate.updatePriceParent(mOptions, CacheOptionView.ADD_OPERATOR);

		} else {
			updateView(false);
			((CacheOptionMultiDelegate) mDelegate).updatePriceMulti(
					optionPrice, CacheOptionView.SUB_OPERATOR);
			mOptions.setOptionCart("-1");
			mDelegate.updateStateCacheOption(mOptions.getOptionId(), false);

			if (((CacheOptionMultiDelegate) mDelegate).getPriceMulti() == 0
					&& !((CacheOptionMultiDelegate) mDelegate).isCheckedAll()) {
				if (((CacheOptionMultiDelegate) mDelegate).isRequired()) {
					mDelegate.updatePriceForHeader(Config.getInstance()
							.getText("*"));
				} else {
					mDelegate.updatePriceForHeader("");
				}
			} else {
				mDelegate
						.updatePriceForHeader(""
								+ Config.getInstance()
										.getPrice(
												""
														+ ((CacheOptionMultiDelegate) mDelegate)
																.getPriceMulti()));
				if (!Config.getInstance().isShow_zero_price()
						&& ((CacheOptionMultiDelegate) mDelegate)
								.getPriceMulti() == 0) {
					mDelegate.updatePriceForHeader("");
				}
			}
			mOptions.setChecked(false);
			mDelegate.updatePriceParent(mOptions, CacheOptionView.SUB_OPERATOR);
		}

	}

	@Override
	public void setBackgroundColor(int color) {
		ll_option.setBackgroundColor(color);
	}

	@Override
	public void updateView(boolean isSelected) {
		if (isSelected) {
			img_icon.setBackgroundResource(Rconfig.getInstance().drawable(
					"core_icon_option_selected"));
		} else {
			img_icon.setBackgroundResource(Rconfig.getInstance().drawable(
					"core_icon_option_multi"));
		}
	}

}
