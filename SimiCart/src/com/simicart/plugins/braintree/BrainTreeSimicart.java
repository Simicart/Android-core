package com.simicart.plugins.braintree;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.braintreepayments.api.dropin.BraintreePaymentActivity;
import com.braintreepayments.api.dropin.Customization;
import com.simicart.MainActivity;
import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.config.Config;
import com.simicart.plugins.braintree.model.BrainTreeModel;

public class BrainTreeSimicart extends Activity {
	
	View rootView;
	public String total = "0.0";
	public String invoice_number = "";
	public String bncode = "Magestore_SI_MagentoCE";
	public String token = "";

	SimiDelegate mDelegate;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		rootView = new LinearLayout(getApplicationContext());
		setContentView(rootView, new LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		Log.e("BrainTreeSimicart", "onCreate");

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			bncode = extras.getString("EXTRA_BNCODE");
			total = extras.getString("EXTRA_PRICE");
			invoice_number = extras.getString("EXTRA_INVOICE_NUMBER");
			token = extras.getString("EXTRA_TOKEN");
		}

		this.onBuyPressed();

		mDelegate = new SimiBlock(rootView, MainActivity.context);
	}
	
	public void onBuyPressed() {
		Intent intent = new Intent(BrainTreeSimicart.this, BraintreePaymentActivity.class);
		intent.putExtra(BraintreePaymentActivity.EXTRA_CLIENT_TOKEN, token);
		 Customization customization = new
		 Customization.CustomizationBuilder()
		 .primaryDescription("Cart")
		 .secondaryDescription("ID: " + invoice_number)
		 .amount(total + "$")
		 .submitButtonText("Purchase")
		 .build();
		 intent.putExtra(BraintreePaymentActivity.EXTRA_CUSTOMIZATION,
		 customization);
		startActivityForResult(intent, 123);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 123) {
			Log.e("onActivityResult", "++" + resultCode);
			switch (resultCode) {
			case BraintreePaymentActivity.RESULT_OK:
				String paymentMethodNonce = data.getStringExtra(BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE);
				Log.e("PaymentMethod", "++" + paymentMethodNonce);
				if(paymentMethodNonce != null) {
					try {
						requestUpdateBrainTree(paymentMethodNonce, total, invoice_number);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case BraintreePaymentActivity.BRAINTREE_RESULT_DEVELOPER_ERROR:
				Log.e("PaymentMethod", "++" + "BRAINTREE_RESULT_DEVELOPER_ERROR");
				break;
			case BraintreePaymentActivity.BRAINTREE_RESULT_SERVER_ERROR:
				Log.e("PaymentMethod", "++" + "BRAINTREE_RESULT_SERVER_ERROR");
				break;
			case BraintreePaymentActivity.BRAINTREE_RESULT_SERVER_UNAVAILABLE:
				Log.e("PaymentMethod", "++" + "BRAINTREE_RESULT_SERVER_UNAVAILABLE");
				// handle errors here, a throwable may be available in
				// data.getSerializableExtra(BraintreePaymentActivity.EXTRA_ERROR_MESSAGE)
				break;
			default:
				break;
			}
		}
	}
	
	@SuppressLint("NewApi")
	public void requestUpdateBrainTree(String nonce,
			String amount, String orderID) throws JSONException {
		mDelegate = new SimiBlock(rootView, MainActivity.context);
		mDelegate.showLoading();
		BrainTreeModel mModel = new BrainTreeModel();
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

		mModel.addParam("nonce", nonce);
		mModel.addParam("amount", amount);
		mModel.addParam("order_id", orderID);
		mModel.request();
	}
	
	public void changeView(String message) {
		Toast toast = Toast.makeText(MainActivity.context, Config.getInstance()
				.getText(message), Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(10000);
		toast.show();
		Intent i = new Intent(BrainTreeSimicart.this, MainActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
		finish();
	}
}
