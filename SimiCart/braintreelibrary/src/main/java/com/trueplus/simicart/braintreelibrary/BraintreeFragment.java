package com.trueplus.simicart.braintreelibrary;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.trueplus.simicart.braintreelibrary.exceptions.GoogleApiClientException;
import com.trueplus.simicart.braintreelibrary.exceptions.InvalidArgumentException;
import com.trueplus.simicart.braintreelibrary.interfaces.BraintreeCancelListener;
import com.trueplus.simicart.braintreelibrary.interfaces.BraintreeErrorListener;
import com.trueplus.simicart.braintreelibrary.interfaces.BraintreeListener;
import com.trueplus.simicart.braintreelibrary.interfaces.BraintreeResponseListener;
import com.trueplus.simicart.braintreelibrary.interfaces.ConfigurationListener;
import com.trueplus.simicart.braintreelibrary.interfaces.PaymentMethodNonceCreatedListener;
import com.trueplus.simicart.braintreelibrary.interfaces.PaymentMethodNoncesUpdatedListener;
import com.trueplus.simicart.braintreelibrary.interfaces.QueuedCallback;
import com.trueplus.simicart.braintreelibrary.internal.BraintreeHttpClient;
import com.trueplus.simicart.braintreelibrary.models.Authorization;
import com.trueplus.simicart.braintreelibrary.models.Configuration;
import com.trueplus.simicart.braintreelibrary.models.PaymentMethodNonce;
import com.trueplus.simicart.braintreelibrary.models.TokenizationKey;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

/**
 * Core Braintree class that handles network requests and managing callbacks.
 */
public class BraintreeFragment extends Fragment {

    public static final String TAG = "com.braintreepayments.api.BraintreeFragment";

    private static final String EXTRA_AUTHORIZATION_TOKEN =
            "com.braintreepayments.api.EXTRA_AUTHORIZATION_TOKEN";
    private static final String EXTRA_INTEGRATION_TYPE =
            "com.braintreepayments.api.EXTRA_INTEGRATION_TYPE";

    @VisibleForTesting
    protected String mIntegrationType;
    @VisibleForTesting
    protected BraintreeHttpClient mHttpClient;
    @VisibleForTesting
    protected GoogleApiClient mGoogleApiClient;
    @VisibleForTesting
    protected Configuration mConfiguration;

    private Context mContext;
    private Authorization mAuthorization;
    private Queue<QueuedCallback> mCallbackQueue = new ArrayDeque<>();
    private List<PaymentMethodNonce> mCachedPaymentMethodNonces = new ArrayList<>();
    private boolean mHasFetchedPaymentMethodNonces = false;
    private boolean mIsBrowserSwitching = false;

    private ConfigurationListener mConfigurationListener;
    private BraintreeResponseListener<Exception> mConfigurationErrorListener;
    private BraintreeCancelListener mCancelListener;
    private PaymentMethodNoncesUpdatedListener mPaymentMethodNoncesUpdatedListener;
    private PaymentMethodNonceCreatedListener mPaymentMethodNonceCreatedListener;
    private BraintreeErrorListener mErrorListener;

    public BraintreeFragment() {}

    /**
     * Create a new instance of {@link BraintreeFragment} using the client token and add it to the
     * {@link Activity}'s {@link FragmentManager}.
     *
     * @param activity The {@link Activity} to add the {@link Fragment} to.
     * @param authorization The tokenization key or client token to use.
     * @return {@link BraintreeFragment}
     * @throws InvalidArgumentException If the tokenization key or client token is not valid or cannot be
     *         parsed.
     */
    public static BraintreeFragment newInstance(Activity activity, String authorization)
            throws InvalidArgumentException {
        FragmentManager fm = activity.getFragmentManager();

        String integrationType = "custom";
        try {
            if (Class.forName("com.braintreepayments.api.BraintreePaymentActivity")
                    .isInstance(activity)) {
                integrationType = "dropin";
            }
        } catch (ClassNotFoundException ignored) {}

        BraintreeFragment braintreeFragment = (BraintreeFragment) fm.findFragmentByTag(TAG);
        if (braintreeFragment == null) {
            braintreeFragment = new BraintreeFragment();
            Bundle bundle = new Bundle();

            try {
                Authorization auth = Authorization.fromString(authorization);
                bundle.putParcelable(EXTRA_AUTHORIZATION_TOKEN, auth);
            } catch (InvalidArgumentException e) {
                throw new InvalidArgumentException("Tokenization Key or client token was invalid.");
            }

            bundle.putString(EXTRA_INTEGRATION_TYPE, integrationType);
            braintreeFragment.setArguments(bundle);
            fm.beginTransaction().add(braintreeFragment, TAG).commit();
        }

        return braintreeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mContext = getActivity().getApplicationContext();
        mIntegrationType = getArguments().getString(EXTRA_INTEGRATION_TYPE);
        mAuthorization = getArguments().getParcelable(EXTRA_AUTHORIZATION_TOKEN);

        if (mHttpClient == null) {
            mHttpClient = new BraintreeHttpClient(mAuthorization);
        }

        if (mAuthorization instanceof TokenizationKey) {
            //sendAnalyticsEvent("started.client-key");
        } else {
            //sendAnalyticsEvent("started.client-token");
        }

        fetchConfiguration();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getActivity() instanceof BraintreeListener) {
            addListener((BraintreeListener) getActivity());
        }

