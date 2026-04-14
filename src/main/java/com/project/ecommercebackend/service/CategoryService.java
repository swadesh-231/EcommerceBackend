package com.project.ecommercebackend.service;

import com.project.ecommercebackend.dto.request.CategoryRequest;
import com.project.ecommercebackend.dto.response.CategoryPageResponse;
import com.project.ecommercebackend.dto.response.CategoryResponse;

import java.util.Optional;

public interface CategoryService {
    CategoryPageResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    CategoryResponse createCategory(CategoryRequest request);
    Optional<CategoryResponse> updateCategory(CategoryRequest request, Long categoryId);
    CategoryResponse deleteCategory(Long id);
}
