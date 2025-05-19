package com.doc_byte.eshop.dto;


public record DeleteUserRequest (
    String username,
    String password) {
}