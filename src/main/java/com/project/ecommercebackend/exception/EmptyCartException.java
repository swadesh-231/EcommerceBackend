package com.project.ecommercebackend.exception;

public class EmptyCartException extends RuntimeException {

    public EmptyCartException() {
        super("Cannot place order: cart is empty");
    }

    public EmptyCartException(String message) {
        super(message);
    }
}
