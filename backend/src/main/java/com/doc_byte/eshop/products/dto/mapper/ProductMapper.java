package com.doc_byte.eshop.products.dto.mapper;

import com.doc_byte.eshop.categories.dto.CategoryDTO;
import com.doc_byte.eshop.products.dto.GetAllProducts;
import com.doc_byte.eshop.products.model.Product;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public GetAllProducts toDto(@NotNull Product product) {
        return new GetAllProducts(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImageUrl(),
                product.getStockQuantity(),
                new CategoryDTO(
                        product.getCategory().getId(),
                        product.getCategory().getName()
                )
        );
    }
}
