package com.project.ecommercebackend.service;

import com.project.ecommercebackend.dto.response.OrderResponse;

public interface OrderService {
    OrderResponse placeOrder(String emailId, Long addressId, String paymentMethod,
                             String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage);
}
