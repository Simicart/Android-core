package com.simicart.plugins.ipay.delegate;

import java.io.Serializable;

import org.json.JSONException;

import com.ipay.IpayResultDelegate;
import com.simicart.plugins.ipay.IpaySimiCart;

@SuppressWarnings("serial")
public class ResultDelegate implements IpayResultDelegate, Serializable {
	private String order_id;

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	@Override
	public void onPaymentCanceled(String TransId, String RefNo, String Amount,
			String Remark, String AuthCode) {
		
		try {
			IpaySimiCart.context.showError(AuthCode);
			IpaySimiCart.context.requestUpdateIpay(TransId, AuthCode, RefNo, "2", order_id);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onPaymentFailed(String TransId, String RefNo, String Amount,
			String Remark, String AuthCode) {
		
		try {
			IpaySimiCart.context.showError(AuthCode);
			IpaySimiCart.context.requestUpdateIpay(TransId, AuthCode, RefNo, "2", order_id);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onPaymentSucceeded(String TransId, String RefNo, String Amount,
			String Remark, String AuthCode) {
		try {
			IpaySimiCart.context.requestUpdateIpay(TransId, AuthCode, RefNo, "1", order_id);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onRequeryResult(String arg0, String arg1, String arg2,
			String arg3) {
		// TODO Auto-generated method stub

	}

}
