package com.doc_byte.eshop.orders.dto;

import com.doc_byte.eshop.orders.model.OrderStatus;

/**
 *   DTO for {@link com.doc_byte.eshop.orders.model.Orders}
 */

public record ChangeStatusDTO(Long id, OrderStatus status) {}
