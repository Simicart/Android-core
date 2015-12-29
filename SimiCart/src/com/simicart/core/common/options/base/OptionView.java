package com.simicart.core.common.options.base;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;

import com.simicart.core.catalog.product.entity.ProductOption;
import com.simicart.core.common.Utils;
import com.simicart.core.common.options.delegate.CacheOptionSingleDelegate;
import com.simicart.core.config.Config;

public class OptionView {
	protected ProductOption mOptions;
	protected View mView;
	protected Context mContext;
	protected CacheOptionSingleDelegate mDelegate;

	public ProductOption getOptions() {
		return mOptions;
	}

	public void setOptions(ProductOption mOptions) {
		this.mOptions = mOptions;
	}

	public boolean isCheckedOption() {
		return this.mOptions.isChecked();
	}

	public OptionView(ProductOption option, Context context,
			CacheOptionSingleDelegate delegate) {
		mDelegate = delegate;
		mContext = context;
		mOptions = option;
	}

	public View createView() {
		return null;
	}

	public boolean checkSaved() {
		if (mOptions.isChecked()) {
			selectOption(true);
			return true;
		}
		return false;
	}

	public boolean checkDefault() {
		if (mOptions.getIsDefault().equals("1")) {
			mOptions.setIsDefault("0");
			selectOption(true);
			return true;
		}
		return false;
	}

	public void selectOption(boolean isSelected) {
		if (isSelected) {
			updateView(true);
			mDelegate.updateStateCacheOption(mOptions.getOptionId(), true);
			if (!Config.getInstance().isShow_zero_price()
					&& mOptions.getOptionPrice() == 0) {
				mDelegate.updatePriceForHeader("");
			} else {
				mDelegate.updatePriceForHeader(Config.getInstance().getPrice(
						"" + getPrice()));
			}

			mOptions.setOptionCart(String.valueOf(mOptions.getOptionId()));
			mOptions.setChecked(true);
			mDelegate.updatePriceParent(mOptions, CacheOptionView.ADD_OPERATOR);

			if (mOptions.getDependence_options() != null) {
				ArrayList<String> dependList = mOptions
						.getCurrent_list_dependence_option_id();
				if (dependList == null || dependList.size() == 0) {
					dependList = new ArrayList<>();
					for (String dependenceID : mOptions.getDependence_options()
							.keySet()) {
						dependList.add(dependenceID);
					}
					mDelegate.clearCheckAll(mOptions.getOptionId());
					mDelegate.onSendDependOption(dependList, "");
				} else {
					mDelegate.onSendDependOption(dependList,
							mOptions.getOptionId());
				}
			}
		} else {
			updateView(false);
			if (null != mOptions) {
				mOptions.setOptionCart("-1");
				mOptions.setChecked(false);
				mDelegate.updatePriceParent(mOptions,
						CacheOptionView.SUB_OPERATOR);
				mDelegate.updateStateCacheOption(mOptions.getOptionId(), false);
				mDelegate.updatePriceForHeader("");
			}
		}
	}

	public void setBackgroundColor(int color) {

	}

	public void updateView(boolean isSelected) {

	}

	public String getPrice() {
		float price = mOptions.getOptionPrice();

		String qty = mOptions.getOptionQty();
		if (Utils.validateString(qty)) {

			float f_qty = Float.parseFloat(qty);

			if (f_qty > 1) {
				price = f_qty * price;
			}
		}

		return String.valueOf(price);
	}

	public String getTitleOption() {
		int i_qty = 0;
		String title = "<font color='#7F7F7F'>" + mOptions.getOptionValue()
				+ "</font>";

		String qty = mOptions.getOptionQty();

		if ((null != qty) && (!qty.equals("") && (!qty.equals("null")))) {
			i_qty = (int) Float.parseFloat(qty);
			if (i_qty > 1) {
				title = "<font color='#7F7F7F'>" + i_qty + " x "
						+ mOptions.getOptionValue()

						+ "</font>";
			}
		}

		if (mOptions.getOptionType().equals("grouped")) {
			title = mOptions.getOptionValue();
		}

		return title;
	}

	public String getPriceOption() {
		if (mOptions.getOptionPrice() == 0
				&& !Config.getInstance().isShow_zero_price()) {
			return "";
		}
		float f_price = mOptions.getOptionPrice();
		String price = "<font color='#7F7F7F'> +"
				+ Config.getInstance().getPrice("" + f_price) + "</font>";

		if (mOptions.getOption_price_incl_tax() != -1) {
			String price_tax = "<font color='#7F7F7F'> (</font><font color='"
					+ Config.getInstance().getPrice_color()
					+ "'>+"
					+ Config.getInstance().getPrice(
							"" + mOptions.getOption_price_incl_tax())
					+ "</font> <font color='#7F7F7F'>"
					+ Config.getInstance().getText("Incl. Tax") + ")</font>";

			if (!Config.getInstance().isShow_zero_price()
					&& mOptions.getOption_price_incl_tax() == 0) {
				return "";
			} else {
				return price_tax;
			}

		}
		return price;
	}

	public String showValueOption() {
		String content;
		int i_qty = 0;
		String title = "<font color='grey'>" + mOptions.getOptionValue()
				+ "</font>";

		String qty = mOptions.getOptionQty();

		if ((null != qty) && (!qty.equals("") && (!qty.equals("null")))) {
			i_qty = (int) Float.parseFloat(qty);
			if (i_qty > 1) {
				title = "<font color='grey'>" + i_qty + " x "
						+ mOptions.getOptionValue()

						+ "</font>";
			}
		}

		if (mOptions.getOptionType().equals("grouped")) {
			title = mOptions.getOptionValue();
		}

		float f_price = mOptions.getOptionPrice();

		String price = "<font color='" + Config.getInstance().getPrice_color()
				+ "'> +" + Config.getInstance().getPrice("" + f_price)
				+ "</font>";

		content = title + price;
		if (mOptions.getOptionPrice() == 0
				&& !Config.getInstance().isShow_zero_price()) {
			content = title;
		}
		if (mOptions.getOption_price_incl_tax() != -1) {
			String price_tax = "<font color='grey'> (</font><font color='"
					+ Config.getInstance().getPrice_color()
					+ "'>+"
					+ Config.getInstance().getPrice(
							"" + mOptions.getOption_price_incl_tax())
					+ "</font> <font color='grey'>"
					+ Config.getInstance().getText("Incl. Tax") + ")</font>";
			if (!Config.getInstance().isShow_zero_price()
					&& mOptions.getOption_price_incl_tax() == 0) {
			} else {
				content = content + price_tax;
			}

		}
		return content;
	}

}
