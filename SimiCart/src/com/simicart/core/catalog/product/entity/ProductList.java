package com.simicart.core.catalog.product.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.config.Constants;

public class ProductList extends SimiEntity {
	private ArrayList<Product> mSpotProduct;
	private String mTitle;

	public ArrayList<Product> getSpotProduct() {

		if ((null == mSpotProduct) || (mSpotProduct.size() < 0)) {
			try {
				mSpotProduct = new ArrayList<Product>();
				JSONArray productList = new JSONArray(getData(Constants.DATA));
				// Log.e("ProductList ", productList.toString());
				if (null != productList && productList.length() > 0) {
					for (int i = 0; i < productList.length(); i++) {
						Product product = new Product();
						if (null != productList.getJSONObject(i)) {
							product.setJSONObject(productList.getJSONObject(i));
							mSpotProduct.add(product);
						} else {
							Log.e("Product List ", "Null " + i);
						}

					}
				}
			} catch (JSONException e) {
				Log.e("ProductList JSONException :", e.getMessage());
				return null;
			}
		}

		return mSpotProduct;
	}

	public void setSpotProduct(ArrayList<Product> spotProduct) {
		mSpotProduct = spotProduct;
	}

	public String getTitle() {
		if (null == mTitle) {
			mTitle = getData(Constants.TITLE);
		}
		return mTitle;
	}

	public void setTitle(String title) {
		this.mTitle = title;
	}
}
