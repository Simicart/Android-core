package com.simicart.plugins.ipay;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ipay.Ipay;
import com.ipay.IpayPayment;
import com.simicart.MainActivity;
import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.config.Config;
import com.simicart.plugins.ipay.delegate.ResultDelegate;
import com.simicart.plugins.ipay.model.IpayModel;

public class IpaySimiCart extends Activity {
	private static final int REQUEST_CODE_PAYMENT = 1;
	View rootView;
	public static IpaySimiCart context;
	public transient ResultDelegate result_delegate;
	SimiDelegate mDelegate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		rootView = new LinearLayout(getApplicationContext());
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String is_sandbox = extras.getString("EXTRA_SANDBOX");
			String amount = "";
			String currentcy = "";
			String country = "";
			if (is_sandbox.equals("1")) {
				amount = extras.getString("EXTRA_AMOUNT");
				currentcy = "MYR";
				country = "MY";
			} else {
				amount = extras.getString("EXTRA_AMOUNT");
				currentcy = extras.getString("EXTRA_CUREENTCY");
				country = extras.getString("EXTRA_COUNTRY");
			}
			String merchant_key = extras.getString("EXTRA_MECHANT_KEY");
			String merchant_code = extras.getString("EXTRA_MECHANT_CODE");
			String product_des = extras.getString("EXTRA_PRODUCTDES");
			String name = extras.getString("EXTRA_NAME");
			String email = extras.getString("EXTRA_EMAIL");
			String contact = extras.getString("EXTRA_CONTACT");
			String invoice = extras.getString("EXTRA_INVOICE");
			IpayPayment payment = new IpayPayment();
			payment.setMerchantKey(merchant_key);
			payment.setMerchantCode(merchant_code);
			payment.setPaymentId("");
			payment.setCurrency(currentcy);
			payment.setRefNo(invoice);
			payment.setAmount(amount);
			payment.setProdDesc(product_des);
			payment.setUserName(name);
			payment.setUserEmail(email);
			payment.setUserContact(contact);
			payment.setRemark("Success");
			payment.setLang("ISO-8859-1");
			payment.setCountry(country);
			payment.setBackendPostURL("https://www.mobile88.com/epayment/report/testsignature_response.asp");
			result_delegate = new ResultDelegate();
			result_delegate.setOrder_id(invoice);
			context = this;
			Intent checkoutIntent = Ipay.getInstance().checkout(payment,
					IpaySimiCart.this, result_delegate);
			startActivityForResult(checkoutIntent, REQUEST_CODE_PAYMENT);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_PAYMENT) {
			if (resultCode == Activity.RESULT_OK) {
				if (data != null) {
					Log.e("Data", data.toString());
				}
			} else if (resultCode == Activity.RESULT_CANCELED) {
				Toast toast = Toast.makeText(MainActivity.context, Config
						.getInstance().getText("Your order has been canceled"),
						Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.setDuration(10000);
				toast.show();
				Intent i = new Intent(IpaySimiCart.this, MainActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);
				finish();
			}
		}
	}

	public static JSONObject endCode(List<NameValuePair> pair)
			throws JSONException {
		int total = pair.size();
		JSONObject obj = new JSONObject();
		for (int i = 0; i < total; i++) {
			obj.put(pair.get(i).getName(), pair.get(i).getValue());
		}
		return obj;
	}

	public String endCodeJson(List<NameValuePair> pair) throws JSONException {
		// List<NameValuePair> text = new ArrayList<NameValuePair>();
		int total = pair.size();
		JSONObject obj = new JSONObject();
		for (int i = 0; i < total; i++) {
			obj.put(pair.get(i).getName(), pair.get(i).getValue());
		}
		return obj.toString();
	}

	public void requestUpdateIpay(String transcation_id, String auth_code,
			String ref_no, String status, String order_id) throws JSONException {
		mDelegate = new SimiBlock(rootView, MainActivity.context);
		mDelegate.showLoading();
		Log.e("invoice3", order_id);
		IpayModel model = new IpayModel();
		ModelDelegate delegate = new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				mDelegate.dismissLoading();
				if (isSuccess) {
					Log.e(getClass().getName(), "SUCCESS" + message);
				} else {
					Log.e(getClass().getName(), "FAIL" + message);
				}
				changeView(message);
			}
		};
		model.setDelegate(delegate);
		model.addParam("transaction_id", transcation_id + "");
		model.addParam("auth_code", auth_code + "");
		model.addParam("ref_no", ref_no + "");
		model.addParam("status", status + "");
		model.addParam("order_id", order_id + "");
		model.request();
	}

	public void setErrorConnection(String title, String message) {
		ProgressDialog.Builder alertbox = new ProgressDialog.Builder(this);
		alertbox.setTitle(Config.getInstance().getText(title));
		alertbox.setMessage(Config.getInstance().getText(message));
		alertbox.setPositiveButton(Config.getInstance().getText("OK")
				.toUpperCase(), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				finish();
			}
		});
		AlertDialog alertDialog = alertbox.create();
		alertDialog.show();
		alertDialog.setCanceledOnTouchOutside(false);
	}

	public void changeView(String message) {
		Toast toast = Toast.makeText(MainActivity.context, Config.getInstance()
				.getText(message), Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(10000);
		toast.show();
		Intent i = new Intent(IpaySimiCart.this, MainActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
		finish();
	}
	
	public void showError(String message){
		Toast toast = Toast.makeText(MainActivity.context, Config.getInstance()
				.getText(message), Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(10000);
		toast.show();
	}
}
