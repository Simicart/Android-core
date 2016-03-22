package com.trueplus.simicart.braintreelibrary;

import android.app.Activity;
import android.content.Intent;

import com.trueplus.simicart.braintreelibrary.exceptions.BraintreeException;
import com.trueplus.simicart.braintreelibrary.exceptions.ErrorWithResponse;
import com.trueplus.simicart.braintreelibrary.interfaces.ConfigurationListener;
import com.trueplus.simicart.braintreelibrary.interfaces.HttpResponseCallback;
import com.trueplus.simicart.braintreelibrary.interfaces.PaymentMethodNonceCallback;
import com.trueplus.simicart.braintreelibrary.models.CardBuilder;
import com.trueplus.simicart.braintreelibrary.models.Configuration;
import com.trueplus.simicart.braintreelibrary.models.PaymentMethodNonce;
import com.trueplus.simicart.braintreelibrary.models.ThreeDSecureAuthenticationResponse;
import com.trueplus.simicart.braintreelibrary.models.ThreeDSecureLookup;
import com.trueplus.simicart.braintreelibrary.threedsecure.ThreeDSecureWebViewActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class ThreeDSecure {

    protected static final int THREE_D_SECURE_REQUEST_CODE = 13487;

    /**
     * 3D Secure is a protocol that enables cardholders and issuers to add a layer of security
     * to e-commerce transactions via password entry at checkout.
     *
     * One of the primary reasons to use 3D Secure is to benefit from a shift in liability from the
     * merchant to the issuer, which may result in interchange savings. Please read our online
     * documentation (<a href="https://developers.braintreepayments.com">https://developers.braintreepayments.com</a>)
     * for a full explanation of 3D Secure.
     *
     * Verification is associated with a transaction amount and your merchant account. To specify a
     * different merchant account (or, in turn, currency), you will need to specify the merchant
     * account id when generating a client token
     * (See <a href="https://developers.braintreepayments.com/android/sdk/overview/generate-client-token">https://developers.braintreepayments.com/android/sdk/overview/generate-client-token</a>).
     *
     * During lookup the original payment method nonce is consumed and a new one is returned,
     * which points to the original payment method, as well as the 3D Secure verification.
     * Transactions created with this nonce will be 3D Secure, and benefit from the appropriate
     * liability shift if authentication is successful or fail with a 3D Secure failure.
     *
     * @param fragment the {@link com.braintreepayments.api.BraintreeFragment} backing the http request. This fragment will
     *                  also be responsible for handling callbacks to it's listeners
     * @param cardBuilder The cardBuilder created from raw details. Will be tokenized before
     *                    the 3D Secure verification if performed.
     * @param amount The amount of the transaction in the current merchant account's currency
     */
    public static void performVerification(final BraintreeFragment fragment, final CardBuilder cardBuilder, final String amount) {
        TokenizationClient.tokenize(fragment, cardBuilder, new PaymentMethodNonceCallback() {
            @Override
            public void success(PaymentMethodNonce paymentMethodNonce) {
                performVerification(fragment, paymentMethodNonce.getNonce(), amount);
            }

            @Override
            public void failure(Exception exception) {
                fragment.postCallback(exception);
            }
        });
    }

    /**
     * 3D Secure is a protocol that enables cardholders and issuers to add a layer of security
     * to e-commerce transactions via password entry at checkout.
     *
     * One of the primary reasons to use 3D Secure is to benefit from a shift in liability from the
     * merchant to the issuer, which may result in interchange savings. Please read our online
     * documentation (<a href="https://developers.braintreepayments.com">https://developers.braintreepayments.com</a>)
     * for a full explanation of 3D Secure.
     *
     * Verification is associated with a transaction amount and your merchant account. To specify a
     * different merchant account (or, in turn, currency), you will need to specify the merchant
     * account id when generating a client token
     * (See <a href="https://developers.braintreepayments.com/android/sdk/overview/generate-client-token">https://developers.braintreepayments.com/android/sdk/overview/generate-client-token</a>).
     *
     * During lookup the original payment method nonce is consumed and a new one is returned,
     * which points to the original payment method, as well as the 3D Secure verification.
     * Transactions created with this nonce will be 3D Secure, and benefit from the appropriate
     * liability shift if authentication is successful or fail with a 3D Secure failure.
     *
     * @param fragment the {@link com.braintreepayments.api.BraintreeFragment} backing the http request. This fragment will
     *                  also be responsible for handling callbacks to it's listeners
     * @param nonce The nonce that represents a card to perform a 3D Secure verification against.
     * @param amount The amount of the transaction in the current merchant account's currency.
     */
    public static void performVerification(final BraintreeFragment fragment, final String nonce,
            final String amount) {
        fragment.waitForConfiguration(new ConfigurationListener() {
            @Override
            public void onConfigurationFetched(Configuration configuration) {
                try {
                    JSONObject params = new JSONObject()
                            .put("merchantAccountId", configuration.getMerchantAccountId())
                            .put("amount", amount);

                    fragment.getHttpClient().post(TokenizationClient.versionedPath(
                                    TokenizationClient.PAYMENT_METHOD_ENDPOINT + "/" + nonce +
                                            "/three_d_secure/lookup"),
                            params.toString(), new HttpResponseCallback() {
                                @Override
                                public void success(String responseBody) {
                                    try {
                                        ThreeDSecureLookup threeDSecureLookup =
                                                ThreeDSecureLookup.fromJson(responseBody);
                                        if (threeDSecureLookup.getAcsUrl() != null) {
                                            Intent intent = new Intent(fragment.getApplicationContext(),
                                                            ThreeDSecureWebViewActivity.class)
                                                            .putExtra(
                                                                    ThreeDSecureWebViewActivity.EXTRA_THREE_D_SECURE_LOOKUP,
                                                                    threeDSecureLookup);
                                            fragment.startActivityForResult(intent,
                                                    THREE_D_SECURE_REQUEST_CODE);
                                        } else {
                                            fragment.postCallback(threeDSecureLookup.getCardNonce());
                                        }
                                    } catch (JSONException e) {
                                        fragment.postCallback(e);
                                    }
                                }

                                @Override
                                public void failure(Exception exception) {
                                    fragment.postCallback(exception);
                                }
                            });
                } catch (JSONException e) {
                    fragment.postCallback(e);
                }
            }
        });
    }

    protected static void onActivityResult(BraintreeFragment fragment, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            ThreeDSecureAuthenticationResponse authenticationResponse =
                    data.getParcelableExtra(ThreeDSecureWebViewActivity.EXTRA_THREE_D_SECURE_RESULT);
            if (authenticationResponse.isSuccess()) {
                fragment.postCallback(authenticationResponse.getCardNonce());
            } else if (authenticationResponse.getException() != null) {
                fragment.postCallback(new BraintreeException(authenticationResponse.getException()));
            } else {
                fragment.postCallback(new ErrorWithResponse(422, authenticationResponse.getErrors()));
            }
        }
    }
}
