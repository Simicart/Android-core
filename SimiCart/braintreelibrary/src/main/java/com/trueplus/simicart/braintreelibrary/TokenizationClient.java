package com.trueplus.simicart.braintreelibrary;

import android.util.Log;

import com.trueplus.simicart.braintreelibrary.interfaces.ConfigurationListener;
import com.trueplus.simicart.braintreelibrary.interfaces.HttpResponseCallback;
import com.trueplus.simicart.braintreelibrary.interfaces.PaymentMethodNonceCallback;
import com.trueplus.simicart.braintreelibrary.models.Configuration;
import com.trueplus.simicart.braintreelibrary.models.PaymentMethodBuilder;
import com.trueplus.simicart.braintreelibrary.models.PaymentMethodNonce;

import org.json.JSONException;

import java.util.List;

class TokenizationClient {

    static final String PAYMENT_METHOD_ENDPOINT = "payment_methods";

    /**
     * Retrieves the current list of {@link PaymentMethodNonce} for the current customer.
     * <p/>
     * When finished, the {@link List} of {@link PaymentMethodNonce}s will be sent to {@link
     * PaymentMethodNoncesUpdatedListener#onPaymentMethodNoncesUpdated(List)}.
     *
     * @param fragment {@link BraintreeFragment}
     */
    static void getPaymentMethodNonces(final BraintreeFragment fragment) {
        fragment.waitForConfiguration(new ConfigurationListener() {
            @Override
            public void onConfigurationFetched(Configuration configuration) {
                fragment.getHttpClient().get(versionedPath(PAYMENT_METHOD_ENDPOINT),
                        new HttpResponseCallback() {
                            @Override
                            public void success(String responseBody) {
                                try {
                                    Log.e("payment method", "++" + responseBody);
                                    List<PaymentMethodNonce> paymentMethodNonces =
                                            PaymentMethodNonce.parsePaymentMethodNonces(
                                                    responseBody);

                                    fragment.postCallback(paymentMethodNonces);
                                } catch (JSONException e) {
                                    fragment.postCallback(e);
                                }
                            }

                            @Override
                            public void failure(Exception exception) {
                                fragment.postCallback(exception);
                            }
                        });
            }
        });
    }

    /**
     * Create a {@link PaymentMethodNonce} in the Braintree Gateway.
     * <p/>
     * On completion, returns the {@link PaymentMethodNonce} to {@link PaymentMethodNonceCallback}.
     * <p/>
     * If creation fails validation, {@link com.braintreepayments.api.interfaces.BraintreeErrorListener#onError(Exception)}
     * will be called with the resulting {@link ErrorWithResponse}.
     * <p/>
     * If an error not due to validation (server error, network issue, etc.) occurs, {@link
     * com.braintreepayments.api.interfaces.BraintreeErrorListener#onError(Exception)} (Throwable)}
     * will be called with the {@link Exception} that occurred.
     *
     * @param paymentMethodBuilder {@link PaymentMethodBuilder} for the {@link PaymentMethodNonce}
     *        to be created.
     */
    static void tokenize(final BraintreeFragment fragment,
            final PaymentMethodBuilder paymentMethodBuilder,
            final PaymentMethodNonceCallback callback) {
        fragment.waitForConfiguration(new ConfigurationListener() {
            @Override
            public void onConfigurationFetched(Configuration configuration) {
                fragment.getHttpClient().post(TokenizationClient.versionedPath(
                                TokenizationClient.PAYMENT_METHOD_ENDPOINT + "/" +
                                        paymentMethodBuilder.getApiPath()),
                        paymentMethodBuilder.build(), new HttpResponseCallback() {
                            @Override
                            public void success(String responseBody) {
                                try {
                                    PaymentMethodNonce paymentMethodNonce =
                                            PaymentMethodNonce.parsePaymentMethodNonces(
                                                    responseBody,
                                                    paymentMethodBuilder
                                                            .getResponsePaymentMethodType());
                                    Log.e("TokenizationClient", "++" + paymentMethodNonce.getNonce());
                                    callback.success(paymentMethodNonce);
                                } catch (JSONException e) {
                                    callback.failure(e);
                                }
                            }

                            @Override
                            public void failure(Exception exception) {
                                callback.failure(exception);
                            }
                        });
            }
        });
    }

    static String versionedPath(String path) {
        return "/v1/" + path;
    }
}
