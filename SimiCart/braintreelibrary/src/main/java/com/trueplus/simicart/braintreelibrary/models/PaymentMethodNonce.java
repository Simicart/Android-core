package com.trueplus.simicart.braintreelibrary.models;

import android.os.Parcelable;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base class representing a method of payment for a customer. {@link PaymentMethodNonce} represents the
 * common interface of all payment method nonces, and can be handled by a server interchangeably.
 */
public abstract class PaymentMethodNonce implements Parcelable {

    private static final String PAYMENT_METHOD_NONCE_COLLECTION_KEY = "paymentMethods";
    private static final String PAYMENT_METHOD_TYPE_KEY = "type";
    private static final String PAYMENT_METHOD_NONCE_KEY = "nonce";
    private static final String DESCRIPTION_KEY = "description";

    protected String mNonce;
    protected String mDescription;

    protected static JSONObject getJsonObjectForType(String apiResourceKey, String response)
            throws JSONException {
        return new JSONObject(response)
                .getJSONArray(apiResourceKey)
                .getJSONObject(0);
    }

    @CallSuper
    protected void fromJson(JSONObject json) throws JSONException {
        mNonce = json.getString(PAYMENT_METHOD_NONCE_KEY);
        mDescription = json.getString(DESCRIPTION_KEY);
    }

    /**
     * @return The nonce generated for this payment method by the Braintree gateway. The nonce will
     *          represent this PaymentMethod for the purposes of creating transactions and other monetary
     *          actions.
     */
    public String getNonce() {
        return mNonce;
    }

    /**
     * @return The description of this PaymentMethod for displaying to a customer, e.g. 'Visa ending in...'
     */
    public String getDescription() {
        return mDescription;
    }

    /**
     * @return The type of this PaymentMethod for displaying to a customer, e.g. 'Visa'. Can be used
     *          for displaying appropriate logos, etc.
     */
    public abstract String getTypeLabel();

    /**
     * Parses a response from the Braintree gateway for a list of payment method nonces.
     *
     * @param jsonBody Json-formatted String containing a list of {@link PaymentMethodNonce}s
     * @return List of {@link PaymentMethodNonce}s contained in jsonBody
     * @throws JSONException
     */
    public static List<PaymentMethodNonce> parsePaymentMethodNonces(String jsonBody)
            throws JSONException {
        JSONArray paymentMethods = new JSONObject(jsonBody).getJSONArray(
                PAYMENT_METHOD_NONCE_COLLECTION_KEY);

        if (paymentMethods == null) {
            return Collections.emptyList();
        }

        List<PaymentMethodNonce> paymentMethodsNonces = new ArrayList<>();
        JSONObject json;
        PaymentMethodNonce paymentMethodNonce;
        Log.e("PaymentMethodNonce", "++" + paymentMethods.toString());
        for(int i = 0; i < paymentMethods.length(); i++) {
            json = paymentMethods.getJSONObject(i);
            paymentMethodNonce = parsePaymentMethodNonces(json,
                    json.getString(PAYMENT_METHOD_TYPE_KEY));
            if (paymentMethodNonce != null) {
                Log.e("PaymentMethodNonce", "__" + paymentMethodNonce.getNonce());
                paymentMethodsNonces.add(paymentMethodNonce);
            }
        }

        return paymentMethodsNonces;
    }

    /**
     * Parses a {@link PaymentMethodNonce} from json.
     *
     * @param json {@link String} representation of a {@link PaymentMethodNonce}.
     * @param type The {@link String} type of the {@link PaymentMethodNonce}.
     * @return {@link PaymentMethodNonce}
     * @throws JSONException
     */
    @Nullable
    public static PaymentMethodNonce parsePaymentMethodNonces(String json, String type) throws JSONException {
        Log.e("PaymentMethodNonce", "**" + json);
        return parsePaymentMethodNonces(new JSONObject(json), type);
    }

    /**
     * Parses a {@link PaymentMethodNonce} from json.
     *
     * @param json {@link JSONObject} representation of a {@link PaymentMethodNonce}.
     * @param type The {@link String} type of the {@link PaymentMethodNonce}.
     * @return {@link PaymentMethodNonce}
     * @throws JSONException
     */
    @Nullable
    public static PaymentMethodNonce parsePaymentMethodNonces(JSONObject json, String type) throws JSONException {
        switch (type) {
            case CardNonce.TYPE:
                if (json.has(CardNonce.API_RESOURCE_KEY)) {
                    return CardNonce.fromJson(json.toString());
                } else {
                    CardNonce cardNonce = new CardNonce();
                    cardNonce.fromJson(json);
                    return cardNonce;
                }
            case PayPalAccountNonce.TYPE:
                if (json.has(PayPalAccountNonce.API_RESOURCE_KEY)) {
                    return PayPalAccountNonce.fromJson(json.toString());
                } else {
                    PayPalAccountNonce payPalAccountNonce = new PayPalAccountNonce();
                    payPalAccountNonce.fromJson(json);
                    return payPalAccountNonce;
                }
            case AndroidPayCardNonce.TYPE:
                if (json.has(AndroidPayCardNonce.API_RESOURCE_KEY)) {
                    return AndroidPayCardNonce.fromJson(json.toString());
                } else {
                    AndroidPayCardNonce androidPayCardNonce = new AndroidPayCardNonce();
                    androidPayCardNonce.fromJson(json);
                    return androidPayCardNonce;
                }
            default:
                return null;
        }
    }
}
