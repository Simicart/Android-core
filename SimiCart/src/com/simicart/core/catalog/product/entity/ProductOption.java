package com.simicart.core.catalog.product.entity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.config.Constants;

public class ProductOption extends SimiEntity {
	protected String mID;
	protected String mTypeID = "-1";
	protected int mPosition = -1;
	protected String mOptionCart;
	protected float mPrice = -1;
	protected float mPriceInclTax;
	protected String isRequired;
	protected boolean checked;
	protected String show_price_v2;
	protected String mValue;
	protected String mTitle;
	protected String mType;
	protected ArrayList<String> dependence_option_ids = null;
	protected ArrayList<String> current_list_dependence_option_id = null;
	protected ArrayList<String> option_prices = null;
	protected HashMap<String, String> dependence_options = null;
	protected PriceV2 mShowPriceV2;
	protected String is_default;
	protected String mQty;

	public ProductOption() {
		mPriceInclTax = -1;
	}

	public ArrayList<String> getCurrent_list_dependence_option_id() {
		return current_list_dependence_option_id;
	}

	public void setCurrent_list_dependence_option_id(
			ArrayList<String> current_list_dependence_option_id) {
		this.current_list_dependence_option_id = current_list_dependence_option_id;
	}

	public PriceV2 getShowPriceV2() {
		if (null == mShowPriceV2) {
			mShowPriceV2 = new PriceV2();
			try {
				mShowPriceV2.setJSONObject(new JSONObject(
						getData(Constants.SHOW_PRICE_V2)));
			} catch (JSONException e) {
				return null;
			}
		}
		return mShowPriceV2;
	}

	public void setShowPriceV2(PriceV2 mShowPriceV2) {
		this.mShowPriceV2 = mShowPriceV2;
	}

	public boolean isShow_price_v2() {
		if (null == show_price_v2) {
			show_price_v2 = getData(Constants.IS_SHOW_PRICE);

		}

		if (null != show_price_v2) {
			if (show_price_v2.equals("TRUE") || show_price_v2.equals("true")
					|| show_price_v2.equals("1")) {
				return true;
			}
		}

		return false;
	}

	public void setShow_price_v2(boolean show_price_v2) {
		this.show_price_v2 = String.valueOf(show_price_v2);
	}

	public String getOptionCart() {
		if (null == mOptionCart) {
			mOptionCart = getData(Constants.OPTION_CART);

		}

		return this.mOptionCart;
	}

	public void setOptionCart(String option_cart) {
		this.mOptionCart = option_cart;
	}

	public String getOptionQty() {
		if (null == mQty) {
			mQty = getData(Constants.OPTION_QTY);
		}

		return mQty;
	}

	public void setOptionQty(String qty) {
		mQty = qty;
	}

	// public ArrayList<String> getDependenceOptionIds() {
	// if (null == dependence_option_ids) {
	// try {
	// String value = getData(Constants.DEPENDENCE_OPTION_IDS);
	// if (null != value) {
	// dependence_option_ids = parseDependenceOptionIds(new JSONArray(
	// value));
	// }
	// } catch (JSONException e) {
	// return null;
	// }
	// }
	// return this.dependence_option_ids;
	// }

	public HashMap<String, String> getDependence_options() {

		if (null == dependence_option_ids) {
			try {
				String value = getData(Constants.DEPENDENCE_OPTION_IDS);
				if (null != value) {
					dependence_option_ids = parseDependenceOptionIds(new JSONArray(
							value));
				}
			} catch (JSONException e) {
				return null;
			}
		}

		if (null == option_prices) {
			try {
				String value = getData(Constants.OPTION_PRICES);
				if (null != value) {
					option_prices = parseDependenceOptionIds(new JSONArray(
							value));
				}
			} catch (JSONException e) {
				return null;
			}
		}
		if (null == dependence_options) {
			if (dependence_option_ids != null) {
				if (option_prices != null) {
					dependence_options = new HashMap<>();
					for (int j = 0; j < dependence_option_ids.size(); j++) {
						dependence_options.put(dependence_option_ids.get(j),
								option_prices.get(j));
					}
				} else {
					dependence_options = new HashMap<>();
					for (int j = 0; j < dependence_option_ids.size(); j++) {
						dependence_options.put(dependence_option_ids.get(j),
								getOptionPrice() + "");
					}
				}
			}
		}

		return dependence_options;
	}

