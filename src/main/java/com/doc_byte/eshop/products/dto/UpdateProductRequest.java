package com.doc_byte.eshop.products.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpdateProductRequest(
        @NotNull Long id,
        @NotBlank String name,
        String description,
        @NotNull @Positive BigDecimal price,
        String imageURL,
        @Positive int stockQuantity,
        @NotNull Long categoryId
) {}
