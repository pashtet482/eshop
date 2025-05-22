package com.doc_byte.eshop.products.dto;

import com.doc_byte.eshop.categories.dto.CategoryIdDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
/**
 * DTO for {@link com.doc_byte.eshop.products.model.Product}
 */
public record UpdateProductRequest(
        @NotBlank String name,
        String description,
        @NotNull @Positive BigDecimal price,
        String imageUrl,
        @Positive int stockQuantity,
        @NotNull CategoryIdDTO category
) {}
