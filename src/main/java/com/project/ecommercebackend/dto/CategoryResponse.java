package com.project.ecommercebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {
    private List<CategoryDTO> categories;
    private Integer PageNumber;
    private Integer PageSize;
    private Long TotalElements;
    private Integer TotalPages;
    private Boolean lastPage;
}
