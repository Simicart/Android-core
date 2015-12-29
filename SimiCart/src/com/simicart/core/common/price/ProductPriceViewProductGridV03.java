package com.simicart.core.common.price;

import android.graphics.Color;
import android.graphics.Paint;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.simicart.core.catalog.product.entity.PriceV2;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;

public class ProductPriceViewProductGridV03 extends ProductPriceView {

	boolean isShowOnePrice = false;

	public ProductPriceViewProductGridV03(Product product) {
		super(product);
	}

	public void setShowOnePrice(boolean showOne) {
		isShowOnePrice = showOne;
	}

	/*
	 * 
	 * This function display price of product that type of product is simple,
	 * configurable or virtual. The desired result is : regular price, special
	 * price. The role of this function : Step 1 : Display regular price : if
	 * product have one tax price, regular price is product_regular_price. Else
	 * if product have two tax price, regular price is incl_tax. Step 2 :
	 * Display special price : if product have one tax price, special price is
	 * product_price. Else if product have two tax price, special price is
	 * incl_tax_special. if special price is not empty, regular price will show
	 * by using strike tag.
	 */

	@Override
	public View getViewPriceType1() {

		LayoutInflater inflater = LayoutInflater.from(mContext);
		View viewPrice = inflater.inflate(
				Rconfig.getInstance().layout("core_item_price_product_list"),
				null, false);
		TextView tv_regular_price = (TextView) viewPrice.findViewById(Rconfig
				.getInstance().id("tv_regular"));
		TextView tv_special_price = (TextView) viewPrice.findViewById(Rconfig
				.getInstance().id("tv_special"));
		TextView tv_minimal_price = (TextView) viewPrice.findViewById(Rconfig
				.getInstance().id("tv_minimal"));
		tv_special_price.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
		tv_special_price.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
		tv_minimal_price.setVisibility(View.GONE);

		String regular_price = "";
		String special_price = "";

		PriceV2 priceV2 = mProduct.getPriceV2();

		// step 1 : display regular price
		float excl_tax = priceV2.getExclTax();
		float incl_tax = priceV2.getInclTax();
		float product_regualar_price = priceV2.getRegularPrice();
		if (excl_tax > -1 && incl_tax > -1) {
			regular_price = getHtmlForPrice(incl_tax);
		} else {
			if (product_regualar_price > -1) {
				regular_price = getHtmlForPrice(product_regualar_price);
			}
		}

		// step 2 : display special price
		float excl_tax_special = priceV2.getExclTaxSpecial();
		float incl_tax_special = priceV2.getInclTaxSpecial();
		float price = priceV2.getPrice();
		if (excl_tax_special > -1 && incl_tax_special > -1) {
			special_price = getHtmlForSpecialPrice(incl_tax_special);
		} else {
			if (price > -1) {
				special_price = getHtmlForSpecialPrice(price);
			}
		}

		// regular_price = "1230";
		if (Utils.validateString(regular_price)) {
			if (isShowOnePrice) {
				LayoutParams params = new LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				tv_regular_price.setLayoutParams(params);
				tv_regular_price.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
			}
			tv_regular_price.setTextColor(Color.parseColor(color_Price));
			tv_regular_price.setText(Html.fromHtml(regular_price));
		}

		if (Utils.validateString(special_price)) {

			if (isShowOnePrice) {
				tv_regular_price.setVisibility(View.GONE);
				LayoutParams params = new LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				tv_special_price.setLayoutParams(params);
				tv_special_price.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
			} else {
				tv_regular_price.setPaintFlags(tv_regular_price.getPaintFlags()
						| Paint.STRIKE_THRU_TEXT_FLAG);
			}
			tv_special_price.setText(Html.fromHtml(special_price));
		} else {
			tv_special_price.setVisibility(View.GONE);
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			tv_regular_price.setLayoutParams(params);
		}

		return viewPrice;
	}

	@Override
	public View getViewPriceType2() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View viewPrice = inflater.inflate(
				Rconfig.getInstance().layout("core_item_price_product_list"),
				null, false);
		TextView tv_regular_price = (TextView) viewPrice.findViewById(Rconfig
				.getInstance().id("tv_regular"));
		TextView tv_special_price = (TextView) viewPrice.findViewById(Rconfig
				.getInstance().id("tv_special"));
		TextView tv_minimal_price = (TextView) viewPrice.findViewById(Rconfig
				.getInstance().id("tv_minimal"));
		tv_special_price.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
		tv_special_price.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);

		tv_special_price.setVisibility(View.GONE);
		tv_minimal_price.setVisibility(View.GONE);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		tv_regular_price.setLayoutParams(params);
		PriceV2 priceV2 = mProduct.getPriceV2();

		String minimal_price_label = priceV2.getMinimalPriceLabel();
		if (Utils.validateString(minimal_price_label)) {
			float incl_tax_minimal = priceV2.getInclTaxMinimal();
			float excl_tax_minimal = priceV2.getExclTaxMinimal();
			if (incl_tax_minimal > -1) {
				tv_regular_price.setText(Html
						.fromHtml(getHtmlForPrice(incl_tax_minimal)));
			} else {
				tv_regular_price.setText(Html
						.fromHtml(getHtmlForPrice(excl_tax_minimal)));
			}

		} else {
			float incl_tax_to = priceV2.getInclTaxTo();
			float excl_tax_to = priceV2.getExclTaxTo();
			if (incl_tax_to > -1) {
				tv_regular_price.setText(Html
						.fromHtml(getHtmlForPrice(incl_tax_to)));
			} else {
				tv_regular_price.setText(Html
						.fromHtml(getHtmlForPrice(excl_tax_to)));
			}

		}

		return viewPrice;

	}

	@Override
	public View getViewPriceType3() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View viewPrice = inflater.inflate(
				Rconfig.getInstance().layout("core_item_price_product_list"),
				null, false);
		TextView tv_regular_price = (TextView) viewPrice.findViewById(Rconfig
				.getInstance().id("tv_regular"));
		TextView tv_special_price = (TextView) viewPrice.findViewById(Rconfig
				.getInstance().id("tv_special"));
		TextView tv_minimal_price = (TextView) viewPrice.findViewById(Rconfig
				.getInstance().id("tv_minimal"));
		tv_special_price.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
		tv_special_price.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
		tv_special_price.setVisibility(View.GONE);
		tv_minimal_price.setVisibility(View.GONE);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		tv_regular_price.setLayoutParams(params);
		PriceV2 priceV2 = mProduct.getPriceV2();
		float incl_tax_minimal = priceV2.getInclTaxMinimal();
		float mnimal_price = priceV2.getMinimalPrice();
		if (incl_tax_minimal > -1) {
			tv_regular_price.setText(Html
					.fromHtml(getHtmlForPrice(incl_tax_minimal)));

		} else {
			tv_regular_price.setText(Html
					.fromHtml(getHtmlForPrice(mnimal_price)));
		}

		return viewPrice;
	}

	protected String getHtmlForPrice(float price) {
		return "<font color='" + color_Price + "'>"
				+ Config.getInstance().getPrice("" + price) + "</font>";
	}

	protected String getHtmlForSpecialPrice(float price) {
		return "<font color='" + color_Price_Special + "'>"
				+ Config.getInstance().getPrice("" + price) + "</font>";
	}

	protected String getHtmlForPrice(float price, String label) {
		return "<font color='" + color_Price + "'>" + label
				+ ": </font><font color='" + color_Price + "'>"
				+ Config.getInstance().getPrice("" + price) + "</font>";
	}

}
