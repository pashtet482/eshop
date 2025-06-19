package com.doc_byte.eshop.orders.dto;


import java.util.List;

/**
 *   DTO for {@link com.doc_byte.eshop.orders.model.Orders}
 */
public record CreateOrderDTO(Long userId, List<OrderItemsDTO> orderItems) {}
