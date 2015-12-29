package com.simicart.core.catalog.product.entity;

import org.json.JSONArray;
import org.json.JSONException;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.config.Constants;

public class PriceV2 extends SimiEntity {

	private float mPrice = -1;
	private float mRegularPrice = -1;
	private float mExclTaxSpecial = -1;
	private float mInclTaxSpecial = -1;
	private float mExclTax = -1;
	private float mInclTax = -1;
	private float mInclTaxTo = -1;
	private float mInclTaxFrom = -1;
	private float mExclTaxTo = -1;
	private float mExclTaxFrom = -1;
	private String mMinimalPriceLabel;
	private float mMinimalPrice = -1;
	private float mExclTaxMinimal = -1;
	private float mInclTaxMinimal = -1;
	private float mExclTaxConfig = -1;
	private float mInclTaxConfig = -1;
	private float mProductPriceConfig = -1;
	private String[] mTierPrice;

	public float getPrice() {
		if (mPrice < 0) {
			String value = getData(Constants.PRODUCT_PRICE);
			if (null != value && !value.equals("") && !value.equals("null")) {
				mPrice = Float.parseFloat(value);
			}

		}
		return mPrice;
	}

	public void setPrice(float mPrice) {
		this.mPrice = mPrice;
	}

	public float getRegularPrice() {

		if (mRegularPrice < 0) {
			String value = getData(Constants.PRODUCT_REGULAR_PRICE);
			if (null != value && !value.equals("") && !value.equals("null")) {
				mRegularPrice = Float.parseFloat(value);
			}

		}
		return mRegularPrice;
	}

	public void setRegularPrice(float mRegularPrice) {
		this.mRegularPrice = mRegularPrice;
	}

	public float getExclTaxSpecial() {
		if (mExclTaxSpecial < 0) {
			String value = getData(Constants.EXCL_TAX_SPECIAL);
			if (null != value && !value.equals("") && !value.equals("null")) {
				mExclTaxSpecial = Float.parseFloat(value);
			}
		}
		return mExclTaxSpecial;
	}

	public void setExclTaxSpecial(float mExclTaxSpecial) {
		this.mExclTaxSpecial = mExclTaxSpecial;
	}

	public float getInclTaxSpecial() {
		if (mInclTaxSpecial < 0) {
			String value = getData(Constants.INCL_TAX_SPECIAL);
			if (null != value && !value.equals("") && !value.equals("null")) {
				mInclTaxSpecial = Float.parseFloat(value);
			}
		}
		return mInclTaxSpecial;
	}

	public void setInclTaxSpecial(float mInclTaxSpecial) {
		this.mInclTaxSpecial = mInclTaxSpecial;
	}

	public float getExclTax() {
		if (mExclTax < 0) {
			String value = getData(Constants.EXCL_TAX);
			if (null != value && !value.equals("") && !value.equals("null")) {
				mExclTax = Float.parseFloat(value);
			}
		}
		return mExclTax;
	}

	public void setExclTax(float mExclTax) {
		this.mExclTax = mExclTax;
	}

	public float getInclTax() {
		if (mInclTax < 0) {
			String value = getData(Constants.INCL_TAX);
			if (null != value && !value.equals("") && !value.equals("null")) {
				mInclTax = Float.parseFloat(value);
			}
		}
		return mInclTax;
	}

	public void setInclTax(float mInclTax) {
		this.mInclTax = mInclTax;
	}

	public float getInclTaxTo() {
		if (mInclTaxTo < 0) {
			String value = getData(Constants.INCL_TAX_TO);
			if (null != value && !value.equals("") && !value.equals("null")) {
				mInclTaxTo = Float.parseFloat(value);
			}
		}
		return mInclTaxTo;
	}

	public void setInclTaxTo(float mInclTaxTo) {
		this.mInclTaxTo = mInclTaxTo;
	}

	public float getInclTaxFrom() {
		if (mInclTaxFrom < 0) {
			String value = getData(Constants.INCL_TAX_FROM);
			if (null != value && !value.equals("") && !value.equals("null")) {
				mInclTaxFrom = Float.parseFloat(value);
			}
		}
		return mInclTaxFrom;
	}

	public void setInclTaxFrom(float mInclTaxFrom) {
		this.mInclTaxFrom = mInclTaxFrom;
	}

	public float getExclTaxTo() {
		if (mExclTaxTo < 0) {
			String value = getData(Constants.EXCL_TAX_TO);
			if (null != value && !value.equals("") && !value.equals("null")) {
				mExclTaxTo = Float.parseFloat(value);
			}
		}
		return mExclTaxTo;
	}

