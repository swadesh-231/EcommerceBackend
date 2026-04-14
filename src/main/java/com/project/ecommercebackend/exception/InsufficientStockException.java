package com.project.ecommercebackend.exception;

import lombok.Getter;

@Getter
public class InsufficientStockException extends RuntimeException {

    private final String productName;
    private final int requestedQuantity;
    private final int availableQuantity;

    public InsufficientStockException(String productName, int requestedQuantity, int availableQuantity) {
        super(String.format("Insufficient stock for '%s': requested %d, available %d",
                productName, requestedQuantity, availableQuantity));
        this.productName = productName;
        this.requestedQuantity = requestedQuantity;
        this.availableQuantity = availableQuantity;
    }
}
