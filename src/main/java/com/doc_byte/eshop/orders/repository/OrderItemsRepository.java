package com.doc_byte.eshop.orders.repository;

import com.doc_byte.eshop.orders.model.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItems, Long> {

}
