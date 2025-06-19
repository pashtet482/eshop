package com.doc_byte.eshop.orders.repository;

import com.doc_byte.eshop.orders.model.OrderItems;
import com.doc_byte.eshop.orders.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItems, Long> {
    List<OrderItems> findByOrderId(Long orderId);
    List<OrderItems> findAllByOrder(Orders order);
}
