package com.doc_byte.eshop.dto;


public record ChangeUsernameRequest(
        String oldUsername,
        String newUsername){
}
