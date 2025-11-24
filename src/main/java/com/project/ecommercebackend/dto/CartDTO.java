package com.project.ecommercebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    private Long cartId;
    private java.math.BigDecimal totalPrice = java.math.BigDecimal.ZERO;
    private List<ProductDTO> products = new ArrayList<>();
}
