package com.trueplus.simicart.braintreelibrary.exceptions;

/**
 * Error thrown when arguments provided to a method are invalid.
 */
public class InvalidArgumentException extends Exception {
    public InvalidArgumentException(String message) {
        super(message);
    }
}
