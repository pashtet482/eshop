package com.doc_byte.eshop.products.dto;

import com.doc_byte.eshop.categories.dto.CategoryIdDTO;

import java.math.BigDecimal;

/**
 * DTO for {@link com.doc_byte.eshop.products.model.Product}
 */
public record GetAllProducts(
        String name,
        String description,
        BigDecimal price,
        String imageUrl,
        Integer stockQuantity,
        CategoryIdDTO category
) {}
