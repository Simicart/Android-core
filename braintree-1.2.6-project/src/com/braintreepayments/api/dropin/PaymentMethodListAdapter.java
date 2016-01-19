package com.braintreepayments.api.dropin;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.braintreepayments.api.dropin.view.PaymentMethodView;
import com.braintreepayments.api.models.PaymentMethod;

import java.util.List;

/**
 * {@link android.widget.BaseAdapter} to handle a {@link java.util.List} of {@link com.braintreepayments.api.models.PaymentMethod}s.
 * Used for listing existing {@link com.braintreepayments.api.models.PaymentMethod}s for a customer to choose.
 */
public class PaymentMethodListAdapter extends BaseAdapter implements
        DialogInterface.OnClickListener {

    private Context mContext;
    private SelectPaymentMethodViewController mViewController;
    private List<PaymentMethod> mPaymentMethods;

    public PaymentMethodListAdapter(Context context, SelectPaymentMethodViewController viewController, List<PaymentMethod> paymentMethods) {
        mContext = context;
        mViewController = viewController;
        mPaymentMethods = paymentMethods;
    }

    @Override
    public int getCount() {
        return mPaymentMethods.size();
    }

    @Override
    public Object getItem(int position) {
        return mPaymentMethods.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PaymentMethodView paymentMethodView = new PaymentMethodView(mContext);
        paymentMethodView.setPaymentMethodDetails(mPaymentMethods.get(position));
        return paymentMethodView;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        mViewController.onPaymentMethodSelected(which);
    }
}
