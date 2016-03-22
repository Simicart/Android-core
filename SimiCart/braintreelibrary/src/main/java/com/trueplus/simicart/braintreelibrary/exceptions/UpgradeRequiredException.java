package com.trueplus.simicart.braintreelibrary.exceptions;

/**
 * Exception thrown when the Braintree gateway responds with a 426 Upgrade Required.
 * This error means that the SDK version in use is deprecated and must be updated to work.
 */
public class UpgradeRequiredException extends BraintreeException {
    public UpgradeRequiredException(String message) {
        super(message);
    }

    public UpgradeRequiredException() {
        super();
    }
}
