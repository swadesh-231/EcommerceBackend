package com.project.ecommercebackend.controller;

import com.project.ecommercebackend.dto.response.CartResponse;
import com.project.ecommercebackend.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartResponse> addProductToCart(@PathVariable Long productId,
                                                        @PathVariable Integer quantity) {
        CartResponse response = cartService.addProductToCart(productId, quantity);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/carts")
    public ResponseEntity<List<CartResponse>> getCarts() {
        List<CartResponse> responses = cartService.getAllCarts();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartResponse> getCartById() {
        CartResponse response = cartService.getLoggedInUserCart();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/cart/products/{productId}/quantity/{operation}")
    public ResponseEntity<CartResponse> updateCartProduct(@PathVariable Long productId,
                                                          @PathVariable String operation) {
        CartResponse response = cartService.updateProductQuantityInCart(productId,
                operation.equalsIgnoreCase("delete") ? -1 : 1);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/carts/{cartId}/product/{productId}")
    public ResponseEntity<String> deleteProductFromCart(@PathVariable Long cartId,
                                                        @PathVariable Long productId) {
        String status = cartService.deleteProductFromCart(cartId, productId);
        return ResponseEntity.ok(status);
    }
}
