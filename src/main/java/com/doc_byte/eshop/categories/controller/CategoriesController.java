package com.doc_byte.eshop.categories.controller;


import com.doc_byte.eshop.categories.dto.CategoryNameRequest;
import com.doc_byte.eshop.categories.dto.ChangeNameRequest;
import com.doc_byte.eshop.categories.service.CategoriesService;
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
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoriesController
{
    private final CategoriesService categoriesService;

    @PostMapping("/create-category")
    @Operation(summary = "Создание новой категории")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Категоия успешно создана"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "409", description = "Категория уже сущуствует"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<String> createCategory(@Valid @RequestBody CategoryNameRequest request){
        categoriesService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Категория успешно добавлена");
    }

    @DeleteMapping("/delete-category")
    @Operation(summary = "Удаление категории")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Категоия успешно удалена"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<String> deleteCategory(@Valid @RequestBody CategoryNameRequest request){
        categoriesService.deleteCategory(request);
        return ResponseEntity.status(HttpStatus.OK).body("Категория успешно удалена");
    }

    @PutMapping("/change-category-name")
    @Operation(summary = "Изнение названия категории")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Категоия успешно обновлена"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<String> changeCategoryName(@Valid @RequestBody ChangeNameRequest request){
        categoriesService.changeCategoryName(request);
        return ResponseEntity.status(HttpStatus.OK).body("Категория успешно обновлена");
    }

    @GetMapping("/get-all-categories")
    @Operation(summary = "Вывод всех категорий")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные успешно выведены"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<List<CategoryNameRequest>> getAllCategories(){
        return ResponseEntity.ok(categoriesService.getAllCategories());
    }
}
