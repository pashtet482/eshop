package com.doc_byte.eshop.orders.dto.mapper;

import com.doc_byte.eshop.orders.dto.AllOrders;
import com.doc_byte.eshop.orders.dto.OrderedProductDTO;
import com.doc_byte.eshop.orders.model.OrderItems;
import com.doc_byte.eshop.orders.model.Orders;
import com.doc_byte.eshop.orders.repository.OrderItemsRepository;
import com.doc_byte.eshop.products.dto.ProductDTO;
import com.doc_byte.eshop.users.dto.UserIdDTO;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final OrderItemsRepository orderItemsRepository;

    public AllOrders toDTO(@NotNull Orders order) {
        List<OrderItems> items = orderItemsRepository.findByOrderId(order.getId());

        List<OrderedProductDTO> orderedProducts = items.stream()
                .map(item -> new OrderedProductDTO(
                        item.getId(),
                        item.getQuantity(),
                        new ProductDTO(item.getProduct()),
                        item.getPriceAtPurchase()
                ))
                .toList();

        return new AllOrders(
                order.getId(),
                new UserIdDTO(order.getUser().getId()),
                order.getStatus(),
                order.getCreatedAt(),
                orderedProducts,
                order.getTotalPrice()
        );
    }
}
