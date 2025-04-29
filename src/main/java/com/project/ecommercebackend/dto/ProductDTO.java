package com.project.ecommercebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long productId;
    private String productName;
    private String imageUrl;
    private String productDescription;
    private Integer quantity;
    private Double price;
    private Double discount;
    private Double specialPrice;
}
