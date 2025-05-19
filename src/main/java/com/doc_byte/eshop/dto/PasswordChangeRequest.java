package com.doc_byte.eshop.dto;


public record PasswordChangeRequest(
        String username,
        String oldPassword,
        String newPassword) {
}
