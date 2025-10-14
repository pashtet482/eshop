package com.doc_byte.eshop.products.dto;

import com.doc_byte.eshop.categories.dto.CategoryDTO;

import java.math.BigDecimal;

/**
 * DTO for {@link com.doc_byte.eshop.products.model.Product}
 */
public record GetAllProducts(
        Long id,
        String name,
        String description,
        BigDecimal price,
        String imageUrl,
        Integer stockQuantity,
        CategoryDTO category
) {}
