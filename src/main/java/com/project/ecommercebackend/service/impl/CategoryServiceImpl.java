package com.project.ecommercebackend.service.impl;

import com.project.ecommercebackend.dto.request.CategoryRequest;
import com.project.ecommercebackend.dto.response.CategoryPageResponse;
import com.project.ecommercebackend.dto.response.CategoryResponse;
import com.project.ecommercebackend.exception.APIException;
import com.project.ecommercebackend.exception.DuplicateResourceException;
import com.project.ecommercebackend.exception.ResourceNotFoundException;
import com.project.ecommercebackend.model.Category;
import com.project.ecommercebackend.repository.CategoryRepository;
import com.project.ecommercebackend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public CategoryPageResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        if (categoryPage.isEmpty()) {
            throw new APIException("No categories found");
        }

        List<CategoryResponse> categories = categoryPage.getContent().stream()
                .map(category -> modelMapper.map(category, CategoryResponse.class))
                .toList();

        CategoryPageResponse response = new CategoryPageResponse();
        response.setCategories(categories);
        response.setPageNumber(categoryPage.getNumber());
        response.setPageSize(categoryPage.getSize());
        response.setTotalElements(categoryPage.getTotalElements());
        response.setTotalPages(categoryPage.getTotalPages());
        response.setLastPage(categoryPage.isLast());
        return response;
    }

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        Category existing = categoryRepository.findByCategoryName(request.getCategoryName());
        if (existing != null) {
            throw new DuplicateResourceException("Category", "categoryName", request.getCategoryName());
        }
        Category category = modelMapper.map(request, Category.class);
        Category saved = categoryRepository.save(category);
        return modelMapper.map(saved, CategoryResponse.class);
    }

    @Override
    @Transactional
    public Optional<CategoryResponse> updateCategory(CategoryRequest request, Long categoryId) {
        Optional<Category> existingOpt = categoryRepository.findById(categoryId);
        if (existingOpt.isEmpty()) {
            return Optional.empty();
        }
        Category existing = existingOpt.get();
        existing.setCategoryName(request.getCategoryName());
        Category updated = categoryRepository.save(existing);
        return Optional.of(modelMapper.map(updated, CategoryResponse.class));
    }

    @Override
    @Transactional
    public CategoryResponse deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        categoryRepository.delete(category);
        return modelMapper.map(category, CategoryResponse.class);
    }
}
