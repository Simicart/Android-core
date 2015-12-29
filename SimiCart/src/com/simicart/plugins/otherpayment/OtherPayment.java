package com.simicart.plugins.otherpayment;

import org.json.JSONException;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.simicart.MainActivity;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.config.Config;
import com.simicart.core.event.activity.CacheActivity;
import com.simicart.core.event.checkout.CheckoutData;
import com.simicart.plugins.otherpayment.config.OtherPaymentConfig;
import com.simicart.plugins.otherpayment.fragment.OtherPaymentFragment;
import com.simicart.plugins.otherpayment.model.OtherPaymentModel;

public class OtherPayment {
	public OtherPayment(String method, CacheActivity cacheActivity) {
		Context context = cacheActivity.getActivity().getBaseContext();
		Log.e(getClass().getName(), "TESSSSSSSSSSSSSSSSSSSSS OtherPayment"
				+ method);
		if (method.equals("requestConfig")) {
			onRequestGetPaymentConfig(context);
		}
	}

	public OtherPayment(String method, CheckoutData checkoutData) {
		if (method.equals("onCheckOut")) {
			for (int i = 0; i < OtherPaymentConfig.getInstance()
					.getListPayment().size(); i++) {
				String url = "";
				try {
					if (checkoutData.getJsonPlaceOrder().has("url_action")) {
						url = checkoutData.getJsonPlaceOrder().getString(
								"url_action");
						Log.e("URL_ACTION", url);
					} else if (checkoutData.getJsonPlaceOrder().has(
							OtherPaymentConfig.getInstance().getListPayment()
									.get(i).getTitleUrlAction())) {
						url = checkoutData.getJsonPlaceOrder().getString(
								OtherPaymentConfig.getInstance()
										.getListPayment().get(i)
										.getTitleUrlAction());
						Log.e("URL_ACTION", url);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				Log.e("CheckOUtDAta payment", checkoutData.getPaymentMethod()
						.getPayment_method());
				Log.e("CheckOUT Config", OtherPaymentConfig.getInstance()
						.getListPayment().get(i).getPaymentMethod());
				if (checkoutData
						.getPaymentMethod()
						.getPayment_method()
						.toUpperCase()
						.equals(OtherPaymentConfig.getInstance()
								.getListPayment().get(i).getPaymentMethod()
								.toUpperCase())) {
					if (!url.equals("")) {
						if (OtherPaymentConfig.getInstance().getListPayment()
								.get(i).getCheckUrl().equals("1")) {
							if (url.equals((OtherPaymentConfig.getInstance()
									.getListPayment().get(i).getUrlFail()))) {
								showToastMessage(OtherPaymentConfig
										.getInstance().getListPayment().get(i)
										.getMessageFail());
							} else {
								OtherPaymentFragment fragment = OtherPaymentFragment
										.newInstance();
								fragment.setPayment(OtherPaymentConfig
										.getInstance().getListPayment().get(i));
								fragment.setInvoiceNumber(checkoutData
										.getInvoice_number());
								fragment.setUrlAction(url);
								SimiManager.getIntance().replaceFragment(
										fragment);
							}
						} else {
							OtherPaymentFragment fragment = OtherPaymentFragment
									.newInstance();
							fragment.setPayment(OtherPaymentConfig
									.getInstance().getListPayment().get(i));
							fragment.setInvoiceNumber(checkoutData
									.getInvoice_number());
							fragment.setUrlAction(url);
							SimiManager.getIntance().replaceFragment(fragment);
						}
					} else {
						showToastMessage(OtherPaymentConfig.getInstance()
								.getListPayment().get(i).getMessageFail());
					}
				}
			}
		}
	}

	private void onRequestGetPaymentConfig(Context context) {
		OtherPaymentModel model = new OtherPaymentModel();
		model.setDelegate(new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				if (isSuccess) {
				}
			}
		});
		model.request();
	}

	public void showToastMessage(String message) {
		Toast toast = Toast.makeText(MainActivity.context, Config.getInstance()
				.getText(message), Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(10000);
		toast.show();
	}
}
