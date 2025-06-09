package com.doc_byte.eshop.categories.service;


import com.doc_byte.eshop.categories.dto.CategoryRequest;
import com.doc_byte.eshop.categories.dto.ChangeNameRequest;
import com.doc_byte.eshop.categories.model.Category;
import com.doc_byte.eshop.categories.repository.CategoryRepository;
import com.doc_byte.eshop.exceptions.ConflictException;
import com.doc_byte.eshop.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoriesService {

    private final CategoryRepository categoryRepository;

    public void createCategory(@NotNull CategoryRequest request){
        Optional<Category> categoryCheck = categoryRepository.findByName(request.name());

        if (categoryCheck.isPresent()) throw new ConflictException("Категория уже существует");

        Category category = new Category();
        category.setName(request.name());
        categoryRepository.save(category);
    }

    public void deleteCategory(@NotNull CategoryRequest request){
        Category category = categoryRepository.findByName(request.name())
                .orElseThrow(() -> new NotFoundException("Категория не найдена"));

        categoryRepository.delete(category);
    }

    public void changeCategoryName(@NotNull ChangeNameRequest request){
        Category category = categoryRepository.findByName(request.oldName())
                .orElseThrow(() -> new NotFoundException("Категория не найдена"));

        Optional<Category> categoryCheck = categoryRepository.findByName(request.newName());
        if (categoryCheck.isPresent()) {
            throw new ConflictException("Категория с таким именем уже существует");
        }

        category.setName(request.newName());
        categoryRepository.save(category);
    }
}

