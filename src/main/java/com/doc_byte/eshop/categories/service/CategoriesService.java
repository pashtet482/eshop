package com.doc_byte.eshop.categories.service;


import com.doc_byte.eshop.categories.dto.CategoryDTO;
import com.doc_byte.eshop.categories.dto.CategoryNameRequest;
import com.doc_byte.eshop.categories.dto.ChangeNameRequest;
import com.doc_byte.eshop.categories.model.Category;
import com.doc_byte.eshop.categories.repository.CategoryRepository;
import com.doc_byte.eshop.exceptions.ConflictException;
import com.doc_byte.eshop.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriesService {

    private final CategoryRepository categoryRepository;

    public void createCategory(@NotNull CategoryNameRequest request){
        Optional<Category> categoryCheck = categoryRepository.findByName(request.name());

        if (categoryCheck.isPresent()) throw new ConflictException("Категория уже существует");

        Category category = new Category();
        category.setName(request.name());
        categoryRepository.save(category);
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

    public List<CategoryDTO> getAllCategories(){
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(this::convertToDTO)
                .toList();
    }

    @NotNull
    private CategoryDTO convertToDTO(@NotNull Category category) {
        return new CategoryDTO(category.getId(), category.getName());
    }
}

