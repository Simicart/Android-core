package com.simicart.core.checkout.controller;

import org.json.JSONObject;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.checkout.delegate.CartDelegate;
import com.simicart.core.checkout.model.CartModel;
import com.simicart.core.config.Constants;

public class CartController extends SimiController {

	protected CartDelegate mDelegate;

	// protected boolean isLoadAgain = true;

	public CartController() {

	}

	@Override
	public void onStart() {
		if (ConfigCheckout.getInstance().getmQty() > 0) {
			if (ConfigCheckout.getInstance().getCartFirstRequest() == true) {
				request();
				ConfigCheckout.getInstance().setCartFirstRequest(false);
			} else {
				if (ConfigCheckout.getInstance().getStatusCart() == true) {
					request();
					ConfigCheckout.getInstance().setCheckStatusCart(false);
				} else {
					resumeJson();
				}
			}
		} else {
			mDelegate.visibleAllView();
		}
	}

	private void request() {
		mModel = new CartModel();
		mDelegate.showLoading();
		ModelDelegate delegate = new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				mDelegate.dismissLoading();
				if (isSuccess) {
					ConfigCheckout.getInstance().setMessageCart(message);
					mDelegate.setMessage(message);
					mDelegate.updateView(mModel.getCollection());
					mDelegate.onUpdateTotalPrice(((CartModel) mModel)
							.getTotalPrice());
					int carQty = ((CartModel) mModel).getQty();
					SimiManager.getIntance().onUpdateCartQty(
							String.valueOf(carQty));

					String url = getUrl(mModel.getCollection().getJSON());
					mDelegate.setCheckoutWebView(url);
				} else {
					SimiManager.getIntance().showNotify(message);
				}
			}
		};
		mModel.setDelegate(delegate);
		mModel.request();
	}

	private String getUrl(JSONObject mJSON) {
		String mOrderWebview = "";
		try {
			if (mJSON != null && mJSON.has(Constants.OTHER)) {
				JSONObject jsonPrice = mJSON.getJSONObject(Constants.OTHER);
				if (jsonPrice.has("order_by_webview_url")) {
					mOrderWebview = jsonPrice.getString("order_by_webview_url");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return mOrderWebview;
	}

	private void resumeJson() {
		if (ConfigCheckout.getInstance().getMessageCart() != null)
			mDelegate.setMessage(ConfigCheckout.getInstance().getMessageCart());
		if (ConfigCheckout.getInstance().getCollectionCart() != null)
			mDelegate.updateView(ConfigCheckout.getInstance()
					.getCollectionCart());
		if (ConfigCheckout.getInstance().getTotalPriceCart() != null)
			mDelegate.onUpdateTotalPrice(ConfigCheckout.getInstance()
					.getTotalPriceCart());
		SimiManager.getIntance().onUpdateCartQty(
				String.valueOf(ConfigCheckout.getInstance().getmQty()));

		if (ConfigCheckout.getInstance().getCollectionCart() != null) {
			String url = getUrl(ConfigCheckout.getInstance()
					.getCollectionCart().getJSON());
			mDelegate.setCheckoutWebView(url);
		} else {
			mDelegate.setCheckoutWebView("");
		}
	}

	@Override
	public void onResume() {
		if (ConfigCheckout.getInstance().getStatusCart()) {
			if (mModel != null && mModel.getCollection() != null) {
				mDelegate.updateView(mModel.getCollection());
				mDelegate.onUpdateTotalPrice(((CartModel) mModel)
						.getTotalPrice());
				String url = getUrl(mModel.getCollection().getJSON());
				mDelegate.setCheckoutWebView(url);
			}
		} else {
			if (ConfigCheckout.getInstance().getCollectionCart() != null) {
				mDelegate.updateView(ConfigCheckout.getInstance()
						.getCollectionCart());
			} else {
				mDelegate.visibleAllView();
			}
			if (ConfigCheckout.getInstance().getTotalPriceCart() != null) {
				mDelegate.onUpdateTotalPrice(ConfigCheckout.getInstance()
						.getTotalPriceCart());
			}
			if (ConfigCheckout.getInstance().getCollectionCart() != null) {
				String url = getUrl(ConfigCheckout.getInstance()
						.getCollectionCart().getJSON());
				mDelegate.setCheckoutWebView(url);
			} else {
				mDelegate.setCheckoutWebView("");
			}
		}
	}

	public void setDelegate(CartDelegate delegate) {
		mDelegate = delegate;
	}

}
