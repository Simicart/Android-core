package com.simicart.core.checkout.controller;

import org.json.JSONObject;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.checkout.delegate.CartDelegate;
import com.simicart.core.checkout.model.CartModel;
import com.simicart.core.config.Constants;
import com.simicart.core.style.circlerefresh.CircleRefreshLayout.OnCircleRefreshListener;

public class CartController extends SimiController {

	protected CartDelegate mDelegate;
	
	private OnCircleRefreshListener onCircleRefreshListener;

	// protected boolean isLoadAgain = true;

	public CartController() {

	}
	
	public OnCircleRefreshListener getOnCircleReFreshListener() {
		return onCircleRefreshListener;
	}

	@Override
	public void onStart() {
		onCircleRefreshListener = new OnCircleRefreshListener() {
			
			@Override
			public void refreshing() {
				mModel = new CartModel();
				ModelDelegate delegate = new ModelDelegate() {

					@Override
					public void callBack(String message, boolean isSuccess) {
						if (isSuccess) {
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
						mDelegate.getCircleRefreshLayout().finishRefreshing();
					}
				};
				mModel.setDelegate(delegate);
				mModel.request();
			}
			
			@Override
			public void completeRefresh() {
				
			}
		};
		request();
	}

	@Override
	public void onResume() {
		request();
	}

	private void request() {
		mModel = new CartModel();
		mDelegate.showLoading();
		ModelDelegate delegate = new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				mDelegate.dismissLoading();
				if (isSuccess) {
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

	public void setDelegate(CartDelegate delegate) {
		mDelegate = delegate;
	}

}
