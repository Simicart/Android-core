package com.trueplus.simicart.braintreelibrary;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;

import com.trueplus.simicart.braintreelibrary.interfaces.BraintreeResponseListener;
import com.trueplus.simicart.braintreelibrary.interfaces.ConfigurationListener;
import com.trueplus.simicart.braintreelibrary.interfaces.HttpResponseCallback;
import com.trueplus.simicart.braintreelibrary.models.Configuration;

import static com.trueplus.simicart.braintreelibrary.DeviceMetadata.getBraintreeSharedPreferences;

import org.json.JSONException;

import java.util.concurrent.TimeUnit;

/**
 * Manages on-disk {@link Configuration} cache and fetching configuration from the Gateway
 */
class ConfigurationManager {

    static final long TTL = TimeUnit.MINUTES.toMillis(5);

    private ConfigurationManager() {}

    protected static void getConfiguration(final BraintreeFragment fragment, final @NonNull ConfigurationListener listener,
            final @NonNull BraintreeResponseListener<Exception> errorListener) {
        final String configUrl = Uri.parse(fragment.getAuthorization().getConfigUrl())
                .buildUpon()
                .appendQueryParameter("configVersion", "3")
                .build()
                .toString();
        Log.e("ConfigurationManager", "++" + configUrl);

        Configuration cachedConfig = getCachedConfiguration(fragment.getApplicationContext(), configUrl);

        if (cachedConfig != null) {
            listener.onConfigurationFetched(cachedConfig);
        } else {
            fragment.getHttpClient().get(configUrl, new HttpResponseCallback() {
                @Override
                public void success(String responseBody) {
                    try {
                        Configuration configuration = Configuration.fromJson(responseBody);
                        cacheConfiguration(fragment.getApplicationContext(), configUrl, configuration);

                        listener.onConfigurationFetched(configuration);
                    } catch (final JSONException e) {
                        errorListener.onResponse(e);
                    }
                }

                @Override
                public void failure(final Exception exception) {
                    errorListener.onResponse(exception);
                }
            });
        }
    }

    @Nullable
    private static Configuration getCachedConfiguration(Context context, String configUrl) {
        SharedPreferences prefs = getBraintreeSharedPreferences(context);
        configUrl = Base64.encodeToString(configUrl.getBytes(), 0);

        String timestampKey = configUrl + "_timestamp";
        if ((System.currentTimeMillis() - prefs.getLong(timestampKey, 0)) > TTL) {
            return null;
        }

        try {
            return Configuration.fromJson(prefs.getString(configUrl, ""));
        } catch (JSONException e) {
            return null;
        }
    }

    private static void cacheConfiguration(Context context, String configUrl, Configuration configuration) {
        configUrl = Base64.encodeToString(configUrl.getBytes(), 0);

        String timestampKey = configUrl + "_timestamp";
        getBraintreeSharedPreferences(context).edit()
                .putString(configUrl, configuration.toJson())
                .putLong(timestampKey, System.currentTimeMillis())
                .apply();
    }
}
