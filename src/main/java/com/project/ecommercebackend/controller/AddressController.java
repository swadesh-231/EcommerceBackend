package com.project.ecommercebackend.controller;

import com.project.ecommercebackend.dto.request.AddressRequest;
import com.project.ecommercebackend.dto.response.AddressResponse;
import com.project.ecommercebackend.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<AddressResponse> createAddress(@Valid @RequestBody AddressRequest request) {
        AddressResponse saved = addressService.createAddress(request);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AddressResponse>> getAddresses() {
        List<AddressResponse> addressList = addressService.getAddresses();
        return ResponseEntity.ok(addressList);
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<AddressResponse> getAddressById(@PathVariable Long addressId) {
        AddressResponse address = addressService.getAddressById(addressId);
        return ResponseEntity.ok(address);
    }

    @GetMapping("/users/addresses")
    public ResponseEntity<List<AddressResponse>> getUserAddresses() {
        List<AddressResponse> addressList = addressService.getUserAddresses();
        return ResponseEntity.ok(addressList);
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressResponse> updateAddress(@PathVariable Long addressId,
                                                         @Valid @RequestBody AddressRequest request) {
        AddressResponse updated = addressService.updateAddress(addressId, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long addressId) {
        String status = addressService.deleteAddress(addressId);
        return new ResponseEntity<>(status, HttpStatus.NO_CONTENT);
    }
}
