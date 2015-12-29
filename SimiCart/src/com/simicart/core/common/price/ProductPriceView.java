package com.simicart.core.common.price;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;

@SuppressLint("DefaultLocale")
public class ProductPriceView {

	public static String PRODUCT_PRICE_TYPE_1 = "simple_virtual";
	public static String PRODUCT_PRICE_TYPE_2 = "bundle";
	public static String PRODUCT_PRICE_TYPE_3 = "grouped";
	public static String PRODUCT_PRICE_TYPE_4 = "configurable";
	protected Product mProduct;
	protected String mTypeProduct;
	protected Context mContext;
	protected ProductPriceUtils mPriceUtils;
	protected String color_Label = Config.getInstance()
			.getContent_color_string();
	protected String color_Tire = "#528F94";
	protected String color_Price = Config.getInstance().getPrice_color();
	protected String color_Price_Special = Config.getInstance()
			.getSpecial_price_color();
	protected String color_Other = "#000000";
	protected View mViewPrice;

	public ProductPriceView() {

	}

	public ProductPriceView(Product product) {
		mProduct = product;
		if (mProduct != null) {
			mTypeProduct = mProduct.getType();
		}
		if (null != mTypeProduct) {
			mTypeProduct = mTypeProduct.toLowerCase();
		}
		mContext = SimiManager.getIntance().getCurrentContext();
		mPriceUtils = new ProductPriceUtils();
		mPriceUtils.setProduct(mProduct);

	}

	public View getViewPriceHome() {
		float price = mProduct.getPrice();
		if (price > -1) {
			String content = "<font color='" + color_Price + "'>"
					+ Config.getInstance().getPrice("" + price) + "</font>";

			Log.e("ProductPriceView getViewPriceHome ", content);

			TextView tv_price = new TextView(mContext);
			if (DataLocal.isLanguageRTL) {
				tv_price.setGravity(Gravity.RIGHT);
			} else {
				tv_price.setGravity(Gravity.LEFT);
			}
			tv_price.setText(Html.fromHtml(content));
			if (DataLocal.isTablet) {
				tv_price.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
			} else {
				tv_price.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
			}
			return tv_price;
		}
		return null;

	}

	public View getViewPrice() {
		if (mTypeProduct == null) {
			mTypeProduct = "simple";
		}
		if (mTypeProduct.equals("simple") || mTypeProduct.equals("virtual")
				|| mTypeProduct.equals("downloadable")
				|| mTypeProduct.equals(PRODUCT_PRICE_TYPE_1)) {
			mTypeProduct = PRODUCT_PRICE_TYPE_1;
			return getViewPriceType1();
		} else if (mTypeProduct.equals("bundle")
				|| mTypeProduct.equals(PRODUCT_PRICE_TYPE_2)) {
			mTypeProduct = PRODUCT_PRICE_TYPE_2;
			return getViewPriceType2();
		} else if (mTypeProduct.equals("grouped")
				|| mTypeProduct.equals(PRODUCT_PRICE_TYPE_3)) {
			mTypeProduct = PRODUCT_PRICE_TYPE_3;
			return getViewPriceType3();
		} else if (mTypeProduct.equals("configurable")
				|| mTypeProduct.equals(PRODUCT_PRICE_TYPE_4)) {
			mTypeProduct = PRODUCT_PRICE_TYPE_4;
			return getViewPriceType1();
		}
		return null;
	}

	public View getViewPriceType1() {
		return null;
	}

	public View getViewPriceType2() {
		return null;
	}

	public View getViewPriceType3() {
		return null;
	}

	public View resumeViewPrice() {
		return mViewPrice;
	}

	public View showPriceFromToListType() {
		LinearLayout ll_priceFromTo = new LinearLayout(mContext);
		ll_priceFromTo.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		// price from
		String priceFrom = mPriceUtils.getTaxFrom();
		if (!priceFrom.equals("")) {
			TextView tv_priceFrom = new TextView(mContext);
			if (DataLocal.isTablet) {
				tv_priceFrom.setTextSize(16);
			}
			tv_priceFrom.setText(Html.fromHtml(priceFrom));
			ll_priceFromTo.addView(tv_priceFrom, params);

			// price to
			String priceTo = mPriceUtils.getTaxTo();
			TextView tv_priceTo = new TextView(mContext);
			if (DataLocal.isTablet) {
				tv_priceTo.setTextSize(16);
			}
			tv_priceTo.setText(Html.fromHtml(priceTo));
			ll_priceFromTo.addView(tv_priceTo, params);

			return ll_priceFromTo;
		}
		return null;
	}

	public View showPriceFromToGridType() {
		LinearLayout ll_priceFromTo = new LinearLayout(mContext);
		ll_priceFromTo.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		// price from
		String priceFrom = mPriceUtils.getTaxFrom();
		TextView tv_priceFrom = new TextView(mContext);
		tv_priceFrom.setText(Html.fromHtml(priceFrom));
		params.setMargins(0, 0, 10, 0);
		ll_priceFromTo.addView(tv_priceFrom, params);

		// price to
		String priceTo = mPriceUtils.getTaxTo();
		TextView tv_priceTo = new TextView(mContext);
		tv_priceTo.setText(Html.fromHtml(priceTo));
		ll_priceFromTo.addView(tv_priceTo, params);

		return ll_priceFromTo;
	}

