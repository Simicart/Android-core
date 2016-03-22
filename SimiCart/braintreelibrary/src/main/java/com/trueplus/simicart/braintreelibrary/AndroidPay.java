package com.trueplus.simicart.braintreelibrary;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.wallet.Cart;
import com.google.android.gms.wallet.FullWallet;
import com.google.android.gms.wallet.FullWalletRequest;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.MaskedWalletRequest;
import com.google.android.gms.wallet.PaymentMethodTokenizationParameters;
import com.google.android.gms.wallet.PaymentMethodTokenizationType;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.android.gms.wallet.WalletConstants.CardNetwork;
import com.trueplus.simicart.braintreelibrary.BraintreeFragment;
import com.trueplus.simicart.braintreelibrary.BuildConfig;
import com.trueplus.simicart.braintreelibrary.exceptions.BraintreeException;
import com.trueplus.simicart.braintreelibrary.interfaces.ConfigurationListener;
import com.trueplus.simicart.braintreelibrary.interfaces.TokenizationParametersListener;
import com.trueplus.simicart.braintreelibrary.models.AndroidPayCardNonce;
import com.trueplus.simicart.braintreelibrary.models.AndroidPayConfiguration;
import com.trueplus.simicart.braintreelibrary.models.Configuration;
import com.trueplus.simicart.braintreelibrary.models.TokenizationKey;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Class containing Android Pay specific logic.
 */
public class AndroidPay {

    protected static final int ANDROID_PAY_MASKED_WALLET_REQUEST_CODE = 13489;
    protected static final int ANDROID_PAY_FULL_WALLET_REQUEST_CODE = 13590;

    private static final String VISA_NETWORK = "visa";
    private static final String MASTERCARD_NETWORK = "mastercard";
    private static final String AMEX_NETWORK = "amex";
    private static final String DISCOVER_NETWORK = "discover";

    /**
     * Get Braintree specific tokenization parameters for Android Pay. Useful for existing Google
     * Wallet or Android Pay integrations, or when full control over the
     * {@link MaskedWalletRequest} and
     * {@link FullWalletRequest} is required.
     *
     * {@link PaymentMethodTokenizationParameters} should be supplied to the
     * {@link MaskedWalletRequest} via
     * {@link MaskedWalletRequest.Builder#setPaymentMethodTokenizationParameters(PaymentMethodTokenizationParameters)}
     * and {@link Collection<Integer>} allowedCardNetworks should be supplied to the
     * {@link MaskedWalletRequest} via
     * {@link MaskedWalletRequest.Builder#addAllowedCardNetworks(Collection)}.
     *
     * @param fragment {@link com.braintreepayments.api.BraintreeFragment}
     * @param listener Instance of {@link TokenizationParametersListener} to receive the
     *                 {@link PaymentMethodTokenizationParameters}.
     */
    public static void getTokenizationParameters(final BraintreeFragment fragment,
            final TokenizationParametersListener listener) {
        fragment.waitForConfiguration(new ConfigurationListener() {
            @Override
            public void onConfigurationFetched(Configuration configuration) {
                listener.onResult(getTokenizationParameters(fragment),
                        getAllowedCardNetworks(fragment));
            }
        });
    }

    static PaymentMethodTokenizationParameters getTokenizationParameters(BraintreeFragment fragment) {
        PaymentMethodTokenizationParameters.Builder parameters = PaymentMethodTokenizationParameters.newBuilder()
                .setPaymentMethodTokenizationType(PaymentMethodTokenizationType.PAYMENT_GATEWAY)
                .addParameter("gateway", "braintree")
                .addParameter("braintree:merchantId", fragment.getConfiguration().getMerchantId())
                .addParameter("braintree:authorizationFingerprint",
                        fragment.getConfiguration().getAndroidPay().getGoogleAuthorizationFingerprint())
                .addParameter("braintree:apiVersion", "v1")
                .addParameter("braintree:sdkVersion", BuildConfig.VERSION_NAME);

        if (fragment.getAuthorization() instanceof TokenizationKey) {
            parameters.addParameter("braintree:clientKey", fragment.getAuthorization().toString());
        }

        return parameters.build();
    }

    static Collection<Integer> getAllowedCardNetworks(BraintreeFragment fragment) {
        Collection<Integer> allowedNetworks = new ArrayList<>();
        for (String network : fragment.getConfiguration().getAndroidPay().getSupportedNetworks()) {
            switch (network) {
                case VISA_NETWORK:
                    allowedNetworks.add(CardNetwork.VISA);
                    break;
                case MASTERCARD_NETWORK:
                    allowedNetworks.add(CardNetwork.MASTERCARD);
                    break;
                case AMEX_NETWORK:
                    allowedNetworks.add(CardNetwork.AMEX);
                    break;
                case DISCOVER_NETWORK:
                    allowedNetworks.add(CardNetwork.DISCOVER);
                    break;
            }
        }

        return allowedNetworks;
    }

