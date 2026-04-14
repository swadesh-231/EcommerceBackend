package com.project.ecommercebackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long orderId;
    private String email;
    private List<OrderItemResponse> orderItems = new ArrayList<>();
    private LocalDate orderDate;
    private PaymentResponse payment;
    private BigDecimal totalAmount;
    private String orderStatus;
    private Long addressId;
}
