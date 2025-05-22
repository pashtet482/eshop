package com.doc_byte.eshop.products.service;

import com.doc_byte.eshop.categories.repository.CategoryRepository;
import com.doc_byte.eshop.exceptions.ConflictException;
import com.doc_byte.eshop.exceptions.NotFoundException;
import com.doc_byte.eshop.model.Category;
import com.doc_byte.eshop.products.dto.CreateProductRequest;
import com.doc_byte.eshop.products.dto.UpdateProductRequest;
import com.doc_byte.eshop.products.model.Product;
import com.doc_byte.eshop.products.repository.ProductsRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductsService {
    private final ProductsRepository productsRepository;
    private final CategoryRepository categoryRepository;

    public void createProduct(@NotNull CreateProductRequest request) {
        validateCreateProductRequest(request);
        Category category = categoryRepository.findById(request.category().getId())
                .orElseThrow(() -> new NotFoundException("Категория не найдена"));

        Product product = new Product();
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setImageUrl(request.imageURL());
        product.setStockQuantity(request.stockQuantity());
        product.setCategory(category);
        productsRepository.save(product);
    }

    private void validateCreateProductRequest(@NotNull CreateProductRequest request) {
        if (productsRepository.existsByName(request.name())) {
            throw new ConflictException("Товар с таким названием уже существует");
        }
        if (request.category().getId() <= 0) {
            throw new IllegalArgumentException("Выберите категорию товара");
        }
    }

    public void deleteProduct(@NotNull Long id){
        Product product = productsRepository.findById(id).orElseThrow(() -> new NotFoundException("Товар не найден"));
        productsRepository.delete(product);
    }

    public void updateProduct(@NotNull UpdateProductRequest request) {
        Product product = productsRepository.findById(request.id())
                .orElseThrow(() -> new NotFoundException("Товар не найден"));

        if (productsRepository.existsByNameAndIdNot(request.name(), request.id())) {
            throw new ConflictException("Товар с таким именем уже существует");
        }

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setImageUrl(request.imageURL());
        product.setStockQuantity(request.stockQuantity());

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new NotFoundException("Категория не найдена"));

        product.setCategory(category);
        productsRepository.save(product);
    }

    public List<Product> getAllProducts(){
        return productsRepository.findAll();
    }
}