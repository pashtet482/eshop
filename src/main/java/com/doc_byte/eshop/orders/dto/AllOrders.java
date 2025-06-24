package com.doc_byte.eshop.orders.dto;

import com.doc_byte.eshop.orders.model.OrderStatus;
import com.doc_byte.eshop.users.dto.UserIdDTO;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * DTO for {@link com.doc_byte.eshop.orders.model.Orders}
 */

public record AllOrders(Long id,
                        UserIdDTO user,
                        OrderStatus status,
                        Instant createdAt,
                        List<OrderedProductDTO> orderedProductDTO,
                        BigDecimal totalPrice) {
}
