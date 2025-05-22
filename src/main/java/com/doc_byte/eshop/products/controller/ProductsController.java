package com.doc_byte.eshop.products.controller;

import com.doc_byte.eshop.products.dto.CreateProductRequest;
import com.doc_byte.eshop.products.dto.GetAllProducts;
import com.doc_byte.eshop.products.dto.UpdateProductRequest;
import com.doc_byte.eshop.products.service.ProductsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductsController {

    private final ProductsService productsService;

    @PostMapping("/create-product")
    @Operation(summary = "Создание нового товара")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Товар успешно добавлен"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные товара"),
            @ApiResponse(responseCode = "409", description = "Товарн с таким названием уже существует"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<String> createProduct(@Valid @RequestBody CreateProductRequest request){
        productsService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Товар успешно добавлен");
    }

    @DeleteMapping("/delete-product/{id}")
    @Operation(summary = "Удаление товара")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Товар успешно удален"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "404", description = "Товар не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productsService.deleteProduct(id);
        return ResponseEntity.ok("Товар удалён");
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
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @Valid @RequestBody UpdateProductRequest request) {
        productsService.updateProduct(id, request);
        return ResponseEntity.ok("Товар успешно обновлён");
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
}