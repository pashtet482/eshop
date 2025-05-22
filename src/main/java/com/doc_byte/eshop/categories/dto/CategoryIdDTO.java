package com.doc_byte.eshop.categories.dto;

import jakarta.validation.constraints.NotNull;

/**
 * DTO for {@link com.doc_byte.eshop.categories.model.Category}
 */
public record CategoryIdDTO(@NotNull Long id) {}
