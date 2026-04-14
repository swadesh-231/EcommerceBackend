package com.project.ecommercebackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {
    private Long cartId;
    private BigDecimal totalPrice = BigDecimal.ZERO;
    private List<ProductResponse> products = new ArrayList<>();
}
