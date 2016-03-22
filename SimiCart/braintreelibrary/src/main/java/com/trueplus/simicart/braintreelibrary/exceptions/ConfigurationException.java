package com.trueplus.simicart.braintreelibrary.exceptions;

/**
 * Error class thrown when a configuration value is invalid
 */
public class ConfigurationException extends BraintreeException {
    public ConfigurationException(String message) {
        super(message);
    }
}
