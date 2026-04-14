package com.project.ecommercebackend.service;

import com.project.ecommercebackend.dto.response.CartResponse;

import java.util.List;

public interface CartService {
    CartResponse addProductToCart(Long productId, Integer quantity);
    List<CartResponse> getAllCarts();
    CartResponse getCart(String emailId, Long cartId);
    CartResponse getLoggedInUserCart();
    CartResponse updateProductQuantityInCart(Long productId, Integer quantity);
    String deleteProductFromCart(Long cartId, Long productId);
    void updateProductInCarts(Long cartId, Long productId);
}
