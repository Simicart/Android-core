package com.simicart.plugins.paypal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.simicart.MainActivity;
import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.config.Config;
import com.simicart.plugins.paypal.model.PaypalModel;

public class PaypalSimicart extends Activity {

	View rootView;
	public static String CONFIG_CLIENT_ID = "AbwLSxDR0lE1ksdFL7YxfJlQ8VVmFCIbvoiO6adhbjb5vw2bWcJNnWXn";
	public static String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;
	private static final int REQUEST_CODE_PAYMENT = 1;
	// private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;

	public static String total = "0.0";
	public static String invoice_number = "";
	public static String bncode = "Magestore_SI_MagentoCE";

	SimiDelegate mDelegate;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		rootView = new LinearLayout(getApplicationContext());
		setContentView(rootView, new LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			CONFIG_CLIENT_ID = extras.getString("EXTRA_CLIENT_ID");
			String is_sandbox = extras.getString("EXTRA_SANDBOX");
			if (is_sandbox.equals("1")) {
				CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
			} else {
				CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION;
			}
			bncode = extras.getString("EXTRA_BNCODE");
			total = extras.getString("EXTRA_PRICE");
			invoice_number = extras.getString("EXTRA_INVOICE_NUMBER");
		}

		PayPalConfiguration config = new PayPalConfiguration().environment(
				CONFIG_ENVIRONMENT).clientId(CONFIG_CLIENT_ID);

		Intent intent = new Intent(this, PayPalService.class);
		intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
		startService(intent);

		this.onBuyPressed();

		mDelegate = new SimiBlock(rootView, MainActivity.context);
	}

	@Override
	public void onDestroy() {
		stopService(new Intent(this, PayPalService.class));
		super.onDestroy();
	}

	public void onBuyPressed() {
		PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);
		thingToBuy.bnCode(bncode);

		Intent intent = new Intent(PaypalSimicart.this, PaymentActivity.class);

		intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

		startActivityForResult(intent, REQUEST_CODE_PAYMENT);
	}

	private PayPalPayment getThingToBuy(String environment) {
		return new PayPalPayment(new BigDecimal(total), Config.getInstance()
				.getCurrency_code(), "Total fee", environment);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			PaymentConfirmation confirm = data
					.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
			if (confirm != null) {
				try {
					Log.e("paymentExample Complete", confirm.toJSONObject()
							.toString(4));
					requestUpdatePaypal(invoice_number, confirm.toJSONObject(),
							"1");

				} catch (JSONException e) {
					Log.e("paymentExample 2",
							"an extremely unlikely failure occurred: ", e);
				}
			}
		} else if (resultCode == Activity.RESULT_CANCELED) {
			requestUpdatePaypalCancel(invoice_number, "2");
			// Toast toast = Toast.makeText(MainActivity.context, Config
			// .getInstance().getText("The user canceled."),
			// Toast.LENGTH_LONG);
			// toast.setGravity(Gravity.CENTER, 0, 0);
			// toast.setDuration(10000);
			// toast.show();
			Intent i = new Intent(PaypalSimicart.this, MainActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
			finish();
		} else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
			// requestUpdatePaypalCancel(invoice_number, "2");
			Log.e("paymentExample 4",
					"An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
			setErrorConnection("Error",
					"CurrencyCode is invalid. Please see the docs.");
		}
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

	@SuppressLint("NewApi")
	public void requestUpdatePaypal(String invoice_number,
			JSONObject jsonObject, String payment_status) throws JSONException {
		mDelegate = new SimiBlock(rootView, MainActivity.context);
		mDelegate.showLoading();
		PaypalModel mModel = new PaypalModel();
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
		mModel.setDelegate(delegate);

		// get JSON
		JSONObject js_client = jsonObject.getJSONObject("client");
		String environment = js_client.getString("environment");
		String product_name = js_client.getString("product_name");
		String paypal_sdk_version = js_client.getString("paypal_sdk_version");
		String platform = js_client.getString("platform");

		String response_type = jsonObject.getString("response_type");

		JSONObject js_response = jsonObject.getJSONObject("response");
		String id = js_response.getString("id");
		String state = js_response.getString("state");
		String create_time = js_response.getString("create_time");
		String intent = js_response.getString("intent");

		// put
		List<NameValuePair> param_response_type = new ArrayList<NameValuePair>();
		param_response_type.add(new BasicNameValuePair("response_type", ""
				+ response_type + ""));
		JSONObject os_proof = endCode(param_response_type);

		List<NameValuePair> o_client = new ArrayList<NameValuePair>();
		o_client.add(new BasicNameValuePair("environment", "" + environment
				+ ""));
		o_client.add(new BasicNameValuePair("product_name", "" + product_name
				+ ""));
		o_client.add(new BasicNameValuePair("paypal_sdk_version", ""
				+ paypal_sdk_version + ""));
		o_client.add(new BasicNameValuePair("platform", "" + platform + ""));
		JSONObject os_client = endCode(o_client);

		List<NameValuePair> o_response = new ArrayList<NameValuePair>();
		o_response.add(new BasicNameValuePair("id", "" + id + ""));
		o_response.add(new BasicNameValuePair("state", "" + state + ""));
		o_response.add(new BasicNameValuePair("create_time", "" + create_time
				+ ""));
		o_response.add(new BasicNameValuePair("intent", "" + intent + ""));
		JSONObject os_response = endCode(o_response);

		os_proof.put("client", os_client);
		os_proof.put("response", os_response);

		mModel.addParam("proof", os_proof);
		mModel.addParam("invoice_number", "" + invoice_number + "");
		mModel.addParam("payment_status", "" + payment_status + "");
		mModel.request();
	}

	@SuppressLint("NewApi")
	public void requestUpdatePaypalCancel(String invoice_number,
			String payment_status) {
		mDelegate = new SimiBlock(rootView, MainActivity.context);
		mDelegate.showLoading();
		PaypalModel mModel = new PaypalModel();
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
		mModel.setDelegate(delegate);

		mModel.addParam("invoice_number", "" + invoice_number + "");
		mModel.addParam("payment_status", "" + payment_status + "");
		mModel.request();
	}

	public void changeView(String message) {
		Toast toast = Toast.makeText(MainActivity.context, Config.getInstance()
				.getText(message), Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(10000);
		toast.show();
		Intent i = new Intent(PaypalSimicart.this, MainActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
		finish();
	}
}