    /**
     * Call this method when you've received a successful FullWallet request in your activity's
     * {@link Activity#onActivityResult(int, int, Intent)} to get an {@link AndroidPayCardNonce} from a
     * {@link FullWallet}.
     *
     * @param fragment An instance of {@link com.braintreepayments.api.BraintreeFragment}.
     * @param wallet a {@link FullWallet} from the Intent in
     *          {@link Activity#onActivityResult(int, int, Intent)}
     */
    public static void tokenize(BraintreeFragment fragment, FullWallet wallet) {
        try {
            Log.e("PaymentMethod", "__" + wallet.getPaymentMethodToken().getToken().toString());
            fragment.postCallback(
                    AndroidPayCardNonce.fromJson(wallet.getPaymentMethodToken().getToken()));
            //fragment.sendAnalyticsEvent("android-pay.nonce-received");
        } catch (JSONException e) {
            fragment.postCallback(e);
            //fragment.sendAnalyticsEvent("android-pay.failed");
        }
    }

    /**
     * Launch an Android Pay masked wallet request. This method will show the payment instrument
     * chooser to the user.
     *
     * @param fragment The current {@link com.braintreepayments.api.BraintreeFragment}.
     * @param cart The cart representation with price and optionally items.
     * @param shippingAddressRequired {@code true} if this request requires a shipping address, {@code false} otherwise.
     * @param phoneNumberRequired {@code true} if this request requires a phone number, {@code false} otherwise.
     * @param requestCode The requestCode to use with {@link Activity#startActivityForResult(Intent, int)}
     */
    static void performMaskedWalletRequest(final BraintreeFragment fragment, final @NonNull Cart cart,
            final boolean shippingAddressRequired, final boolean phoneNumberRequired, 
            final int requestCode) {
        //fragment.sendAnalyticsEvent("android-pay.selected");

        if (cart == null) {
            Log.e("AndroidPay", "cart is null");
            fragment.postCallback(new BraintreeException("Cannot pass null cart to performMaskedWalletRequest"));
            //fragment.sendAnalyticsEvent("android-pay.failed");
            return;
        }

        fragment.waitForConfiguration(new ConfigurationListener() {
            @Override
            public void onConfigurationFetched(Configuration configuration) {
                MaskedWalletRequest.Builder maskedWalletRequestBuilder =
                        MaskedWalletRequest.newBuilder()
                                .setMerchantName(getMerchantName(configuration.getAndroidPay()))
                                .setCurrencyCode("USD")
                                .setCart(cart)
                                .setEstimatedTotalPrice(cart.getTotalPrice())
                                .setShippingAddressRequired(shippingAddressRequired)
                                .setPhoneNumberRequired(phoneNumberRequired)
                                .setPaymentMethodTokenizationParameters(
                                        getTokenizationParameters(fragment))
                                .addAllowedCardNetworks(getAllowedCardNetworks(fragment));

                Wallet.Payments.loadMaskedWallet(fragment.getGoogleApiClient(),
                        maskedWalletRequestBuilder.build(), requestCode);

                //fragment.sendAnalyticsEvent("android-pay.started");
            }
        });
    }

    /**
     * Perform a full wallet request. This can only be done after a masked wallet request has been
     * made.
     *
     * @param fragment The current {@link com.braintreepayments.api.BraintreeFragment} through which the callbacks should
     *          be forwarded
     * @param cart The {@link Cart} that was used to create the MaskedWalletRequest
     * @param googleTransactionId The transaction id from the {@link MaskedWallet}.
     */
    static void performFullWalletRequest(BraintreeFragment fragment, Cart cart, String googleTransactionId) {
        FullWalletRequest.Builder fullWalletRequestBuilder = FullWalletRequest.newBuilder()
                .setCart(cart)
                .setGoogleTransactionId(googleTransactionId);

        Wallet.Payments.loadFullWallet(fragment.getGoogleApiClient(), fullWalletRequestBuilder.build(),
                ANDROID_PAY_FULL_WALLET_REQUEST_CODE);
    }

    static void onActivityResult(BraintreeFragment fragment, Cart cart, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (data.hasExtra(WalletConstants.EXTRA_MASKED_WALLET)) {
                //fragment.sendAnalyticsEvent("android-pay.authorized");
                String googleTransactionId =
                        ((MaskedWallet) data.getParcelableExtra(WalletConstants.EXTRA_MASKED_WALLET))
                        .getGoogleTransactionId();
                performFullWalletRequest(fragment, cart, googleTransactionId);
            } else if (data.hasExtra(WalletConstants.EXTRA_FULL_WALLET)) {
                tokenize(fragment,
                        (FullWallet) data.getParcelableExtra(WalletConstants.EXTRA_FULL_WALLET));
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            //fragment.sendAnalyticsEvent("android-pay.canceled");
        } else {
            //fragment.sendAnalyticsEvent("android-pay.failed");
        }
    }

    protected static int getEnvironment(AndroidPayConfiguration configuration) {
        if("production".equals(configuration.getEnvironment())) {
            return WalletConstants.ENVIRONMENT_PRODUCTION;
        } else {
            return WalletConstants.ENVIRONMENT_SANDBOX;
        }
    }

    private static String getMerchantName(AndroidPayConfiguration configuration) {
        if (configuration.getDisplayName() != null) {
            return configuration.getDisplayName();
        } else {
            return "";
        }
    }
}