	public void setExclTaxTo(float mExclTaxTo) {
		this.mExclTaxTo = mExclTaxTo;
	}

	public float getExclTaxFrom() {
		if (mExclTaxFrom < 0) {
			String value = getData(Constants.EXCL_TAX_FROM);
			if (null != value && !value.equals("") && !value.equals("null")) {
				mExclTaxFrom = Float.parseFloat(value);
			}
		}
		return mExclTaxFrom;
	}

	public void setExclTaxFrom(float mExclTaxFrom) {
		this.mExclTaxFrom = mExclTaxFrom;
	}

	public String getMinimalPriceLabel() {
		if (null == mMinimalPriceLabel) {

			mMinimalPriceLabel = getData(Constants.MINIMAL_PRICE_LABEL);
		}
		return mMinimalPriceLabel;
	}

	public void setMinimalPriceLabel(String mMinimalPriceLabel) {
		this.mMinimalPriceLabel = mMinimalPriceLabel;
	}

	public Float getMinimalPrice() {
		if (mMinimalPrice < 0) {
			String value = getData(Constants.MINIMAL_PRICE);
			if (null != value && !value.equals("") && !value.equals("null")) {
				mMinimalPrice = Float.parseFloat(value);
			}
		}
		return mMinimalPrice;
	}

	public void setMinimalPrice(Float mMinimalPrice) {
		this.mMinimalPrice = mMinimalPrice;
	}

	public float getExclTaxMinimal() {
		if (mExclTaxMinimal < 0) {
			String value = getData(Constants.EXCL_TAX_MINIMAL);
			if (null != value && !value.equals("") && !value.equals("null")) {
				mExclTaxMinimal = Float.parseFloat(value);
			}
		}
		return mExclTaxMinimal;
	}

	public void setExclTaxMinimal(float mExclTaxMinimal) {
		this.mExclTaxMinimal = mExclTaxMinimal;
	}

	public float getInclTaxMinimal() {
		if (mInclTaxMinimal < 0) {
			String value = getData(Constants.INCL_TAX_MINIMAL);
			if (null != value && !value.equals("")) {
				mInclTaxMinimal = Float.parseFloat(value);
			}
		}
		return mInclTaxMinimal;
	}

	public void setInclTaxMinimal(float mInclTaxMinimal) {
		this.mInclTaxMinimal = mInclTaxMinimal;
	}

	public float getExclTaxConfig() {
		if (mExclTaxConfig < 0) {
			String value = getData(Constants.EXCL_TAX_CONFIG);
			if (null != value && !value.equals("") && !value.equals("null")) {
				mExclTaxConfig = Float.parseFloat(value);
			}
		}
		return mExclTaxConfig;
	}

	public void setExclTaxConfig(float mExclTaxConfig) {
		this.mExclTaxConfig = mExclTaxConfig;
	}

	public float getInclTaxConfig() {
		if (mInclTaxConfig < 0) {
			String value = getData(Constants.INCL_TAX_CONFIG);
			if (null != value && !value.equals("") && !value.equals("null")) {
				mInclTaxConfig = Float.parseFloat(value);
			}
		}
		return mInclTaxConfig;
	}

	public void setInclTaxConfig(float mInclTaxConfig) {
		this.mInclTaxConfig = mInclTaxConfig;
	}

	public float getProductPriceConfig() {
		if (mProductPriceConfig < 0) {

			String value = getData(Constants.PRODUCT_PRICE_CONFIG);
			if (null != value && !value.equals("") && !value.equals("null")) {
				mProductPriceConfig = Float.parseFloat(value);
			}

		}
		return mProductPriceConfig;
	}

	public void setProductPriceConfig(float mProductPriceConfig) {
		this.mProductPriceConfig = mProductPriceConfig;
	}

	public String[] getTierPrice() {
		if ((null == mTierPrice) || mTierPrice.length == 0) {
			try {
				String tierString = getData(Constants.TIER_PRICE);
				if (null != tierString && !tierString.equals("")) {
					JSONArray arr = new JSONArray(tierString);
					mTierPrice = getTierPrice(arr);
				}
			} catch (JSONException e) {
				return null;
			}
		}
		return mTierPrice;
	}

	public void setTierPrice(String[] mTierPrice) {
		this.mTierPrice = mTierPrice;
	}

	private String[] getTierPrice(JSONArray json) throws JSONException {
		int n = json.length();
		String[] tier_prices = new String[n];
		for (int i = 0; i < n; i++) {
			tier_prices[i] = json.getString(i);
		}
		return tier_prices;
	}

}
