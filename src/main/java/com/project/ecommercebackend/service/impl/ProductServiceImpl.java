package com.project.ecommercebackend.service.impl;

import com.project.ecommercebackend.dto.request.ProductRequest;
import com.project.ecommercebackend.dto.response.ProductPageResponse;
import com.project.ecommercebackend.dto.response.ProductResponse;
import com.project.ecommercebackend.exception.APIException;
import com.project.ecommercebackend.exception.DuplicateResourceException;
import com.project.ecommercebackend.exception.ResourceNotFoundException;
import com.project.ecommercebackend.model.Category;
import com.project.ecommercebackend.model.Product;
import com.project.ecommercebackend.repository.CategoryRepository;
import com.project.ecommercebackend.repository.ProductRepository;
import com.project.ecommercebackend.service.FileService;
import com.project.ecommercebackend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final FileService fileService;

    @Value("${app.image-dir}")
    private String imageDir;

    @Override
    @Transactional
    public ProductResponse addProduct(Long categoryId, ProductRequest request) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        boolean productExists = category.getProducts().stream()
                .anyMatch(p -> p.getProductName().equals(request.getProductName()));

        if (productExists) {
            throw new DuplicateResourceException("Product", "productName", request.getProductName());
        }

        Product product = modelMapper.map(request, Product.class);
        product.setImageUrl("default.png");
        product.setCategory(category);
        product.setSpecialPrice(calculateSpecialPrice(product.getPrice(), product.getDiscount()));

        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductResponse.class);
    }

    @Override
    public ProductPageResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Pageable pageable = buildPageable(pageNumber, pageSize, sortBy, sortOrder);
        Page<Product> pageProducts = productRepository.findAll(pageable);
        return buildProductPageResponse(pageProducts);
    }

    @Override
    public ProductPageResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize,
                                                String sortBy, String sortOrder) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        Pageable pageable = buildPageable(pageNumber, pageSize, sortBy, sortOrder);
        Page<Product> pageProducts = productRepository.findByCategoryOrderByPriceAsc(category, pageable);

        if (pageProducts.isEmpty()) {
            throw new APIException(category.getCategoryName() + " category does not have any products");
        }
        return buildProductPageResponse(pageProducts);
    }

    @Override
    public ProductPageResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize,
                                                      String sortBy, String sortOrder) {
        Pageable pageable = buildPageable(pageNumber, pageSize, sortBy, sortOrder);
        Page<Product> productPage = productRepository.findByProductNameLikeIgnoreCase("%" + keyword + "%", pageable);

        if (productPage.isEmpty()) {
            throw new APIException("No products found with keyword: " + keyword);
        }
        return buildProductPageResponse(productPage);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long productId, ProductRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        product.setProductName(request.getProductName());
        product.setProductDescription(request.getProductDescription());
        product.setQuantity(request.getQuantity());
        product.setDiscount(request.getDiscount());
        product.setPrice(request.getPrice());
        product.setSpecialPrice(calculateSpecialPrice(request.getPrice(), request.getDiscount()));

        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductResponse.class);
    }

    @Override
    @Transactional
    public ProductResponse deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        productRepository.delete(product);
        return modelMapper.map(product, ProductResponse.class);
    }

    @Override
    @Transactional
    public ProductResponse updateProductImage(Long productId, MultipartFile image) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        String fileName = fileService.uploadImage(imageDir, image);
        product.setImageUrl(fileName);

        Product updatedProduct = productRepository.save(product);
        return modelMapper.map(updatedProduct, ProductResponse.class);
    }

    private BigDecimal calculateSpecialPrice(BigDecimal price, BigDecimal discount) {
        BigDecimal discountAmount = price.multiply(discount).multiply(new BigDecimal("0.01"));
        return price.subtract(discountAmount);
    }

    private Pageable buildPageable(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        return PageRequest.of(pageNumber, pageSize, sort);
    }

    private ProductPageResponse buildProductPageResponse(Page<Product> pageProducts) {
        List<ProductResponse> products = pageProducts.getContent().stream()
                .map(product -> modelMapper.map(product, ProductResponse.class))
                .toList();

        ProductPageResponse response = new ProductPageResponse();
        response.setContents(products);
        response.setPageNumber(pageProducts.getNumber());
        response.setPageSize(pageProducts.getSize());
        response.setTotalElements(pageProducts.getTotalElements());
        response.setTotalPages(pageProducts.getTotalPages());
        response.setLast(pageProducts.isLast());
        return response;
    }
}