	public View showPriceConfigured() {
		String priceConfig = mPriceUtils.getPriceConfig();
		if (priceConfig.isEmpty()) {

			String price = "<font color='" + color_Label + "'>"
					+ Config.getInstance().getText("Price as configured")
					+ ": </font> <br/>";
			price = price + mPriceUtils.getPriceTax();

			return showPrice(price);

		}
		return showPrice(priceConfig);

	}

	public View showPriceTax() {
		String priceTax = mPriceUtils.getPriceTax();
		return showPrice(priceTax);
	}

	public View showNormalPrice() {
		String priceNormal = mPriceUtils.getNormalPrice();
		return showPrice(priceNormal);
	}

	public View showPriceSpecial() {
		String priceSpecial = mPriceUtils.getSpecialPrice();
		return showPrice(priceSpecial);
	}

	public View showRegularPriceStrike() {
		String regurlarprice = mPriceUtils.getRegularPriceStrike();
		return showPrice(regurlarprice);
	}

	public View showRegularPrice() {
		String regurlarprice = mPriceUtils.getRegularPrice();
		return showPrice(regurlarprice);
	}

	public View showMinimalPrice() {
		String minimalPrice = mPriceUtils.getPriceMinimal();
		return showPrice(minimalPrice);
	}

	public View showPrice(String content) {
		TextView tv_price = new TextView(mContext);

		if (DataLocal.isTablet) {
			tv_price.setTextSize(16);
			LinearLayout.LayoutParams params_0 = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

			params_0.gravity = Gravity.CENTER_HORIZONTAL;
			tv_price.setLayoutParams(params_0);
		}
		tv_price.setText(Html.fromHtml(content));
		int position = checkStrikeTag(content);
		if (position != -1) {
			LinearLayout ll_price = new LinearLayout(mContext);
			ll_price.setOrientation(LinearLayout.HORIZONTAL);
			LinearLayout.LayoutParams params_2 = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			LinearLayout.LayoutParams params_1 = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params_1.setMargins(0, 0, 5, 0);

			String content1 = content.substring(0, position - 1);
			String content2 = content.substring(position, content.length());

			content2 = content2.replace("<strike>", "");
			content2 = content2.replace("</strike>", "");
			SpannableString spannable = new SpannableString(
					Html.fromHtml(content1 + "   " + content2));
			spannable.setSpan(new StrikethroughSpan(), Html.fromHtml(content1)
					.length(), Html.fromHtml(content1 + "   " + content2)
					.length(), Paint.STRIKE_THRU_TEXT_FLAG);

			TextView tv_price1 = new TextView(mContext);
			TextView tv_price2 = new TextView(mContext);
			tv_price2.setPaintFlags(tv_price2.getPaintFlags()
					| Paint.STRIKE_THRU_TEXT_FLAG);
			tv_price1.setTextColor(Color.parseColor(Config.getInstance()
					.getPrice_color()));
			tv_price1.setText(spannable);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			if (DataLocal.isLanguageRTL) {
				params.gravity = Gravity.RIGHT;
				params_1.gravity = Gravity.RIGHT;
				ll_price.setGravity(Gravity.RIGHT);
				tv_price1.setGravity(Gravity.RIGHT);
			}
			ll_price.setLayoutParams(params);
			ll_price.addView(tv_price1, params_1);
			// ll_price.addView(tv_price2, params_2);

			return ll_price;

		}

		return tv_price;
	}

	public View showTierPriceFull() {
		String tierPrice = mPriceUtils.getPriceTireFull();
		return showPrice(tierPrice);
	}

	public View showTierPriceShort() {
		String content = mPriceUtils.getPriceTireShort();
		return showPrice(content);
	}

	public String getColorLabel() {
		return color_Label;
	}

	public void setColorLabel(String color_Label) {
		this.color_Label = color_Label;
		mPriceUtils.setColorLabel(color_Label);
	}

	public String getColorTire() {
		return color_Tire;
	}

	public void setColorTire(String color_Tire) {
		this.color_Tire = color_Tire;
		mPriceUtils.setColorTire(color_Tire);
	}

	public String getColorPrice() {
		return color_Price;
	}

	public void setColorPrice(String color_Price) {
		this.color_Price = color_Price;
		mPriceUtils.setColorPrice(color_Price);
	}

	public String getColorOther() {
		return color_Other;
	}

	public void setColorOther(String color_Other) {
		this.color_Other = color_Other;
		mPriceUtils.setColorOther(color_Other);
	}

	public String getTypeProduct() {
		return mTypeProduct;
	}

	public void setTypeProduct(String mTypeProduct) {
		this.mTypeProduct = mTypeProduct;
	}

	protected int checkStrikeTag(String source) {
		if (!source.contains("<strike>")) {
			return -1;
		}

		for (int i = 0; i < source.length(); i++) {
			if (source.charAt(i) == '<' && source.charAt(i + 1) == 's') {
				String strike = source.substring(i + 1, i + 7);
				if (strike.equals("strike")) {
					return i;
				}
			}
		}

		return -1;
	}

}
