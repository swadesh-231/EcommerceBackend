package com.project.ecommercebackend.repository;

import com.project.ecommercebackend.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}

