package com.doc_byte.eshop.users.dto;


import jakarta.validation.constraints.NotBlank;

public record ChangeUsernameRequest(
        @NotBlank String oldUsername,
        @NotBlank String newUsername){
}
