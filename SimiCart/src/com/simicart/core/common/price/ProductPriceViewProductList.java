package com.simicart.core.common.price;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.simicart.core.catalog.product.entity.Product;

public class ProductPriceViewProductList extends ProductPriceView {

	public ProductPriceViewProductList(Product product) {
		super(product);
	}

	@Override
	public View getViewPriceType1() {
		LinearLayout ll_price = new LinearLayout(mContext);
		ll_price.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		boolean isAdded = false;

		if (mPriceUtils.getPriceTax().isEmpty()
				&& mPriceUtils.getTaxSpecial().isEmpty()) {
			if (!isAdded) {
				isAdded = true;
				ll_price.addView(showNormalPrice(), params);
			}
		} else {

			if (mPriceUtils.getTaxSpecial().isEmpty()) {
				if (!isAdded) {
					isAdded = true;
					ll_price.addView(showPriceTax(), params);
				}
			} else {
				if (!isAdded) {
					isAdded = true;
					ll_price.addView(showRegularPriceStrike(), params);
				}
				if (!isAdded) {
					isAdded = true;
					ll_price.addView(showPriceSpecial(), params);
				}
			}
		}

		if (!mPriceUtils.getPriceMinimal().equals("")) {
			if (!isAdded) {
				isAdded = true;
				ll_price.addView(showMinimalPrice(), params);
			}

		}

		if (!mPriceUtils.getPriceTireShort().equals("")) {
			if (!isAdded) {
				isAdded = true;
				ll_price.addView(showTierPriceShort(), params);
			}
		}

		return ll_price;

	}

	@Override
	public View getViewPriceType2() {
		LinearLayout ll_price = new LinearLayout(mContext);
		ll_price.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		boolean isAdded = false;

		View view = showPriceFromToListType();
		if (null != view) {
			if (!isAdded) {
				isAdded = true;
				ll_price.addView(view, params);
			}
		} else {
			View viewTax = showPriceTax();
			if (null != viewTax) {
				if (!isAdded) {
					isAdded = true;
					ll_price.addView(viewTax, params);
				}
			}
		}

		View viewAsLowAs = showMinimalPrice();
		if (null != viewAsLowAs) {
			if (!isAdded) {
				isAdded = true;
				ll_price.addView(viewAsLowAs, params);
			}
		}

		if (!mPriceUtils.getPriceTireShort().equals("")) {
			ll_price.addView(showTierPriceShort(), params);
		}
		return ll_price;

	}

	@Override
	public View getViewPriceType3() {
		LinearLayout ll_price = new LinearLayout(mContext);
		ll_price.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		ll_price.addView(showMinimalPrice(), params);

		if (!mPriceUtils.getPriceTireShort().equals("")) {
			ll_price.addView(showTierPriceShort(), params);
		}
		return ll_price;
	}

}
