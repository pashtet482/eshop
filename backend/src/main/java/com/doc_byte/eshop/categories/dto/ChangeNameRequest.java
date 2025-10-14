package com.doc_byte.eshop.categories.dto;
/**
* DTO for {@link com.doc_byte.eshop.categories.model.Category}
 */
public record ChangeNameRequest(String oldName, String newName) {}
