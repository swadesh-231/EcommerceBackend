package com.project.ecommercebackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String productName;
    private String imageUrl;
    private String productDescription;
    private Integer quantity;
    private java.math.BigDecimal price;
    private java.math.BigDecimal discount;
    private java.math.BigDecimal specialPrice;

    @ManyToOne
    @JoinColumn(name = "category_Id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User user;

    @OneToMany(mappedBy = "product", cascade = { CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REMOVE }, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CartItem> cartItems = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Product))
            return false;
        return productId != null && productId.equals(((Product) o).getProductId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
