package com.trueplus.simicart.braintreelibrary;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;

import com.braintreepayments.cardform.OnCardFormFieldFocusedListener;
import com.braintreepayments.cardform.OnCardFormSubmitListener;
import com.braintreepayments.cardform.OnCardFormValidListener;
import com.braintreepayments.cardform.view.CardForm;
import com.trueplus.simicart.braintreelibrary.dropin.view.LoadingHeader;
import com.trueplus.simicart.braintreelibrary.exceptions.BraintreeError;
import com.trueplus.simicart.braintreelibrary.exceptions.ErrorWithResponse;
import com.trueplus.simicart.braintreelibrary.exceptions.InvalidArgumentException;
import com.trueplus.simicart.braintreelibrary.exceptions.UnexpectedException;
import com.trueplus.simicart.braintreelibrary.models.CardBuilder;

/**
 * {@link BraintreeViewController} for coordinating the Add Payment Method form.
 * Responsible for managing views and form element bindings associated with adding a payment method.
 */
public class AddPaymentMethodViewController extends BraintreeViewController
        implements OnClickListener, OnCardFormSubmitListener, OnCardFormValidListener,
        OnCardFormFieldFocusedListener {

    private static final String EXTRA_FORM_IS_SUBMITTING = "com.braintreepayments.dropin.EXTRA_FORM_IS_SUBMITTING";
    private static final String EXTRA_SUBMIT_BUTTON_ENABLED = "com.braintreepayments.dropin.EXTRA_SUBMIT_BUTTON_ENABLED";
    private static final String EXTRA_FOCUS_EVENT_SENT = "com.braintreepayments.dropin.EXTRA_FOCUS_EVENT_SENT";

    private static final String INTEGRATION_METHOD = "dropin";

    /**
     * When adding new views, make sure to update {@link #onSaveInstanceState}, {@link #restoreState(Bundle)}
     * and the proper tests.
     */
    private PaymentButton mPaymentButton;
    private CardForm mCardForm;
    private View mDescription;
    private Button mSubmitButton;

    private LoadingHeader mLoadingHeader;
    private ScrollView mScrollView;

    private boolean mIsSubmitting;
    private boolean mFocusEventSent;

    public AddPaymentMethodViewController(BraintreePaymentActivity activity,
                                          Bundle savedInstanceState, View root, BraintreeFragment braintreeFragment,
                                          PaymentRequest paymentRequest) {
        super(activity, root, braintreeFragment, paymentRequest);
        mIsSubmitting = false;

        initViews();
        restoreState(savedInstanceState);
    }

    private void initViews() {
        mLoadingHeader = findView(R.id.bt_header_container);
        mScrollView = findView(R.id.bt_form_scroll_container);
        mDescription = findView(R.id.bt_description_container);
        mPaymentButton = (PaymentButton) mActivity.getFragmentManager()
                .findFragmentById(R.id.bt_payment_button);
        mCardForm = findView(R.id.bt_card_form);
        mSubmitButton = findView(R.id.bt_card_form_submit_button);

        mPaymentButton.setOnClickListener(this);

        mPaymentRequest.androidPayRequestCode(AndroidPay.ANDROID_PAY_MASKED_WALLET_REQUEST_CODE);
        try {
            mPaymentButton.setPaymentRequest(mPaymentRequest);
        } catch (InvalidArgumentException ignored) {
            // already checked in BraintreePaymentActivity
        }

        mCardForm.setRequiredFields(mActivity, true, true,
                mBraintreeFragment.getConfiguration().isCvvChallengePresent(),
                mBraintreeFragment.getConfiguration().isPostalCodeChallengePresent(),
                getCustomizedCallToAction());
        mCardForm.setOnCardFormValidListener(this);
        mCardForm.setOnCardFormSubmitListener(this);
        mCardForm.setOnFormFieldFocusedListener(this);

        mSubmitButton.setOnClickListener(this);
        mSubmitButton.setText(getSubmitButtonText());
    }

    private void restoreState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(EXTRA_FORM_IS_SUBMITTING)) {
            mIsSubmitting = savedInstanceState.getBoolean(EXTRA_FORM_IS_SUBMITTING);
            if (mIsSubmitting) {
                setUIForSubmit();
            }
        }

        if (savedInstanceState.containsKey(EXTRA_SUBMIT_BUTTON_ENABLED)) {
            mSubmitButton.setEnabled(savedInstanceState.getBoolean(EXTRA_SUBMIT_BUTTON_ENABLED));
        }

        mFocusEventSent = savedInstanceState.getBoolean(EXTRA_FOCUS_EVENT_SENT);

        if (mCardForm.isValid()) {
            setEnabledSubmitButtonStyle();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(EXTRA_FORM_IS_SUBMITTING, mIsSubmitting);
        outState.putBoolean(EXTRA_SUBMIT_BUTTON_ENABLED, mSubmitButton.isEnabled());
        outState.putBoolean(EXTRA_FOCUS_EVENT_SENT, mFocusEventSent);
    }

    @Override
    public void onClick(View v) {
        if (v == mPaymentButton.getView()) {
            mIsSubmitting = true;
            mActivity.showLoadingView();
        } else if (v == mSubmitButton) {
            if (mCardForm.isValid()) {
                startSubmit();
                Card.tokenize(mBraintreeFragment, getCardBuilder());
                //mBraintreeFragment.sendAnalyticsEvent("card.form.submitted.succeeded");
            } else {
                mCardForm.validate();
                setDisabledSubmitButtonStyle();
                //mBraintreeFragment.sendAnalyticsEvent("card.form.submitted.failed");
            }
        }
    }

    @Override
    public void onCardFormValid(boolean valid) {
        if(valid) {
            setEnabledSubmitButtonStyle();
        } else {
            setDisabledSubmitButtonStyle();
        }
    }

    @Override
    public void onCardFormSubmit() {
        mSubmitButton.performClick();
    }

    @Override
    public void onCardFormFieldFocused(final View field) {
        if (!mFocusEventSent) {
            //mBraintreeFragment.sendAnalyticsEvent("card.form.focused");
            mFocusEventSent = true;
        }
    }

    private CardBuilder getCardBuilder() {
        CardBuilder cardBuilder = new CardBuilder()
                .cardNumber(mCardForm.getCardNumber())
                .expirationMonth(mCardForm.getExpirationMonth())
                .expirationYear(mCardForm.getExpirationYear())
                .integration(INTEGRATION_METHOD);

        if (mBraintreeFragment.getConfiguration().isCvvChallengePresent()) {
            cardBuilder.cvv(mCardForm.getCvv());
        }
        if (mBraintreeFragment.getConfiguration().isPostalCodeChallengePresent()) {
            cardBuilder.postalCode(mCardForm.getPostalCode());
        }

        return cardBuilder;
    }

    public void setErrors(ErrorWithResponse error) {
        endSubmit();

        BraintreeError cardErrors = error.errorFor("creditCard");
        if(cardErrors != null) {
            //mBraintreeFragment.sendAnalyticsEvent("add-card.failed");

            if(cardErrors.errorFor("number") != null) {
                mCardForm.setCardNumberError();
            }

            if(cardErrors.errorFor("expirationYear") != null ||
                    cardErrors.errorFor("expirationMonth") != null ||
                    cardErrors.errorFor("expirationDate") != null ) {
                mCardForm.setExpirationError();
            }

            if (cardErrors.errorFor("cvv") != null) {
                mCardForm.setCvvError();
            }

            if(cardErrors.errorFor("billingAddress") != null) {
                mCardForm.setPostalCodeError();
            }

            showErrorUI();
        } else {
            mActivity.onError(new UnexpectedException(error.getMessage()));
        }
    }

    private void showErrorUI() {
        mLoadingHeader.setError(mActivity.getString(R.string.bt_invalid_card));
    }

    protected boolean isSubmitting() {
        return mIsSubmitting;
    }

    protected void endSubmit() {
        setDisabledSubmitButtonStyle();
        mCardForm.setEnabled(true);
        mSubmitButton.setEnabled(true);
        mIsSubmitting = false;
    }

    private void startSubmit() {
        mCardForm.closeSoftKeyboard();
        mIsSubmitting = true;
        setUIForSubmit();
    }

    private void setUIForSubmit() {
        mCardForm.setEnabled(false);
        mSubmitButton.setEnabled(false);

        mDescription.setVisibility(View.GONE);
        mLoadingHeader.setLoading();
    }

    protected void showSuccess() {
        mLoadingHeader.setSuccessful();
    }

    private void setEnabledSubmitButtonStyle() {
        mSubmitButton.setBackgroundResource(R.drawable.bt_submit_button_background);
    }

    private void setDisabledSubmitButtonStyle() {
        mSubmitButton.setBackgroundResource(R.color.bt_button_disabled_color);
    }
}
