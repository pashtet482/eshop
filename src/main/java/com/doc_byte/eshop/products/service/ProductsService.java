package com.doc_byte.eshop.products.service;

import com.doc_byte.eshop.categories.repository.CategoryRepository;
import com.doc_byte.eshop.exceptions.ConflictException;
import com.doc_byte.eshop.exceptions.NotFoundException;
import com.doc_byte.eshop.categories.model.Category;
import com.doc_byte.eshop.products.dto.CreateProductRequest;
import com.doc_byte.eshop.products.dto.GetAllProducts;
import com.doc_byte.eshop.products.dto.UpdateProductRequest;
import com.doc_byte.eshop.products.dto.mapper.ProductMapper;
import com.doc_byte.eshop.products.model.Product;
import com.doc_byte.eshop.products.repository.ProductsRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductsService {
    private final ProductsRepository productsRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    public void createProduct(@Valid CreateProductRequest request) {
        validateCreateProductRequest(request);
        Category category = categoryRepository.findById(request.category().id())
                .orElseThrow(() -> new NotFoundException("Категория не найдена"));

        Product product = new Product();
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setImageUrl(request.imageUrl());
        product.setStockQuantity(request.stockQuantity());
        product.setCategory(category);
        productsRepository.save(product);
    }

    private void validateCreateProductRequest(@NotNull CreateProductRequest request) {
        if (productsRepository.existsByName(request.name())) {
            throw new ConflictException("Товар с таким названием уже существует");
        }
        if (request.category().id() <= 0) {
            throw new IllegalArgumentException("Выберите категорию товара");
        }
    }

    public void deleteProduct(@NotNull Long id){
        Product product = productsRepository.findById(id).orElseThrow(() -> new NotFoundException("Товар не найден"));
        productsRepository.delete(product);
    }

    public void updateProduct(Long id, @NotNull UpdateProductRequest request) {
        Product product = productsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Товар не найден"));

        if (productsRepository.existsByNameAndIdNot(request.name(), id)) {
            throw new ConflictException("Товар с таким именем уже существует");
        }

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setImageUrl(request.imageUrl());
        product.setStockQuantity(request.stockQuantity());

        Category category = categoryRepository.findById(request.category().id())
                .orElseThrow(() -> new NotFoundException("Категория не найдена"));

        product.setCategory(category);
        productsRepository.save(product);
    }

    public List<GetAllProducts> getAllProducts(){
        return productsRepository.findAll().stream()
                .map(productMapper::toDto)
                .toList();
    }

    public ResponseEntity<String> uploadProductImage(@NotNull MultipartFile file){
        if (file.isEmpty()) {
            System.out.println(">>> Файл пустой");
            return ResponseEntity.badRequest().body("Файл пустой");
        }

        try {
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path savePath = Paths.get("uploads/products", filename);

            System.out.println(">>> Пытаемся создать путь: " + savePath.toAbsolutePath());

            Files.createDirectories(savePath.getParent());
            file.transferTo(savePath.toFile());

            String relativeUrl = "uploads/products/" + filename;
            System.out.println(">>> Загружено успешно: " + relativeUrl);

            return ResponseEntity.ok(relativeUrl);
        } catch (IOException e) {
            e.printStackTrace();  // Покажет, ЧТО именно сломалось
            return ResponseEntity.status(500).body("Ошибка загрузки");
        }
    }
}