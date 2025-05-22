package com.doc_byte.eshop.categories.repository;

import com.doc_byte.eshop.categories.model.Category;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @NotNull Optional<Category> findById(@NotNull Long id);
}
