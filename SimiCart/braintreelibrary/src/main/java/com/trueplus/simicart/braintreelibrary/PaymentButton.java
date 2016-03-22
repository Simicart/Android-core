package com.trueplus.simicart.braintreelibrary;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ViewSwitcher;
import com.paypal.android.sdk.onetouch.core.CheckoutRequest;
import com.trueplus.simicart.braintreelibrary.exceptions.InvalidArgumentException;
import com.trueplus.simicart.braintreelibrary.interfaces.BraintreeResponseListener;
import com.trueplus.simicart.braintreelibrary.interfaces.ConfigurationListener;
import com.trueplus.simicart.braintreelibrary.models.Authorization;
import com.trueplus.simicart.braintreelibrary.models.Configuration;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class PaymentButton extends Fragment implements ConfigurationListener,
        BraintreeResponseListener<Exception>, OnClickListener {

    private static final String TAG = "com.braintreepayments.api.PaymentButton";
    private static final String EXTRA_PAYMENT_REQUEST =
            "com.braintreepayments.api.EXTRA_PAYMENT_REQUEST";

    @VisibleForTesting
    BraintreeFragment mBraintreeFragment;

    private PaymentRequest mPaymentRequest;
    private ViewSwitcher mProgressViewSwitcher;
    private OnClickListener mOnClickListener;

    public PaymentButton() {}

    /**
     * Create a new instance of {@link PaymentButton} using the {@link CheckoutRequest} and add it
     * to the {@link Activity}'s {@link FragmentManager}.
     *
     * @param activity The {@link Activity} to add the {@link Fragment} to.
     * @param paymentRequest The {@link PaymentRequest} to use to configure the
     *        {@link PaymentButton}. *Note:* This {@link CheckoutRequest} must include a client key
     *        or client token.
     * @param containerViewId Optional identifier of the container this fragment is to be placed in.
     *        If 0, it will not be placed in a container.
     * @return {@link PaymentButton}
     * @throws InvalidArgumentException If the client key or tokenization key is not valid or
     * cannot be parsed.
     */
    public static PaymentButton newInstance(Activity activity, int containerViewId,
                                            PaymentRequest paymentRequest) throws InvalidArgumentException {
        FragmentManager fm = activity.getFragmentManager();

        PaymentButton paymentButton = (PaymentButton) fm.findFragmentByTag(TAG);
        if (paymentButton == null) {
            paymentButton = new PaymentButton();

            Bundle bundle = new Bundle();
            Authorization.fromString(paymentRequest.getAuthorization());
            bundle.putParcelable(EXTRA_PAYMENT_REQUEST, paymentRequest);
            paymentButton.setArguments(bundle);

            fm.beginTransaction().add(containerViewId, paymentButton, TAG).commit();
        }

        return paymentButton;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
        getTokenizationKeyFromAttributes(activity, attrs);
    }

    @TargetApi(VERSION_CODES.M)
    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
        getTokenizationKeyFromAttributes(context, attrs);
    }

    private void getTokenizationKeyFromAttributes(Context context, AttributeSet attrs) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.PaymentButtonAttributes);
        String tokenizationKey = attributes.getString(R.styleable.PaymentButtonAttributes_tokenizationKey);
        attributes.recycle();

        if (Authorization.isTokenizationKey(tokenizationKey)) {
            try {
                setPaymentRequest(new PaymentRequest().tokenizationKey(tokenizationKey));
            } catch (InvalidArgumentException ignored) {}
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            if (getArguments() != null) {
                setPaymentRequest(
                        (PaymentRequest) getArguments().getParcelable(EXTRA_PAYMENT_REQUEST));
            }
        } catch (InvalidArgumentException ignored) {}
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bt_payment_button, container, false);
        mProgressViewSwitcher =
                (ViewSwitcher) view.findViewById(R.id.bt_payment_method_view_switcher);
        showProgress(true);

        if (mPaymentRequest == null) {
            view.setVisibility(GONE);
        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        try {
            setupBraintreeFragment();
        } catch (InvalidArgumentException e) {
            setVisibility(GONE);
        }
    }

    /**
     * Initialize the {@link PaymentButton}. This method *MUST* be called if the {@link
     * PaymentButton} was adding using XML or {@link PaymentButton} will not be displayed.
     *
     * @param paymentRequest {@link PaymentRequest} containing payment method options.
     */
    public void setPaymentRequest(PaymentRequest paymentRequest)
            throws InvalidArgumentException {
        mPaymentRequest = paymentRequest;
        if (mPaymentRequest == null) {
            setVisibility(GONE);
        } else {
            setupBraintreeFragment();
        }
    }

    /**
     * Set the on click listener to be called when the {@link PaymentButton} is clicked.
     *
     * @param listener {@link OnClickListener} to be called.
     */
    public void setOnClickListener(OnClickListener listener) {
        mOnClickListener = listener;
    }

    @Override
    public void onConfigurationFetched(Configuration configuration) {
        mBraintreeFragment.setConfigurationErrorListener(null);
        setupButton(configuration);
    }

    @Override
    public void onResponse(Exception exception) {
        mBraintreeFragment.setConfigurationErrorListener(null);
        setVisibility(GONE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_paypal_button) {
            PayPal.authorizeAccount(mBraintreeFragment,
                    mPaymentRequest.getPayPalAdditionalScopes());
        } else if (v.getId() == R.id.bt_venmo_button) {
            Venmo.authorizeAccount(mBraintreeFragment);
        } else if (v.getId() == R.id.bt_android_pay_button) {
            AndroidPay.performMaskedWalletRequest(mBraintreeFragment,
                    mPaymentRequest.getAndroidPayCart(),
                    mPaymentRequest.isAndroidPayShippingAddressRequired(),
                    mPaymentRequest.isAndroidPayPhoneNumberRequired(),
                    mPaymentRequest.getAndroidPayRequestCode());
        }

        if (mOnClickListener != null) {
            mOnClickListener.onClick(getView());
        }
    }

    void setupButton(Configuration configuration) {
        View view = getView();
        if (view == null) {
            setVisibility(GONE);
            return;
        }

        boolean isPayPalEnabled = configuration.isPayPalEnabled();
        boolean isVenmoEnabled = configuration.getPayWithVenmo().isEnabled(mBraintreeFragment.getApplicationContext());
        boolean isAndroidPayEnabled = isAndroidPayEnabled(configuration);
        int buttonCount = 0;
        if (!isPayPalEnabled && !isVenmoEnabled && !isAndroidPayEnabled) {
            setVisibility(GONE);
        } else {
            if (isPayPalEnabled) {
                buttonCount++;
            }
            if (isVenmoEnabled) {
                buttonCount++;
            }
            if (isAndroidPayEnabled) {
                buttonCount++;
            }

            if (isPayPalEnabled) {
                enableButton(view.findViewById(R.id.bt_paypal_button), buttonCount);
            }
            if (isVenmoEnabled) {
                enableButton(view.findViewById(R.id.bt_venmo_button), buttonCount);
            }
            if (isAndroidPayEnabled) {
                enableButton(view.findViewById(R.id.bt_android_pay_button), buttonCount);
            }

            if (isPayPalEnabled && buttonCount > 1) {
                view.findViewById(R.id.bt_payment_button_divider).setVisibility(VISIBLE);
            } else if (isVenmoEnabled && buttonCount > 1) {
                view.findViewById(R.id.bt_payment_button_divider_2).setVisibility(VISIBLE);
            }
            if (buttonCount > 2) {
                view.findViewById(R.id.bt_payment_button_divider_2).setVisibility(VISIBLE);
            }

            setVisibility(VISIBLE);
            showProgress(false);
        }
    }

    private void enableButton(View view, int buttonCount) {
        view.setVisibility(VISIBLE);
        view.setOnClickListener(this);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.MATCH_PARENT, 3f / buttonCount);
        view.setLayoutParams(params);
    }

    private void setVisibility(int visibility) {
        if (getView() != null && getView().getVisibility() != visibility) {
            getView().setVisibility(visibility);
        }
    }

    private void showProgress(boolean showing) {
        if (mProgressViewSwitcher != null &&
                mProgressViewSwitcher.getDisplayedChild() != (showing ? 1 : 0)) {
            mProgressViewSwitcher.setDisplayedChild(showing ? 1 : 0);
        }
    }

    private void setupBraintreeFragment() throws InvalidArgumentException {
        if (getActivity() != null && mPaymentRequest != null && mBraintreeFragment == null) {
            mBraintreeFragment = BraintreeFragment.newInstance(getActivity(),
                    mPaymentRequest.getAuthorization());
            mBraintreeFragment.setConfigurationErrorListener(this);

            if (mBraintreeFragment.getConfiguration() != null) {
                onConfigurationFetched(mBraintreeFragment.getConfiguration());
            } else {
                mBraintreeFragment.waitForConfiguration(this);

                showProgress(true);
                setVisibility(VISIBLE);
            }
        } else if (mBraintreeFragment != null && mBraintreeFragment.getConfiguration() != null) {
            setupButton(mBraintreeFragment.getConfiguration());
        }
    }

    @VisibleForTesting
    private boolean isAndroidPayEnabled(Configuration configuration) {
        try {
            return (configuration.getAndroidPay().isEnabled(
                    mBraintreeFragment.getApplicationContext())
                    && mPaymentRequest.getAndroidPayCart() != null);
        } catch (NoClassDefFoundError e) {
            return false;
        }
    }
}
