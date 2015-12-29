package com.simicart.core.common.price;

import android.graphics.Paint;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.catalog.product.entity.ProductOption;
import com.simicart.core.config.DataLocal;

public class ProductPriceViewV03 extends ProductPriceView {

	public ProductPriceViewV03(Product product) {
		super(product);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getViewPriceType1() {
		LinearLayout ll_price = new LinearLayout(mContext);
		ll_price.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER_HORIZONTAL;

		if (mPriceUtils.getPriceTax().isEmpty()
				&& mPriceUtils.getTaxSpecial().isEmpty()) {
			// if no tax, will show normal price
			// Log.e("ProductPriceViewDetailZTheme getViewPriceType1", "002");
			ll_price.addView(showNormalPrice(), params);
			return ll_price;
		} else {
			if (mPriceUtils.getTaxSpecial().isEmpty()) {
				// if no special price, show tax price
				// Log.e("ProductPriceViewDetailZTheme getViewPriceType1",
				// "003");
				ll_price.addView(showPriceTax(), params);
				return ll_price;
			} else {
				// show special price tax
				// ll_price.addView(showRegularPriceStrike(), params);
				// Log.e("ProductPriceViewDetailZTheme getViewPriceType1",
				// "004");
				ll_price.addView(showPriceSpecial(), params);
				return ll_price;
			}
		}
	}

	@Override
	public View getViewPriceType2() {
		return showPriceConfigured();
	}

	public View updatePriceWithOption(ProductOption option, boolean isAdd) {
		if (mTypeProduct.equals("simple") || mTypeProduct.equals("virtual")
				|| mTypeProduct.equals(PRODUCT_PRICE_TYPE_1)) {
			mViewPrice = updatePriceWithOptionType1(option, isAdd);
		}

		if (mTypeProduct.equals("bundle")
				|| mTypeProduct.equals(PRODUCT_PRICE_TYPE_2)) {
			mViewPrice = updatePriceWithOptionType2(option, isAdd);
		}

		if (mTypeProduct.equals("configurable")
				|| mTypeProduct.equals(PRODUCT_PRICE_TYPE_4)) {
			mViewPrice = updatePriceWithOptionType1(option, isAdd);
		}
		return mViewPrice;
	}

	public View updatePriceWithOptionType1(ProductOption option, boolean isAdd) {
		// update price for virtual, simple and configurable product
		LinearLayout ll_price = new LinearLayout(mContext);
		ll_price.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		if (mPriceUtils.getPriceTax().isEmpty()
				&& mPriceUtils.getTaxSpecial().isEmpty()) {
			ll_price.addView(updateNormalPriceWithOption(option, isAdd), params);
			return ll_price;
		} else {
			if (mPriceUtils.getTaxSpecial().isEmpty()) {
				ll_price.addView(updatePriceTaxWithOption(option, isAdd),
						params);
				return ll_price;
			} else {
				ll_price.addView(updatePriceSpecialWithOption(option, isAdd),
						params);
				return ll_price;
			}
		}
	}

	public View updatePriceWithOptionType2(ProductOption option, boolean isAdd) {
		// update price for bundle product
		LinearLayout ll_price = new LinearLayout(mContext);
		ll_price.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		if (mPriceUtils.getTaxFromTo().isEmpty()
				&& mPriceUtils.getTaxMinimal().isEmpty()) {
			ll_price.addView(
					updateProductPriceConfiguredWithOption(option, isAdd),
					params);
			return ll_price;
		} else {
			ll_price.addView(updatePriceConfigWithOption(option, isAdd), params);
			return ll_price;
		}

	}

	@Override
	public View showPriceFromToListType() {

		// price from
		String priceFrom = mPriceUtils.getTaxFrom();
		if (!priceFrom.equals("")) {
			TextView tv_priceFrom = new TextView(mContext);
			if (DataLocal.isTablet) {
				tv_priceFrom.setTextSize(16);
			}
			priceFrom = filterContent(priceFrom);

			// price to
			String priceTo = mPriceUtils.getTaxTo();

			priceTo = filterContent(priceTo);

			String content = priceFrom + " " + priceTo;
			tv_priceFrom.setText(Html.fromHtml(content));

			return tv_priceFrom;
		}
		return null;
	}

	public View updateNormalPriceWithOption(ProductOption option, boolean isAdd) {
		String content = mPriceUtils.updateNormalPriceWithOption(option, isAdd);
		return showPrice(content);
	}

	public View updateNormalPriceWithOption(String price, boolean isAdd) {
		String content = mPriceUtils.updateNormalPriceWithOption(price, isAdd);
		return showPrice(content);
	}

	public View updatePriceTaxWithOption(ProductOption option, boolean isAdd) {
		String content = mPriceUtils.updatePriceTaxWithOption(option, isAdd);
		return showPrice(content);
	}

	public View updateRegularPriceStrikeWithOption(ProductOption option,
			boolean isAdd) {
		String content = mPriceUtils.updateRegularPriceStrikeWithOption(option,
				isAdd);
		return showPrice(content);
	}

	public View updatePriceSpecialWithOption(ProductOption option, boolean isAdd) {
		String content = mPriceUtils
				.updateSpecialPriceWithOption(option, isAdd);
		return showPrice(content);
	}

	public View updateProductPriceConfiguredWithOption(ProductOption option,
			boolean isAdd) {

		String content = mPriceUtils.updateProductPriceConfigWithOption(option,
				isAdd);
		return showPrice(content);
	}

	public View updatePriceConfigWithOption(ProductOption option, boolean isAdd) {
		String content = mPriceUtils.updatePriceConfigWithOption(option, isAdd);
		return showPrice(content);
	}

	protected String filterContent(String content) {

		if (content.contains("Price")) {
			content = content.replace("Price", "");
		}

		if (content.contains("as")) {
			content = content.replace("as", "");
		}

		if (content.contains("configured")) {
			content = content.replace("configured", "");
		}

		if (content.contains("Special")) {
			content = content.replace("Special", "");
		}
		if (content.contains("<br>")) {
			content = content.replace("<br>", " ");
		}
		if (content.contains(":")) {
			content = content.replace(":", "");
		}
		return content;
	}

	@Override
	public View showPrice(String content) {
		TextView tv_price = new TextView(mContext);
		LinearLayout.LayoutParams param_1 = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		param_1.gravity = Gravity.LEFT;
		tv_price.setLayoutParams(param_1);
		tv_price.setLines(1);
		tv_price.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		if (DataLocal.isTablet) {
			tv_price.setTextSize(16);
		}
		content = filterContent(content);
		tv_price.setText(Html.fromHtml(content));
		int position = checkStrikeTag(content);
		if (position != -1) {
			LinearLayout ll_price = new LinearLayout(mContext);
			ll_price.setOrientation(LinearLayout.HORIZONTAL);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			LinearLayout.LayoutParams params_1 = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params_1.setMargins(0, 0, 5, 0);

			String content1 = content.substring(0, position - 1);
			String content2 = content.substring(position, content.length());

			content2 = content2.replace("<strike>", "");
			content2 = content2.replace("</strike>", "");

			TextView tv_price1 = new TextView(mContext);
			TextView tv_price2 = new TextView(mContext);

			tv_price1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
			tv_price2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

			tv_price2.setPaintFlags(tv_price2.getPaintFlags()
					| Paint.STRIKE_THRU_TEXT_FLAG);
			tv_price2.setText(Html.fromHtml(content2));
			tv_price1.setText(Html.fromHtml(content1));

			ll_price.addView(tv_price1, params_1);
			ll_price.addView(tv_price2, params);

			return ll_price;

		}

		return tv_price;
	}

	public View updatePriceWithOptionConfigable(Product mProduct, boolean isAdd) {
		String price = mProduct.getDependentPriceOption();
		if (isAdd) {
			mProduct.setCachePriceOptionDependent(price);
			if (mProduct.isAddedPriceDependent()) {
				price = "";
			}
		} else {
			if (mProduct.isAddedPriceDependent()) {
				price = mProduct.getCachePriceOptionDependent();
			}
			mProduct.setAddedPriceDependent(false);
		}
		if (isAdd && mProduct.isCompleteDependent()) {
			mProduct.setAddedPriceDependent(true);
		}

		// update price for virtual, simple and configurable product
		LinearLayout ll_price = new LinearLayout(mContext);
		ll_price.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		if (mPriceUtils.getPriceTax().isEmpty()
				&& mPriceUtils.getTaxSpecial().isEmpty()) {
			ll_price.addView(updateNormalPriceWithOption(price, isAdd), params);
			return ll_price;
		} else {
			if (mPriceUtils.getTaxSpecial().isEmpty()) {
				ll_price.addView(updatePriceTaxWithOption(price, isAdd), params);
				return ll_price;
			} else {
				ll_price.addView(updatePriceSpecialWithOption(price, isAdd),
						params);
				return ll_price;
			}
		}
	}

	private View updatePriceSpecialWithOption(String price, boolean isAdd) {
		String content = mPriceUtils.updateSpecialPriceWithOption(price, isAdd);
		return showPrice(content);
	}

	private View updatePriceTaxWithOption(String price, boolean isAdd) {
		String content = mPriceUtils.updatePriceTaxWithOption(price, isAdd);
		return showPrice(content);
	}
}
