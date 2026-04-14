package com.project.ecommercebackend.exception;

import lombok.Getter;

@Getter
public class OutOfStockException extends RuntimeException {

    private final String productName;

    public OutOfStockException(String productName) {
        super(String.format("Product '%s' is currently out of stock", productName));
        this.productName = productName;
    }
}
