package com.project.ecommercebackend.service;

import com.project.ecommercebackend.dto.request.ProductRequest;
import com.project.ecommercebackend.dto.response.ProductPageResponse;
import com.project.ecommercebackend.dto.response.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
    ProductResponse addProduct(Long categoryId, ProductRequest request);
    ProductPageResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    ProductPageResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    ProductPageResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    ProductResponse updateProduct(Long productId, ProductRequest request);
    ProductResponse deleteProduct(Long productId);
    ProductResponse updateProductImage(Long productId, MultipartFile image);
}
