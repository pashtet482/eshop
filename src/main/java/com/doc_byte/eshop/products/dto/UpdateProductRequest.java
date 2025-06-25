package com.doc_byte.eshop.products.dto;

import com.doc_byte.eshop.categories.dto.CategoryDTO;
import com.doc_byte.eshop.products.model.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.jetbrains.annotations.Contract;

import java.math.BigDecimal;
/**
 * DTO for {@link com.doc_byte.eshop.products.model.Product}
 */
public record UpdateProductRequest(
        Long id,
        @NotBlank String name,
        String description,
        @NotNull @Positive BigDecimal price,
        String imageUrl,
        @Positive Integer stockQuantity,
        @NotNull CategoryDTO category
) {
    @Contract("_ -> new")
    public static @org.jetbrains.annotations.NotNull UpdateProductRequest
        from(@org.jetbrains.annotations.NotNull Product p) {
        return new UpdateProductRequest(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getPrice(),
                p.getImageUrl(),
                p.getStockQuantity(),
                new CategoryDTO(
                        p.getCategory().getId(),
                        p.getCategory().getName()
                )
        );
    }
}
