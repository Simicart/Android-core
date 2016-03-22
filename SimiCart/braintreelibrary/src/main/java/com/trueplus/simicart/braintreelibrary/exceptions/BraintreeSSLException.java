package com.trueplus.simicart.braintreelibrary.exceptions;

/**
 * Exception thrown when there is an error setting up SSL certificate pinning.
 */
public class BraintreeSSLException extends BraintreeException {

    public BraintreeSSLException(String message) {
        super(message);
    }
}