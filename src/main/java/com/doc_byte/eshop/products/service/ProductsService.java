package com.doc_byte.eshop.products.service;

import com.doc_byte.eshop.products.dto.CreateProductRequest;
import com.doc_byte.eshop.products.model.Product;
import com.doc_byte.eshop.products.repository.ProductsRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductsService {
    private final ProductsRepository productsRepository;

    public void createProduct(@NotNull CreateProductRequest request){
        //TODO: Сделать валидацию
        Product product = new Product();
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setImageUrl(request.imageURL());
        product.setStockQuantity(request.stockQuantity());
        product.setCategory(request.categoryID());
        productsRepository.save(product);
    }
}
