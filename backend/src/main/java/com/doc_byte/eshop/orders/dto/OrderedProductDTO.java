package com.doc_byte.eshop.orders.dto;

import com.doc_byte.eshop.products.dto.ProductDTO;
import com.doc_byte.eshop.products.model.Product;

import java.math.BigDecimal;

/**
 * DTO for {@link com.doc_byte.eshop.orders.model.OrderItems}
 */

public record OrderedProductDTO(Long id,
                                Integer quantity,
                                ProductDTO product,
                                BigDecimal priceAtPurchase) {
}
