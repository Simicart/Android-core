package com.simicart.plugins.wishlist.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.catalog.product.entity.PriceV2;
import com.simicart.core.checkout.entity.Option;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.plugins.wishlist.common.WishListConstants;

public class ItemWishList extends SimiEntity {

	private String product_id;
	private String product_rate;
	private String product_name;
	private String wishlist_item_id;
	private String product_type;
	private String manufacturer_name;
	private String product_image;
	private String stock_status;

	private float product_price;
	private float product_regular_price;
	private int product_review_number;
	private boolean is_show_price;

	private boolean selected_all_required_options;

	private PriceV2 mPriceV2;
	private ArrayList<Option> mOptions;
	private String share_url;
	private String share_mes;

	public String getShare_mes() {
		return share_mes;
	}

	public String getShare_url() {
		return share_url;
	}

	public void setShare_url(String share_url) {
		this.share_url = share_url;
	}

	public void setShare_mes(String share_mes) {
		this.share_mes = share_mes;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getProduct_rate() {
		return product_rate;
	}

	public void setProduct_rate(String product_rate) {
		this.product_rate = product_rate;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getWishlist_item_id() {
		return wishlist_item_id;
	}

	public void setWishlist_item_id(String wishlist_item_id) {
		this.wishlist_item_id = wishlist_item_id;
	}

	public String getProduct_type() {
		return product_type;
	}

	public void setProduct_type(String product_type) {
		this.product_type = product_type;
	}

	public String getManufacturer_name() {
		return manufacturer_name;
	}

	public void setManufacturer_name(String manufacturer_name) {
		this.manufacturer_name = manufacturer_name;
	}

	public String getProduct_image() {
		return product_image;
	}

	public void setProduct_image(String product_image) {
		this.product_image = product_image;
	}

	public float getProduct_price() {
		return product_price;
	}

	public void setProduct_price(float product_price) {
		this.product_price = product_price;
	}

	public float getProduct_regular_price() {
		return product_regular_price;
	}

	public void setProduct_regular_price(float product_regular_price) {
		this.product_regular_price = product_regular_price;
	}

	public String getStock_status() {
		return stock_status;
	}

	public void setStock_status(String stock_status) {
		this.stock_status = stock_status;
	}

	public int getProduct_review_number() {
		return product_review_number;
	}

	public void setProduct_review_number(int product_review_number) {
		this.product_review_number = product_review_number;
	}

	public boolean isIs_show_price() {
		return is_show_price;
	}

	public void setIs_show_price(boolean is_show_price) {
		this.is_show_price = is_show_price;
	}

	public PriceV2 getmPriceV2() {
		return mPriceV2;
	}

	public void setmPriceV2(PriceV2 mPriceV2) {
		this.mPriceV2 = mPriceV2;
	}

	public ArrayList<Option> getmOptions() {
		return mOptions;
	}

	public void setmOptions(ArrayList<Option> mOptions) {
		this.mOptions = mOptions;
	}

	public boolean isSelected_all_required_options() {
		return selected_all_required_options;
	}

	public void setSelected_all_required_options(
			boolean selected_all_required_options) {
		this.selected_all_required_options = selected_all_required_options;
	}

	public boolean parse(JSONObject json) {
		try {
			if (json.has(Constants.PRODUCT_ID)) {
				product_id = json.getString(Constants.PRODUCT_ID);
			}
			if (json.has(Constants.PRODUCT_RATE)) {
				product_rate = json.getString(Constants.PRODUCT_RATE);
			}
			if (json.has(Constants.PRODUCT_PRICE)) {
				product_price = Float.parseFloat(json
						.getString(Constants.PRODUCT_PRICE));
				if (json.has(Constants.SHOW_PRICE_V2)) {
					JSONObject jsonObject = json
							.getJSONObject(Constants.SHOW_PRICE_V2);
					if (jsonObject.has(Constants.PRODUCT_PRICE)) {
						product_price = Float.parseFloat(jsonObject
								.getString(Constants.PRODUCT_PRICE));
					}
				}
			}
			if (json.has(Constants.PRODUCT_REGULAR_PRICE)) {
				product_regular_price = Float.parseFloat(json
						.getString(Constants.PRODUCT_REGULAR_PRICE));
				if (json.has(Constants.SHOW_PRICE_V2)) {
					JSONObject jsonObject = json
							.getJSONObject(Constants.SHOW_PRICE_V2);
					if (jsonObject.has(Constants.PRODUCT_REGULAR_PRICE)) {
						product_regular_price = Float.parseFloat(jsonObject
								.getString(Constants.PRODUCT_REGULAR_PRICE));
					}
				}
			}
			if (json.has(Constants.PRODUCT_NAME)) {
				product_name = json.getString(Constants.PRODUCT_NAME);
			}
			if (json.has(Constants.SHOW_PRICE_V2)) {
				JSONObject obj = json.getJSONObject(Constants.SHOW_PRICE_V2);
				PriceV2 v2 = new PriceV2();
				v2.setJSONObject(obj);
				if(v2 != null){
					mPriceV2 = v2;
				}
			}
			if (json.has(WishListConstants.PRODUCT_SHARING_URL)) {
				share_url = json
						.getString(WishListConstants.PRODUCT_SHARING_URL);
			}

			if (json.has(WishListConstants.PRODUCT_SHARING_MESSAGE)) {
				share_mes = json
						.getString(WishListConstants.PRODUCT_SHARING_MESSAGE);
			}

			if (json.has(WishListConstants.SELECTED_ALL_REQUIRED_OPTIONS)) {
				selected_all_required_options = json
						.getBoolean(WishListConstants.SELECTED_ALL_REQUIRED_OPTIONS);
			}
			if (json.has(WishListConstants.WISHLIST_ITEM_ID)) {
				wishlist_item_id = json
						.getString(WishListConstants.WISHLIST_ITEM_ID);
			}
			if (json.has(Constants.PRODUCT_TYPE)) {
				product_type = json.getString(Constants.PRODUCT_TYPE);
			}
			if (json.has(Constants.MANUFACTURER_NAME)) {
				manufacturer_name = json.getString(Constants.MANUFACTURER_NAME);
			}
			if (json.has(Constants.IS_SHOW_PRICE)) {
				is_show_price = json.getBoolean(Constants.IS_SHOW_PRICE);
			}
			if (json.has(Constants.PRODUCT_REVIEW_NUMBER)) {
				product_review_number = json
						.getInt(Constants.PRODUCT_REVIEW_NUMBER);
			}
			if (json.has(Constants.PRODUCT_IMAGE)) {
				product_image = json.getString(Constants.PRODUCT_IMAGE);
			}
			// parse stock status
			if (json.has(Constants.STOCK_STATUS)) {
				String stock = json.getString(Constants.STOCK_STATUS);
				if (stock.equals("true") || stock.equals("1")) {
					stock_status = Config.getInstance().getText("In Stock");
				} else {
					stock_status = Config.getInstance().getText("Out Stock");
				}
			}
			// parse options
			if (json.has(Constants.OPTIONS)) {
				JSONArray arrOption = json.getJSONArray(Constants.OPTIONS);
				for (int i = 0; i < arrOption.length(); i++) {
					JSONObject jsonOption = arrOption.getJSONObject(i);
					Option option = new Option();
					if (option.parse(jsonOption)) {
						if (null == mOptions) {
							mOptions = new ArrayList<Option>();
						}
						mOptions.add(option);
					}
				}
			}

			return true;
		} catch (JSONException e) {
			Log.w("JSON Exception", e.getMessage());
			return false;
		}
	}
}
