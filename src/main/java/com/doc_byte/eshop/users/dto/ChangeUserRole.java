package com.doc_byte.eshop.users.dto;

/**
 * DTO for {@link com.doc_byte.eshop.users.model.User}
 */

public record ChangeUserRole(
        String email,
        Boolean role) {
}
