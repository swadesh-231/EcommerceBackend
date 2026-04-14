package com.project.ecommercebackend.service;

import com.project.ecommercebackend.dto.request.AddressRequest;
import com.project.ecommercebackend.dto.response.AddressResponse;

import java.util.List;

public interface AddressService {
    AddressResponse createAddress(AddressRequest request);
    List<AddressResponse> getAddresses();
    AddressResponse getAddressById(Long addressId);
    List<AddressResponse> getUserAddresses();
    AddressResponse updateAddress(Long addressId, AddressRequest request);
    String deleteAddress(Long addressId);
}
