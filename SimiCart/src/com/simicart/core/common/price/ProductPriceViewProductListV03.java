package com.simicart.core.common.price;

import android.graphics.Paint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.simicart.core.catalog.product.entity.PriceV2;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;

public class ProductPriceViewProductListV03 extends ProductPriceView {

	public ProductPriceViewProductListV03(Product product) {
		super(product);
	}

	/*
	 * 
	 * This function display price of product that type of product is simple,
	 * configurable or virtual. The desired result is : regular price, special
	 * price and minimal price. The role of this function : Step 1 : Display
	 * regular price : if product have one tax price, regular price is
	 * product_regular_price. Else if product have two tax price, regular price
	 * is incl_tax. Step 2 : Display special price : if product have one tax
	 * price, special price is product_price. Else if product have two tax
	 * price, special price is incl_tax_special. if special price is not empty,
	 * regular price will show by using strike tag. Step 3 : Display minimal
	 * price : mimimal_price_label : minimal_price
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

		String regular_price = "";
		String special_price = "";
		String minimal_price = "";

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

		// step 3 : display minimal price
		String mimimal_price_label = priceV2.getMinimalPriceLabel();
		float minimalPrice = priceV2.getMinimalPrice();
		if (minimalPrice > -1 && Utils.validateString(mimimal_price_label)) {
			minimal_price = getHtmlForPrice(minimalPrice, mimimal_price_label);
			tv_minimal_price.setText(Html.fromHtml(minimal_price));
		} else {
			tv_minimal_price.setVisibility(View.GONE);
		}

		if (Utils.validateString(special_price)) {
			tv_regular_price.setPaintFlags(tv_regular_price.getPaintFlags()
					| Paint.STRIKE_THRU_TEXT_FLAG);
			tv_special_price.setText(Html.fromHtml(special_price));
		} else {
			tv_special_price.setVisibility(View.GONE);
		}

		if (Utils.validateString(regular_price)) {
			tv_regular_price.setText(Html.fromHtml(regular_price));
		}

		return viewPrice;
	}

	/*
	 * 
	 * This function show price of product that type of product is bundle. The
	 * role of function is : If JSON of product have 'minimal_price_label' tag,
	 * if product has one tax(excl_tax_minimal), price will be
	 * minimal_price_label : excl_tax_minimal, else if product has two tax,
	 * price will be mimimal_price_labe : incl_tax_minimal. if JSON of product
	 * doesn't have 'minimal_price_label' tag, if product has incl_tax, price
	 * will be : incl_tax_from incl_tax_to, else if product doesn't has
	 * incl_tax, price will be excl_tax_from excl_tax_to.
	 */

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

		PriceV2 priceV2 = mProduct.getPriceV2();

		String minimal_label = priceV2.getMinimalPriceLabel();
		if (Utils.validateString(minimal_label)) {
			// JSON of product has 'minimal_price_label' tag
			tv_special_price.setVisibility(View.GONE);
			tv_minimal_price.setVisibility(View.GONE);
			float excl_tax_minimal = priceV2.getExclTaxMinimal();
			float incl_tax_minimal = priceV2.getInclTaxMinimal();
			if (incl_tax_minimal > -1) {
				tv_regular_price.setText(Html.fromHtml(getHtmlForPrice(
						incl_tax_minimal, minimal_label)));
			} else {
				tv_regular_price.setText(Html.fromHtml(getHtmlForPrice(
						excl_tax_minimal, minimal_label)));
			}

		} else {

			tv_special_price.setVisibility(View.GONE);
			// JSON of product doesn't has 'minimal_price_label' tag
			float incl_tax_from = priceV2.getInclTaxFrom();
			float incl_tax_to = priceV2.getInclTaxTo();
			float excl_tax_from = priceV2.getExclTaxFrom();
			float excl_tax_to = priceV2.getExclTaxTo();
			String from_text = Config.getInstance().getText("From");
			String to_text = Config.getInstance().getText("To");
			if (incl_tax_from > -1) {
				tv_regular_price.setText(Html.fromHtml(getHtmlForPrice(
						incl_tax_from, from_text)));
				tv_minimal_price.setText(Html.fromHtml(getHtmlForPrice(
						incl_tax_to, to_text)));
			} else {
				if (excl_tax_from > -1) {
					tv_regular_price.setText(Html.fromHtml(getHtmlForPrice(
							excl_tax_from, from_text)));
					tv_minimal_price.setText(Html.fromHtml(getHtmlForPrice(
							excl_tax_to, to_text)));
				}
			}
		}

		return viewPrice;
	}

	/*
	 * 
	 * This function show price of product that type of product is group. The
	 * role of function is : if product has incl_tax_minimal and
	 * excl_tax_minimal, price will be minimal_price_label : incl_tax_minimal.
	 * Else if product has one tax, price will be minimal_price_label :
	 * minimal_price.
	 */

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
		tv_special_price.setVisibility(View.GONE);
		tv_minimal_price.setVisibility(View.GONE);

		PriceV2 priceV2 = mProduct.getPriceV2();

		float incl_tax_minimal = priceV2.getInclTaxMinimal();
		float minimal_price = priceV2.getMinimalPrice();
		String minimal_price_label = priceV2.getMinimalPriceLabel();
		if (incl_tax_minimal > -1) {
			tv_regular_price.setText(Html.fromHtml(getHtmlForPrice(
					incl_tax_minimal, minimal_price_label)));
		} else {
			tv_regular_price.setText(Html.fromHtml(getHtmlForPrice(
					minimal_price, minimal_price_label)));
		}

		return super.getViewPriceType3();
	}

	protected String getHtmlForPrice(float price) {
		return "<font color='" + color_Price + "'>"
				+ Config.getInstance().getPrice("" + price) + "</font>";
	}

	protected String getHtmlForPrice(float price, String label) {
		return "<font color='" + color_Price + "'>" + label
				+ ": </font><font color='" + color_Price + "'>"
				+ Config.getInstance().getPrice("" + price) + "</font>";
	}

	protected String getHtmlForSpecialPrice(float price) {
		return "<font color='" + Config.getInstance().getSpecial_price_color()
				+ "'>" + Config.getInstance().getPrice("" + price) + "</font>";
	}

}
