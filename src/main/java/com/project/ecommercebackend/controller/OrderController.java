package com.project.ecommercebackend.controller;

import com.project.ecommercebackend.dto.request.OrderPlaceRequest;
import com.project.ecommercebackend.dto.response.OrderResponse;
import com.project.ecommercebackend.service.OrderService;
import com.project.ecommercebackend.utils.AuthUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final AuthUtil authUtil;

    @PostMapping("/order/users/payments/{paymentMethod}")
    public ResponseEntity<OrderResponse> orderProducts(
            @PathVariable String paymentMethod,
            @Valid @RequestBody OrderPlaceRequest request) {

        String emailId = authUtil.loggedInEmail();
        OrderResponse order = orderService.placeOrder(
                emailId,
                request.getAddressId(),
                paymentMethod,
                request.getPgName(),
                request.getPgPaymentId(),
                request.getPgStatus(),
                request.getPgResponseMessage()
        );

        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
}
