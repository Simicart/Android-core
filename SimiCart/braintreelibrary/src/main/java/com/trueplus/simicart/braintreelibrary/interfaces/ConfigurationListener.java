package com.trueplus.simicart.braintreelibrary.interfaces;

import com.trueplus.simicart.braintreelibrary.models.Configuration;

/**
 * Interface that defines a callback for {@link Configuration}.
 */
public interface ConfigurationListener extends BraintreeListener {

    /**
     * {@link #onConfigurationFetched(Configuration)} will be called when
     * {@link Configuration} has been successfully fetched.
     */
    void onConfigurationFetched(Configuration configuration);
}
