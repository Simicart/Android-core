package com.simicart.core.common.price;

import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.catalog.product.entity.PriceV2;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.catalog.product.entity.ProductOption;
import com.simicart.core.config.DataLocal;

public class ProductPriceViewDetail extends ProductPriceView {

	public ProductPriceViewDetail(Product product) {
		super(product);
	}

	public ProductPriceViewDetail(PriceV2 priceV2) {
		mPriceUtils = new ProductPriceUtils();
		mPriceUtils.setPriceV2(priceV2);

		mContext = SimiManager.getIntance().getCurrentContext();
	}

	public View getViewPriceType1() {

		LinearLayout ll_price = new LinearLayout(mContext);
		ll_price.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		if (DataLocal.isLanguageRTL) {
			ll_price.setGravity(Gravity.RIGHT);
		} else {
			ll_price.setGravity(Gravity.LEFT);
		}

		if (mPriceUtils.getPriceTax().isEmpty()
				&& mPriceUtils.getTaxSpecial().isEmpty()) {
			ll_price.addView(showNormalPrice(), params);
		} else {

			if (mPriceUtils.getTaxSpecial().isEmpty()) {
				ll_price.addView(showPriceTax(), params);
			} else {
				ll_price.addView(showRegularPriceStrike(), params);
				ll_price.addView(showPriceSpecial(), params);
			}
		}
		if (!mPriceUtils.getPriceTireFull().equals("")) {
			ll_price.addView(showTierPriceFull(), params);
		}

		return ll_price;
	}

	public View getViewPriceType2() {
		LinearLayout ll_price = new LinearLayout(mContext);
		ll_price.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		if (DataLocal.isLanguageRTL) {
			ll_price.setGravity(Gravity.RIGHT);
		} else {
			ll_price.setGravity(Gravity.LEFT);
		}
		String taxminimal = mPriceUtils.getTaxMinimal();

		if (mPriceUtils.getTaxFromTo().isEmpty() && taxminimal.isEmpty()) {
			if (!mPriceUtils.getPriceTax().isEmpty()) {
				ll_price.addView(showPriceTax(), params);
			}
			ll_price.addView(showPriceConfigured(), params);
		}

		else {

			if (!taxminimal.isEmpty()) {
				TextView tv_taxminimal = (TextView) showPrice(taxminimal);
				ll_price.addView(tv_taxminimal, params);
			} else {
				ll_price.addView(showPriceFromToListType(), params);
				ll_price.addView(showPriceConfigured(), params);
			}
		}

		if (!mPriceUtils.getPriceTireFull().equals("")) {
			ll_price.addView(showTierPriceFull(), params);
		}

		return ll_price;
	}

	public View updatePriceWithOption(ProductOption option, boolean isAdd) {
		if (mTypeProduct.equals(PRODUCT_PRICE_TYPE_1)) {
			mViewPrice = updatePriceWithOptionType1(option, isAdd);
		}

		if (mTypeProduct.equals(PRODUCT_PRICE_TYPE_2)) {
			mViewPrice = updatePriceWithOptionType2(option, isAdd);
		}

		if (mTypeProduct.equals(PRODUCT_PRICE_TYPE_4)) {
			mViewPrice = updatePriceWithOptionType1(option, isAdd);
		}
		return mViewPrice;
	}

	// public View updatePriceWithOptionType4(ProductOption option, boolean
	// isAdd) {
	// LinearLayout ll_price = new LinearLayout(mContext);
	// ll_price.setOrientation(LinearLayout.VERTICAL);
	// LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
	// LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	// if (DataLocal.isLanguageRTL) {
	// ll_price.setGravity(Gravity.RIGHT);
	// } else {
	// ll_price.setGravity(Gravity.LEFT);
	// }
	//
	// if (mPriceUtils.getPriceTax().isEmpty()
	// && mPriceUtils.getTaxSpecial().isEmpty()) {
	// ll_price.addView(updateNormalPriceWithOption(option, isAdd), params);
	// } else {
	//
	// if (mPriceUtils.getTaxSpecial().isEmpty()) {
	// ll_price.addView(updatePriceTaxWithOption(option, isAdd),
	// params);
	// } else {
	//
	// ll_price.addView(
	// updateRegularPriceStrikeWithOption(option, isAdd),
	// params);
	// ll_price.addView(updatePriceSpecialWithOption(option, isAdd),
	// params);
	//
	// }
	// }
	//
	// return ll_price;
	// }

	public View updateNormalPriceConfigableWithOption(ProductOption option,
			boolean isAdd) {
		String content = mPriceUtils.updateNormalPriceConfigableWithOption(
				option, isAdd);
		return showPrice(content);
	}

	public View updatePriceWithOptionType1(ProductOption option, boolean isAdd) {

		LinearLayout ll_price = new LinearLayout(mContext);
		ll_price.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		if (DataLocal.isLanguageRTL) {
			ll_price.setGravity(Gravity.RIGHT);
		} else {
			ll_price.setGravity(Gravity.LEFT);
		}

		if (mPriceUtils.getPriceTax().isEmpty()
				&& mPriceUtils.getTaxSpecial().isEmpty()) {
			ll_price.addView(updateNormalPriceWithOption(option, isAdd), params);
		} else {

			if (mPriceUtils.getTaxSpecial().isEmpty()) {
				ll_price.addView(updatePriceTaxWithOption(option, isAdd),
						params);
			} else {

				ll_price.addView(
						updateRegularPriceStrikeWithOption(option, isAdd),
						params);
				ll_price.addView(updatePriceSpecialWithOption(option, isAdd),
						params);

			}
		}

		// if (!mPriceUtils.getPriceTireFull().equals("")) {
		//
		// ll_price.addView(showTierPriceFull(), params);
		// }

		return ll_price;

	}

	public View updatePriceWithOptionType2(ProductOption option, boolean isAdd) {
		LinearLayout ll_price = new LinearLayout(mContext);
		ll_price.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		if (DataLocal.isLanguageRTL) {
			ll_price.setGravity(Gravity.RIGHT);
		} else {
			ll_price.setGravity(Gravity.LEFT);
		}
		if (mPriceUtils.getTaxFromTo().isEmpty()
				&& mPriceUtils.getTaxMinimal().isEmpty()) {
			if (!mPriceUtils.getPriceTax().isEmpty()) {

				ll_price.addView(showPriceTax(), params);
			}
			ll_price.addView(
					updateProductPriceConfiguredWithOption(option, isAdd),
					params);
		} else {
			if (!mPriceUtils.getTaxFromTo().isEmpty()) {
				ll_price.addView(showPriceFromToListType(), params);
			}
			if (!mPriceUtils.getTaxMinimal().isEmpty()) {
				TextView tv_taxminimal = (TextView) showPrice(mPriceUtils
						.getTaxMinimal());
				ll_price.addView(tv_taxminimal, params);
			}
			ll_price.addView(updatePriceConfigWithOption(option, isAdd), params);
		}

		// if (!mPriceUtils.getPriceTireFull().equals("")) {
		// ll_price.addView(showTierPriceFull(), params);
		// }

		return ll_price;
	}

	public View updateNormalPriceWithOption(ProductOption option, boolean isAdd) {
		String content = mPriceUtils.updateNormalPriceWithOption(option, isAdd);
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

}
