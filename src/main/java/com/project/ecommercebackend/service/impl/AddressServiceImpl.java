package com.project.ecommercebackend.service.impl;

import com.project.ecommercebackend.dto.request.AddressRequest;
import com.project.ecommercebackend.dto.response.AddressResponse;
import com.project.ecommercebackend.exception.ResourceNotFoundException;
import com.project.ecommercebackend.model.Address;
import com.project.ecommercebackend.model.User;
import com.project.ecommercebackend.repository.AddressRepository;
import com.project.ecommercebackend.repository.UserRepository;
import com.project.ecommercebackend.service.AddressService;
import com.project.ecommercebackend.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final AuthUtil authUtil;

    @Override
    @Transactional
    public AddressResponse createAddress(AddressRequest request) {
        User user = authUtil.loggedInUser();
        Address address = modelMapper.map(request, Address.class);
        address.setUser(user);

        if (user.getAddresses() == null) {
            user.setAddresses(new ArrayList<>());
        }
        user.getAddresses().add(address);

        Address savedAddress = addressRepository.save(address);
        return modelMapper.map(savedAddress, AddressResponse.class);
    }

    @Override
    public List<AddressResponse> getAddresses() {
        List<Address> addresses = addressRepository.findAll();
        return addresses.stream()
                .map(address -> modelMapper.map(address, AddressResponse.class))
                .toList();
    }

    @Override
    public AddressResponse getAddressById(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));
        return modelMapper.map(address, AddressResponse.class);
    }

    @Override
    public List<AddressResponse> getUserAddresses() {
        User user = authUtil.loggedInUser();
        List<Address> addresses = user.getAddresses();
        if (addresses == null || addresses.isEmpty()) {
            return Collections.emptyList();
        }
        return addresses.stream()
                .map(address -> modelMapper.map(address, AddressResponse.class))
                .toList();
    }

    @Override
    @Transactional
    public AddressResponse updateAddress(Long addressId, AddressRequest request) {
        Address existingAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        existingAddress.setCity(request.getCity());
        existingAddress.setPincode(request.getPincode());
        existingAddress.setState(request.getState());
        existingAddress.setCountry(request.getCountry());
        existingAddress.setStreet(request.getStreet());
        existingAddress.setBuildingName(request.getBuildingName());

        Address updatedAddress = addressRepository.save(existingAddress);
        return modelMapper.map(updatedAddress, AddressResponse.class);
    }

    @Override
    @Transactional
    public String deleteAddress(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        User user = address.getUser();
        if (user != null && user.getAddresses() != null) {
            user.getAddresses().removeIf(a -> a.getAddressId().equals(addressId));
        }

        addressRepository.delete(address);
        return "Address deleted successfully with addressId: " + addressId;
    }
}
