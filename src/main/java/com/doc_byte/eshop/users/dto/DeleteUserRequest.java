package com.doc_byte.eshop.users.dto;


import jakarta.validation.constraints.NotBlank;
/**
 * DTO for {@link com.doc_byte.eshop.users.model.User}
 */
public record DeleteUserRequest (
        @NotBlank String username,
        @NotBlank String password) {
}