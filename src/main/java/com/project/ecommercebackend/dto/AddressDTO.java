package com.project.ecommercebackend.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    private Long addressId;

    @NotBlank(message = "Street name is required")
    @Size(min = 5, message = "Street name must be at least 5 characters")
    private String street;

    @NotBlank(message = "Building name is required")
    @Size(min = 5, message = "Building name must be at least 5 characters")
    private String buildingName;

    @NotBlank(message = "City name is required")
    @Size(min = 4, message = "City name must be at least 4 characters")
    private String city;

    @NotBlank(message = "State name is required")
    @Size(min = 2, message = "State name must be at least 2 characters")
    private String state;

    @NotBlank(message = "Country name is required")
    @Size(min = 2, message = "Country name must be at least 2 characters")
    private String country;

    @NotBlank(message = "Pincode is required")
    @Size(min = 5, message = "Pincode must be at least 5 characters")
    private String pincode;
}

