package com.simicart.plugins.braintree;

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

import com.google.android.gms.wallet.Cart;
import com.simicart.MainActivity;
import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.config.Config;
import com.simicart.plugins.braintree.model.BrainTreeModel;
import com.trueplus.simicart.braintreelibrary.BraintreeFragment;
import com.trueplus.simicart.braintreelibrary.BraintreePaymentActivity;
import com.trueplus.simicart.braintreelibrary.PaymentRequest;

import org.json.JSONException;

public class BrainTreeActivity extends Activity {

    View rootView;
    public String total = "0.0";
    public String orderID = "";
    public String token = "";
    private BraintreeFragment mBraintreeFragment;

    SimiDelegate mDelegate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = new LinearLayout(this);
        setContentView(rootView, new LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            total = extras.getString("EXTRA_TOTAL_PRICE");
            orderID = extras.getString("EXTRA_INVOICE_NUMBER");
            token = extras.getString("EXTRA_TOKEN");
        }
        mDelegate = new SimiBlock(rootView, this);
        onBuyPressed();
    }


    public void onBuyPressed() {

        PaymentRequest paymentRequest = new PaymentRequest()
                .clientToken(token)
                .androidPayCart(getAndroidPayCart())
                .primaryDescription("Cart")
                .secondaryDescription(Config.getInstance().getText("Order ID: ") + orderID)
                .amount(total + " " + Config.getInstance().getCurrency_code());

        startActivityForResult(paymentRequest.getIntent(this), 123);
    }

    private Cart getAndroidPayCart() {
        return Cart.newBuilder()
                .setCurrencyCode(Config.getInstance().getCurrency_code())
                .setTotalPrice(total)
                .build();
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
                            requestUpdateBrainTree(paymentMethodNonce, total, orderID);
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
                    SimiManager.getIntance().showNotify(null, message, "Ok");
                    backToHome();
                }  else {
                    SimiManager.getIntance().showNotify(null, "Transaction Error!", "Ok");
                    backToHome();
                }
            }
        };
        mModel.setDelegate(delegate);

        mModel.addParam("nonce", nonce);
        mModel.addParam("amount", amount);
        mModel.addParam("order_id", orderID);
        mModel.request();
    }


//    private void confirmCancel() {
//        new AlertDialog.Builder(this)
//                .setMessage(
//                        Config.getInstance()
//                                .getText(
//                                        "Are you sure that you want to cancel the order?"))
//                .setPositiveButton(Config.getInstance().getText("Yes"),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog,
//                                                int which) {
//                                cancelOrder();
//                            }
//                        })
//                .setNegativeButton(Config.getInstance().getText("No"),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog,
//                                                int which) {
//                                // do nothing
//                                //getAuthorization();
//                            }
//                        }).show();
//    }


//    private void cancelOrder() {
//        BraintreCancelModel cancelModel = new BraintreCancelModel();
//        cancelModel.setDelegate(new ModelDelegate() {
//            @Override
//            public void onFail(SimiError error) {
//
//            }
//
//            @Override
//            public void onSuccess(SimiCollection collection) {
//                String message = "Your order has been canceled!";
//                showMessage(message);
//                backToHome();
//
//            }
//        });
//
//        cancelModel.addDataExtendURL(orderID);
//
//        cancelModel.request();
//    }

    public void showMessage(String message) {
        Toast toast = Toast.makeText(MainActivity.context, Config.getInstance()
                .getText(message), Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

    public void backToHome() {
        Intent i = new Intent(BrainTreeActivity.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        
    }
}
