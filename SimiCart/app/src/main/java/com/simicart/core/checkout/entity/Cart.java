package com.simicart.core.checkout.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;

public class Cart extends SimiEntity {
	protected String mID;
	protected String mProductID;
	protected String mProductName;
	protected int mQty = -1;
	protected String mProductImage;
	protected float mProductPrice = -1;
	protected ArrayList<Option> mOptions;
	protected String mProductStock;
	protected int min_qty_allow = 1;
	protected int max_qty_allow = 999;

	public String getStock() {
		if (null == mProductStock) {
			mProductStock = getData(Constants.STOCK_STATUS);
		}
		if (null != mProductStock && (mProductStock.equals("TRUE")
				|| mProductStock.equals("true") || mProductStock.equals("1"))) {
			return Config.getInstance().getText("In Stock");
		}
		return Config.getInstance().getText("Out Stock");
	}

	public void setStock(String stock) {
		this.mProductStock = stock;
	}

	public float getProduct_price() {
		if (mProductPrice == -1) {
			String price = getData(Constants.PRODUCT_PRICE);
			if (null != price) {
				mProductPrice = Float.parseFloat(price);
			}
		}
		return mProductPrice;
	}

	public ArrayList<Option> getOptions() {
		if (null == mOptions || mOptions.size() == 0) {
			try {
				mOptions = parseOption();
			} catch (JSONException e) {
				mOptions = null;
			}

		}
		return mOptions;
	}

	private ArrayList<Option> parseOption() throws JSONException {
		ArrayList<Option> options = new ArrayList<Option>();
		if (null != mJSON && mJSON.has(Constants.OPTIONS)) {
			String js_options = getData(Constants.OPTIONS);
			if (null != js_options) {
				JSONArray arr = new JSONArray(js_options);
				if (null != arr && arr.length() > 0) {
					for (int i = 0; i < arr.length(); i++) {
						Option option = new Option();
						option.setJSONObject(arr.getJSONObject(i));
						options.add(option);
					}
					return options;
				}
			}
		}
		return null;
	}

	public void setOptions(ArrayList<Option> options) {
		this.mOptions = options;
	}

	public void setProduct_price(float product_price) {
		this.mProductPrice = product_price;
	}

	public String getId() {
		if (null == mID) {
			mID = getData(Constants.CART_ITEM_ID);
		}
		return mID;
	}

	public void setId(String id) {
		this.mID = id;
	}

	public String getProduct_id() {

		if (null == mProductID) {
			mProductID = getData(Constants.PRODUCT_ID);
		}

		return mProductID;
	}

	public void setProduct_id(String product_id) {
		this.mProductID = product_id;
	}

	public String getProduct_name() {

		if (null == mProductName) {
			mProductName = getData(Constants.PRODUCT_NAME);
		}
		return mProductName;
	}

	public void setProduct_name(String product_name) {
		this.mProductName = product_name;
	}

	public int getQty() {
		if (mQty < 0) {
			String value = getData(Constants.PRODUCT_QTY);
			if (null != value && !value.equals("") && !value.equals("null")) {
				mQty = (int) Double.parseDouble(value);
			}

		}

		return mQty;
	}

	public void setQty(int qty) {
		this.mQty = qty;
	}

	public String getProduct_image() {
		if (null == mProductImage) {
			mProductImage = getData(Constants.PRODUCT_IMAGE);
		}
		return mProductImage;
	}

	public void setProduct_image(String product_image) {
		this.mProductImage = product_image;
	}
	public int getMinQtyAllow() {
		  String s_min = getData("min_qty_allow");
		  if (null != s_min) {
		   min_qty_allow = Integer.parseInt(s_min);
		  }
		  return min_qty_allow;
		 }

		 public int getMaxQtyAllow() {
		  String s_max = getData("max_qty_allow");
		  if (null != s_max) {
		   max_qty_allow = Integer.parseInt(s_max);
		  }
		  return max_qty_allow;
		 }

}
