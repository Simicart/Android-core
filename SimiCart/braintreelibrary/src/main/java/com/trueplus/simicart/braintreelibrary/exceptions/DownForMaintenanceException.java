package com.trueplus.simicart.braintreelibrary.exceptions;

/**
 * Exception thrown when the Braintree gateway is unavailable (503 HTTP_UNAVAILABLE).
 */
public class DownForMaintenanceException extends BraintreeException {
    public DownForMaintenanceException(String message) {
        super(message);
    }

    public DownForMaintenanceException() {
        super();
    }
}
