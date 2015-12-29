package com.simicart.core.checkout.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.checkout.controller.ConfigCheckout;
import com.simicart.core.checkout.entity.Cart;
import com.simicart.core.checkout.entity.TotalPrice;
import com.simicart.core.config.Constants;

public class CartModel extends SimiModel {

	protected int mQty;
	protected TotalPrice mTotalPrice;

	public int getQty() {
		return mQty;
	}

	@Override
	protected void paserData() {
		try {
			JSONArray list = this.mJSON.getJSONArray("data");
			collection = new SimiCollection();
			collection.setJSON(mJSON);
			mQty = 0;
			for (int i = 0; i < list.length(); i++) {
				Cart cart = new Cart();
				cart.setJSONObject(list.getJSONObject(i));
				mQty += cart.getQty();
				ConfigCheckout.getInstance().setmQty("" + mQty);
				collection.addEntity(cart);
			}
			ConfigCheckout.getInstance().setCollectionCart(collection);

			try {
				if (mJSON.has(Constants.OTHER)) {
					JSONObject jsonPrice = mJSON.getJSONObject(Constants.OTHER);
					if (null != jsonPrice) {
						collection.setJSONOther(jsonPrice);
						mTotalPrice = new TotalPrice();
						mTotalPrice.setJSONObject(jsonPrice);
						ConfigCheckout.getInstance().setTotalPriceCart(
								mTotalPrice);
					}
				}
			} catch (JSONException e) {
				mTotalPrice = null;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void setUrlAction() {
		url_action = Constants.GET_CART;
	}

	public TotalPrice getTotalPrice() {
		return mTotalPrice;
	}

}
