package com.doc_byte.eshop.products.controller;

import com.doc_byte.eshop.products.dto.CreateProductRequest;
import com.doc_byte.eshop.products.dto.GetAllProducts;
import com.doc_byte.eshop.products.dto.UpdateProductRequest;
import com.doc_byte.eshop.products.model.Product;
import com.doc_byte.eshop.products.service.ProductsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductsController {

    private final ProductsService productsService;

    @PostMapping("/create-product")
    @Operation(summary = "Создание нового товара")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Товар успешно добавлен"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные товара"),
            @ApiResponse(responseCode = "409", description = "Товар с таким названием уже существует"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<String> createProduct(@Valid @RequestBody CreateProductRequest request){
        productsService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Товар успешно добавлен");
    }

    @PutMapping("/update-product/{id}")
    @Operation(summary = "Обновление товара")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные успешно изменены"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "404", description = "Товар не найден"),
            @ApiResponse(responseCode = "409", description = "Товар с таким названием уже существует"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<UpdateProductRequest> updateProduct(@PathVariable Long id, @Valid @RequestBody UpdateProductRequest request) {
        Product updatedProduct = productsService.updateProduct(id, request);
        return ResponseEntity.ok(UpdateProductRequest.from(updatedProduct));
    }

    @GetMapping("/products")
    @Operation(summary = "Вывод всех товаров")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список тоаров получен"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<List<GetAllProducts>> getAllProducts(){
        return ResponseEntity.ok(productsService.getAllProducts());
    }

    @PostMapping("/product-image")
    @Operation(summary = "Загрузка изображения на сервер")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список тоаров получен"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<String> uploadProductImage(@RequestParam("file") @NotNull MultipartFile file) {
        return productsService.uploadProductImage(file);
    }
}