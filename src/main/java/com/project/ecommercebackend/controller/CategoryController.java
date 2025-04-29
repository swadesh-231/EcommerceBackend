package com.project.ecommercebackend.controller;


import com.project.ecommercebackend.config.AppConstants;
import com.project.ecommercebackend.dto.CategoryDTO;
import com.project.ecommercebackend.dto.CategoryResponse;
import com.project.ecommercebackend.exception.ResourceNotFoundException;
import com.project.ecommercebackend.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class CategoryController {
    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(@RequestParam(name = "PageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false)Integer pageNumber,
                                                             @RequestParam(name = "PageSize",defaultValue = AppConstants.PAGE_SIZE,required = false)Integer pageSize,
                                                             @RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_CATEGORIES_BY,required = false)String sortBy,
                                                             @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_DIR,required = false)String sortOrder) {
        CategoryResponse categoryResponse = categoryService.geAllCategories(pageNumber, pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(categoryResponse,HttpStatus.OK);
    }
    @PostMapping("/public/categories")
    public ResponseEntity<String> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        categoryService.CreateCategory(categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Category added successfully");
    }
    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@RequestBody CategoryDTO categoryDTO, @PathVariable Long categoryId) {
        return categoryService.updateCategory(categoryDTO, categoryId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "ID", categoryId));
    }
    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId) {
        CategoryDTO deletedCategory = categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(deletedCategory, HttpStatus.OK);
    }
}

