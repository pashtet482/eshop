package com.doc_byte.eshop.dto;


import jakarta.validation.constraints.NotBlank;

public record DeleteUserRequest (
        @NotBlank String username,
        @NotBlank String password) {
}