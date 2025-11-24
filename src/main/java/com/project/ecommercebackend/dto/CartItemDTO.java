package com.project.ecommercebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
    private Long cartItemId;
    private CartDTO cart;
    private ProductDTO product;
    private Integer quantity;
    private java.math.BigDecimal discountedPrice;
    private java.math.BigDecimal productPrice;

}