	private ArrayList<String> parseDependenceOptionIds(JSONArray json)
			throws JSONException {
		int n = json.length();
		ArrayList<String> options = new ArrayList<String>(n);
		for (int i = 0; i < n; i++) {
			options.add(json.getString(i));
		}
		return options;
	}

	private ArrayList<String> parseDependenceOptionPrices(JSONArray json)
			throws JSONException {
		int n = json.length();
		ArrayList<String> prices = new ArrayList<String>(n);
		for (int i = 0; i < n; i++) {
			prices.add(json.getString(i));
		}
		return prices;
	}

	// public void setDependenceOptionIds(ArrayList<String>
	// dependence_option_ids) {
	// this.dependence_option_ids = dependence_option_ids;
	// }

	public void setDependence_options(HashMap<String, String> dependence_options) {
		this.dependence_options = dependence_options;
	}

	public String getOptionTypeId() {
		String value = getData(Constants.OPTION_TYPE_ID);
		if (null != value) {
			this.mTypeID = value;
		}
		return mTypeID;
	}

	public void setOptionTypeId(String option_type_id) {
		this.mTypeID = option_type_id;
	}

	public int getPosition() {
		if (mPosition < 0) {
			String value = getData(Constants.POSITION);
			if (null != value) {
				mPosition = Integer.parseInt(value);
			}
		}
		return mPosition;
	}

	public void setPosition(int position) {
		this.mPosition = position;
	}

	public boolean isRequired() {

		if (null == isRequired) {
			isRequired = getData(Constants.IS_REQUIRED);
			if (null != isRequired) {

				if (isRequired.equals("TRUE") || isRequired.equals("true")
						|| isRequired.equals("1") || isRequired.equals("YES")
						|| isRequired.equals("yes")) {
					return true;
				} else {
					if (isRequired.equals("TRUE") || isRequired.equals("true")
							|| isRequired.equals("1")
							|| isRequired.equals("YES")
							|| isRequired.equals("yes")) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public void setRequired(boolean is_required) {
		this.isRequired = String.valueOf(is_required);
	}

	public boolean isChecked() {

		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getOptionType() {
		if (null == mType) {
			mType = getData(Constants.OPTION_TYPE);
		}

		return this.mType;
	}

	public void setOptionType(String option_type) {
		this.mType = option_type;
	}

	public String getOptionTitle() {
		if (null == mTitle) {
			mTitle = getData(Constants.OPTION_TITLE);
		}
		return this.mTitle;
	}

	public void setOptionTitle(String option_title) {
		this.mTitle = option_title;
	}

	public String getOptionValue() {
		if (null == mValue) {
			mValue = getData(Constants.OPTION_VALUE);
		}
		return this.mValue;
	}

	public void setOptionValue(String option_value) {
		this.mValue = option_value;
	}

	public float getOptionPrice() {

		if (mPrice < 0) {
			String value = getData(Constants.OPTION_PRICE);
			if (null != value) {
				mPrice = Float.parseFloat(value);
			}
		}

		return mPrice;
	}

	public void setOptionPrice(float option_price) {
		this.mPrice = option_price;
	}

	public String getOptionId() {
		if (null == mID) {
			mID = getData(Constants.OPTION_ID);
		}

		return this.mID;
	}

	public void setOptionId(String option_id) {
		this.mID = option_id;
	}

	public float getOption_price_incl_tax() {
		if (mPriceInclTax <= 0) {
			String value = getData(Constants.OPTION_PRICE_INCL_TAX);
			if (null != value) {
				mPriceInclTax = Float.parseFloat(value);
			}
		}

		return mPriceInclTax;
	}

	public void setOption_price_incl_tax(float option_price_incl_tax) {
		this.mPriceInclTax = option_price_incl_tax;
	}

	public void setIsDefault(String option) {
		this.is_default = option;
	}

	public String getIsDefault() {
		// String isDefault = getData(Constants.IS_DEFAULT);
		// if (null != isDefault && !isDefault.equals("0")) {
		// return isDefault;
		// }

		if (null == this.is_default) {
			this.is_default = getData(Constants.IS_DEFAULT);
			if (null == is_default) {
				is_default = "0";
			}
		}

		return this.is_default;
	}

}