        flushCallbacks();

        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected() &&
                !mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.connect();
        }

        if (mIsBrowserSwitching) {
            onActivityResult(PayPal.PAYPAL_REQUEST_CODE, Activity.RESULT_FIRST_USER,
                    BraintreeBrowserSwitchActivity.sLastBrowserSwitchResponse);

            BraintreeBrowserSwitchActivity.sLastBrowserSwitchResponse = null;
            mIsBrowserSwitching = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        //AnalyticsManager.flushEvents(this);

        if (getActivity() instanceof BraintreeListener) {
            removeListener((BraintreeListener) getActivity());
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void startActivity(Intent intent) {
        if (intent.hasExtra(BraintreeBrowserSwitchActivity.EXTRA_BROWSER_SWITCH)) {
            BraintreeBrowserSwitchActivity.sLastBrowserSwitchResponse = null;
            mIsBrowserSwitching = true;
            getActivity().startActivity(intent);
        } else {
            super.startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PayPal.PAYPAL_REQUEST_CODE:
                PayPal.onActivityResult(this, data);
                break;
            case ThreeDSecure.THREE_D_SECURE_REQUEST_CODE:
                ThreeDSecure.onActivityResult(this, resultCode, data);
                break;
            case Venmo.VENMO_REQUEST_CODE:
                Venmo.onActivityResult(this, resultCode, data);
                break;
        }

        if (resultCode == Activity.RESULT_CANCELED) {
            postCancelCallback(requestCode);
        }
    }

    /**
     * Adds a listener.
     *
     * @param listener the listener to add.
     */
    public <T extends BraintreeListener> void addListener(T listener) {
        if (listener instanceof ConfigurationListener) {
            mConfigurationListener = (ConfigurationListener) listener;
        }

        if (listener instanceof BraintreeCancelListener) {
            mCancelListener = (BraintreeCancelListener) listener;
        }

        if (listener instanceof PaymentMethodNoncesUpdatedListener) {
            mPaymentMethodNoncesUpdatedListener = (PaymentMethodNoncesUpdatedListener) listener;
        }

        if (listener instanceof PaymentMethodNonceCreatedListener) {
            mPaymentMethodNonceCreatedListener = (PaymentMethodNonceCreatedListener) listener;
        }

        if (listener instanceof BraintreeErrorListener) {
            mErrorListener = (BraintreeErrorListener) listener;
        }

        flushCallbacks();
    }

    /**
     * Removes a previously added listener.
     *
     * @param listener the listener to remove.
     */
    public <T extends BraintreeListener> void removeListener(T listener) {
        if (listener instanceof ConfigurationListener) {
            mConfigurationListener = null;
        }

        if (listener instanceof BraintreeCancelListener) {
            mCancelListener = null;
        }

        if (listener instanceof PaymentMethodNoncesUpdatedListener) {
            mPaymentMethodNoncesUpdatedListener = null;
        }

        if (listener instanceof PaymentMethodNonceCreatedListener) {
            mPaymentMethodNonceCreatedListener = null;
        }

        if (listener instanceof BraintreeErrorListener) {
            mErrorListener = null;
        }
    }

    protected void sendAnalyticsEvent(final String eventFragment) {
        //AnalyticsManager.sendRequest(this, mIntegrationType, eventFragment);
    }

    protected void postCancelCallback(final int requestCode) {
        postOrQueueCallback(new QueuedCallback() {
            @Override
            public boolean shouldRun() {
                return mCancelListener != null;
            }

            @Override
            public void run() {
                mCancelListener.onCancel(requestCode);
            }
        });
    }

    protected void postCallback(final PaymentMethodNonce paymentMethodNonce) {
        mCachedPaymentMethodNonces.add(0, paymentMethodNonce);
        postOrQueueCallback(new QueuedCallback() {
            @Override
            public boolean shouldRun() {
                return mPaymentMethodNonceCreatedListener != null;
            }

            @Override
            public void run() {
                Log.e("BraintreeFragment", "++" + paymentMethodNonce.getNonce());
                mPaymentMethodNonceCreatedListener.onPaymentMethodNonceCreated(paymentMethodNonce);
            }
        });
    }

    protected void postCallback(final List<PaymentMethodNonce> paymentMethodNonceList) {
        mCachedPaymentMethodNonces = paymentMethodNonceList;
        mHasFetchedPaymentMethodNonces = true;
        postOrQueueCallback(new QueuedCallback() {
            @Override
            public boolean shouldRun() {
                return mPaymentMethodNoncesUpdatedListener != null;
            }

            @Override
            public void run() {
                mPaymentMethodNoncesUpdatedListener.onPaymentMethodNoncesUpdated(
                        paymentMethodNonceList);
            }
        });
    }

    protected void postCallback(final Exception error) {
        postOrQueueCallback(new QueuedCallback() {
            @Override
            public boolean shouldRun() {
                return mErrorListener != null;
            }

            @Override
            public void run() {
                mErrorListener.onError(error);
            }
        });
    }

    @VisibleForTesting
    protected void postOrQueueCallback(QueuedCallback callback) {
        if (!callback.shouldRun()) {
            mCallbackQueue.add(callback);
        } else {
            callback.run();
        }
    }

    @VisibleForTesting
    protected void flushCallbacks() {
        Queue<QueuedCallback> queue = new ArrayDeque<>();
        queue.addAll(mCallbackQueue);
        for (QueuedCallback callback : queue) {
            if (callback.shouldRun()) {
                callback.run();
                mCallbackQueue.remove(callback);
            }
        }
    }

    @VisibleForTesting
    protected void fetchConfiguration() {
        ConfigurationManager.getConfiguration(this, new ConfigurationListener() {
            @Override
            public void onConfigurationFetched(Configuration configuration) {
                setConfiguration(configuration);
                postOrQueueCallback(new QueuedCallback() {
                    @Override
                    public boolean shouldRun() {
                        return mConfigurationListener != null;
                    }

                    @Override
                    public void run() {
                        mConfigurationListener.onConfigurationFetched(getConfiguration());
                    }
                });
                flushCallbacks();
            }
        }, new BraintreeResponseListener<Exception>() {
            @Override
            public void onResponse(final Exception e) {
                postCallback(e);
                postOrQueueCallback(new QueuedCallback() {
                    @Override
                    public boolean shouldRun() {
                        return mConfigurationErrorListener != null;
                    }

                    @Override
                    public void run() {
                        mConfigurationErrorListener.onResponse(e);
                    }
                });
                flushCallbacks();
            }
        });
    }

    void setConfigurationErrorListener(BraintreeResponseListener<Exception> listener) {
        mConfigurationErrorListener = listener;
    }

    protected void waitForConfiguration(final ConfigurationListener listener) {
        postOrQueueCallback(new QueuedCallback() {
            @Override
            public boolean shouldRun() {
                return getConfiguration() != null;
            }

            @Override
            public void run() {
                listener.onConfigurationFetched(getConfiguration());
            }
        });
    }

    protected Authorization getAuthorization() {
        return mAuthorization;
    }

    protected Context getApplicationContext() {
        return mContext;
    }

    protected Configuration getConfiguration() {
        return mConfiguration;
    }

    protected void setConfiguration(Configuration configuration) {
        mConfiguration = configuration;
        getHttpClient().setBaseUrl(configuration.getClientApiUrl());
    }

    protected BraintreeHttpClient getHttpClient() {
        return mHttpClient;
    }

    protected boolean hasFetchedPaymentMethodNonces() {
        return mHasFetchedPaymentMethodNonces;
    }

    protected List<PaymentMethodNonce> getCachedPaymentMethodNonces() {
        return Collections.unmodifiableList(mCachedPaymentMethodNonces);
    }

    /**
     * Obtain an instance of a {@link GoogleApiClient} that is connected or connecting to be used
     * for Android Pay. This instance will be automatically disconnected in
     * {@link BraintreeFragment#onStop()} and automatically connected in
     * {@link BraintreeFragment#onResume()}.
     * <p/>
     * Connection failed and connection suspended errors will be sent to
     * {@link BraintreeErrorListener#onError(Exception)}.
     *
     * @param listener {@link BraintreeResponseListener<GoogleApiClient>} to receive the
     *                 {@link GoogleApiClient} in
     *                 {@link BraintreeResponseListener<GoogleApiClient>#onResponse(GoogleApiClient)}.
     */
    public void getGoogleApiClient(final BraintreeResponseListener<GoogleApiClient> listener) {
        waitForConfiguration(new ConfigurationListener() {
            @Override
            public void onConfigurationFetched(Configuration configuration) {
                listener.onResponse(getGoogleApiClient());
            }
        });
    }

    protected GoogleApiClient getGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(Wallet.API, new Wallet.WalletOptions.Builder()
                            .setEnvironment(AndroidPay.getEnvironment(getConfiguration().getAndroidPay()))
                            .setTheme(WalletConstants.THEME_LIGHT)
                            .build())
                    .build();
        }

        if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.registerConnectionCallbacks(new ConnectionCallbacks() {
                @Override
                public void onConnected(Bundle bundle) {}

                @Override
                public void onConnectionSuspended(int i) {
                    postCallback(new GoogleApiClientException("Connection suspended: " + i));
                }
            });

            mGoogleApiClient.registerConnectionFailedListener(new OnConnectionFailedListener() {
                @Override
                public void onConnectionFailed(ConnectionResult connectionResult) {
                    postCallback(new GoogleApiClientException(
                            "Connection failed: " + connectionResult.getErrorCode()));
                }
            });

            mGoogleApiClient.connect();
        }

        return mGoogleApiClient;
    }
}
