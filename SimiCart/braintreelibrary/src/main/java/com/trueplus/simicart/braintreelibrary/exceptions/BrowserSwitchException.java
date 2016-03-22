package com.trueplus.simicart.braintreelibrary.exceptions;


/**
 * Error class thrown when browser switch returns an error
 */
public class BrowserSwitchException extends BraintreeException {

    public BrowserSwitchException(String message) {
        super(message);
    }
}
