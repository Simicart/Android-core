package com.simicart.plugins.braintree;


import com.simicart.MainActivity;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.checkout.entity.PaymentMethod;
import com.simicart.core.common.Utils;
import com.simicart.core.event.checkout.CheckoutData;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BraintreePayment {

    Context context;

    public BraintreePayment(String method, CheckoutData checkoutData) {
        this.context = MainActivity.context;
        Log.e("BrainTree", "BrainTree" + method);
        if (method.equals("callBrainTreeServer")) {
            this.callBrainTreeServer(checkoutData.getPaymentMethod(),
                    checkoutData.getInvoice_number(),
                    checkoutData.getTotal_price());
        }
    }

    public void callBrainTreeServer(PaymentMethod paymentMethod,
                                    String invoice_number, String total_price) {
        if (paymentMethod.getPayment_method().equals("SIMIBRAINTREE")) {
            Intent intent = new Intent(this.context, BrainTreeActivity.class);

            String token = paymentMethod.getData("token");
            intent.putExtra("EXTRA_TOKEN", token);
            Log.e("BrainTree TOKEN_ID ", token);
            if (!Utils.validateString(token)) {
                SimiManager.getIntance().showNotify(
                        "The requested Payment Method is not available.");
                SimiManager.getIntance().backToHomeFragment();
                return;
            }

            String client_id = paymentMethod.getData("client_id");
            intent.putExtra("EXTRA_CLIENT_ID", client_id);
            Log.e("BrainTree CLIENT_ID ", client_id);
            // if (Utils.validateString(client_id)) {
            // SimiManager.getIntance().showNotify(
            // "The requested Payment Method is not available.");
            // return;
            // }

            String is_sandbox = paymentMethod.getData("is_sandbox");
            intent.putExtra("EXTRA_SANDBOX", is_sandbox);
            Log.e("BrainTree IS_SANDBOX ", is_sandbox);
            if (!Utils.validateString(is_sandbox)) {
                SimiManager.getIntance().showNotify(
                        "The requested Payment Method is not available.");
                SimiManager.getIntance().backToHomeFragment();
                return;
            }

            intent.putExtra("EXTRA_PRICE", total_price);
            Log.e("BrainTree EXTRA_PRICE", total_price);

//            String bnCode = paymentMethod.getData("bncode");
//            intent.putExtra("EXTRA_BNCODE", bnCode);
//            Log.e("BrainTree BNCODE ", bnCode);
//            if (!Utils.validateString(bnCode)) {
//                SimiManager.getIntance().showNotify(
//                        "The requested Payment Method is not available.");
//                SimiManager.getIntance().backToHomeFragment();
//                return;
//            }

            intent.putExtra("EXTRA_INVOICE_NUMBER", invoice_number);
            Log.e("BrainTree INVOICE NUMBER ", invoice_number);
            if (!Utils.validateString(invoice_number)) {
                SimiManager.getIntance().showNotify(
                        "The requested Payment Method is not available.");
                SimiManager.getIntance().backToHomeFragment();
                return;
            }
            this.context.startActivity(intent);
        } else {
            return;
        }
    }

}
