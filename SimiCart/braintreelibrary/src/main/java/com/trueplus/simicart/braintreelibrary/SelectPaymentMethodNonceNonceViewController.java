package com.trueplus.simicart.braintreelibrary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.trueplus.simicart.braintreelibrary.dropin.adapters.PaymentMethodNonceListAdapter;
import com.trueplus.simicart.braintreelibrary.dropin.view.PaymentMethodNonceView;
import com.trueplus.simicart.braintreelibrary.models.PaymentMethodNonce;

/**
 * {@link BraintreeViewController} for handling manipulation of existing payment method nonces.
 */
public class SelectPaymentMethodNonceNonceViewController extends BraintreeViewController
        implements View.OnClickListener, PaymentMethodNonceListAdapter.PaymentMethodNonceSelectedListener {

    // @formatter:off
    private static final String EXTRA_SELECTED_PAYMENT_METHOD = "com.braintreepayments.api.dropin.EXTRA_SELECTED_PAYMENT_METHOD";
    // @formatter:on

    /**
     * When adding new views, make sure to update {@link #onSaveInstanceState} and the proper
     * tests.
     */
    private PaymentMethodNonceView mPaymentMethodNonceView;
    private TextView mChangeMethodView;
    private Button mSubmitButton;

    private int mActivePaymentMethod;

    public SelectPaymentMethodNonceNonceViewController(BraintreePaymentActivity activity,
            Bundle savedInstanceState, View root, BraintreeFragment braintreeFragment,
            PaymentRequest paymentRequest) {
        super(activity, root, braintreeFragment, paymentRequest);
        mPaymentMethodNonceView = findView(R.id.bt_selected_payment_method_view);
        mPaymentMethodNonceView.setOnClickListener(this);

        mChangeMethodView = findView(R.id.bt_change_payment_method_link);
        mChangeMethodView.setOnClickListener(this);

        mSubmitButton = findView(R.id.bt_select_payment_method_submit_button);
        mSubmitButton.setOnClickListener(this);
        mSubmitButton.setText(getSubmitButtonText());

        if (savedInstanceState.containsKey(EXTRA_SELECTED_PAYMENT_METHOD)) {
            mActivePaymentMethod = savedInstanceState.getInt(EXTRA_SELECTED_PAYMENT_METHOD);
        } else {
            mActivePaymentMethod = 0;
        }
        setupPaymentMethod();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(EXTRA_SELECTED_PAYMENT_METHOD, mActivePaymentMethod);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mPaymentMethodNonceView.getId()) {
            if (mBraintreeFragment.getCachedPaymentMethodNonces().size() > 1) {
                showPaymentMethodListDialog();
            }
        } else if (v.getId() == mChangeMethodView.getId()) {
            if (mBraintreeFragment.getCachedPaymentMethodNonces().size() == 1) {
                launchFormView();
            } else {
                showPaymentMethodListDialog();
            }
        } else if (v.getId() == mSubmitButton.getId()) {
            mSubmitButton.setEnabled(false);
            mActivity.finalizeSelection(getActivePaymentMethod());
        }
    }

    protected void setupPaymentMethod() {
        mPaymentMethodNonceView.setPaymentMethodNonceDetails(getActivePaymentMethod());

        TextView link = findView(R.id.bt_change_payment_method_link);
        if(mBraintreeFragment.getCachedPaymentMethodNonces().size() == 1) {
            link.setText(R.string.bt_add_payment_method);
        } else {
            link.setText(R.string.bt_change_payment_method);
        }
    }

    private void showPaymentMethodListDialog() {
        PaymentMethodNonceListAdapter paymentMethodNonceListAdapter =
                new PaymentMethodNonceListAdapter(mActivity, this, mBraintreeFragment.getCachedPaymentMethodNonces());

        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(mActivity,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar);

        new AlertDialog.Builder(contextThemeWrapper)
            .setTitle(R.string.bt_choose_payment_method)
            .setAdapter(paymentMethodNonceListAdapter, paymentMethodNonceListAdapter)
            .setPositiveButton(R.string.bt_add_new_payment_method, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    launchFormView();
                }
            })
            .show();
    }

    private void launchFormView() {
        mActivity.showAddPaymentMethodView();
    }

    @Override
    public void onPaymentMethodSelected(int index) {
        mActivePaymentMethod = index;
        setupPaymentMethod();
    }

    private PaymentMethodNonce getActivePaymentMethod() {
        return mBraintreeFragment.getCachedPaymentMethodNonces().get(mActivePaymentMethod);
    }
}
