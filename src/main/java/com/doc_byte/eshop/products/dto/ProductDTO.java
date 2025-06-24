package com.doc_byte.eshop.products.dto;

import com.doc_byte.eshop.products.model.Product;

public record ProductDTO(Long id,
                         String name,
                         String imageUrl) {
    public ProductDTO(Product product) {
        this(product.getId(), product.getName(), product.getImageUrl());
    }
}
