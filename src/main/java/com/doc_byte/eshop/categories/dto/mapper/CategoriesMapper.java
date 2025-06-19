package com.doc_byte.eshop.categories.dto.mapper;

import com.doc_byte.eshop.categories.dto.CategoryNameRequest;
import com.doc_byte.eshop.categories.model.Category;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class CategoriesMapper {
    @Contract("_ -> new")
    public static @NotNull CategoryNameRequest nameOnly(@NotNull Category category) {
        return new CategoryNameRequest(
                category.getName()
        );
    }

    private CategoriesMapper(){}
}
