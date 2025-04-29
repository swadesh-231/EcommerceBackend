package com.project.ecommercebackend.service;


import com.project.ecommercebackend.dto.CategoryDTO;
import com.project.ecommercebackend.dto.CategoryResponse;

import java.util.Optional;

public interface CategoryService {
    CategoryResponse geAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    CategoryDTO CreateCategory(CategoryDTO categoryDTO);
    Optional<CategoryDTO> updateCategory(CategoryDTO categoryDTO, Long categoryId);
    CategoryDTO deleteCategory(Long id);
}
