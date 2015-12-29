package com.simicart.core.customer.controller;

import java.util.ArrayList;

import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.checkout.controller.ConfigCheckout;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.customer.delegate.OrderHistoryReOrderDelegate;
import com.simicart.core.customer.model.OrderHistoryDetailModel;
import com.simicart.core.customer.model.OrderHistoryReOrderModel;

public class OrderHistoryDetailController extends SimiController {

	protected SimiDelegate mDelegate;
	protected String mID;
	protected OnTouchListener mReOrderClicker;
	protected OrderHistoryReOrderDelegate mReOrderDelegate;

	public OnTouchListener getReOrderClicker() {
		return mReOrderClicker;
	}

	public void setID(String id) {
		mID = id;
	}

	public void setDelegate(SimiDelegate delegate) {
		mDelegate = delegate;
	}

	public void setReOrderDelegate(OrderHistoryReOrderDelegate mReOrderDelegate) {
		this.mReOrderDelegate = mReOrderDelegate;
	}

	@Override
	public void onStart() {
		mDelegate.showLoading();
		mModel = new OrderHistoryDetailModel();
		mModel.setDelegate(new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				mDelegate.dismissLoading();
				if (isSuccess) {
					mDelegate.updateView(mModel.getCollection());
				}

			}
		});

		String email = DataLocal.getEmail();
		String password = DataLocal.getPassword();

		mModel.addParam(Constants.USER_EMAIL, email);
		mModel.addParam(Constants.USER_PASSWORD, password);
		mModel.addParam(Constants.ORDER_ID, mID);

		mModel.request();

		mReOrderClicker = new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					Utils.hideKeyboard(v);
					changeColorReOrder(Color.GRAY);
					break;
				}
				case MotionEvent.ACTION_UP: {
					Utils.hideKeyboard(v);
					requestReOrder();
				}
				case MotionEvent.ACTION_CANCEL: {
					changeColorReOrder(Config.getInstance().getColorMain());
					break;
				}
				default:
					break;
				}
				return true;
			}
		};
	}

	@Override
	public void onResume() {
		mDelegate.updateView(mModel.getCollection());
	}

	@SuppressWarnings("deprecation")
	protected void changeColorReOrder(int color) {
		// GradientDrawable gdDefault = new GradientDrawable();
		// gdDefault.setColor(color);
		mReOrderDelegate.reOrder().setBackgroundColor(color);
	}

	protected void requestReOrder() {
		mReOrderDelegate.showLoading();
		final OrderHistoryReOrderModel mModel = new OrderHistoryReOrderModel();
		mModel.setDelegate(new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				mReOrderDelegate.dismissLoading();
				if (isSuccess) {
					int numberQty = 0;
					ArrayList<SimiEntity> entity = mModel.getCollection()
							.getCollection();
					if (null != entity && entity.size() > 0) {
						for (SimiEntity simiEntity : entity) {
							String qty = simiEntity.getData("cart_qty");
							if (null != qty && !qty.equals("")
									&& !qty.equals("null")) {
								numberQty += Integer.parseInt(qty);
							}
						}
					}
					SimiManager.getIntance().onUpdateCartQty(
							String.valueOf(numberQty));
					ConfigCheckout.getInstance().setmQty(numberQty + "");
					SimiManager.getIntance().showToast(message);
					ConfigCheckout.getInstance().statusCart = true;
					SimiManager.getIntance().showToast(message);
				}
			}
		});
		mModel.addParam(Constants.ORDER_ID, mID);
		mModel.request();
	}
}
