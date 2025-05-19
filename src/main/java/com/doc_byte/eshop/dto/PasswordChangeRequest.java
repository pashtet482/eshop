package com.doc_byte.eshop.dto;


import jakarta.validation.constraints.NotBlank;

public record PasswordChangeRequest(
        @NotBlank String username,
        @NotBlank String oldPassword,
        @NotBlank String newPassword) {
}
