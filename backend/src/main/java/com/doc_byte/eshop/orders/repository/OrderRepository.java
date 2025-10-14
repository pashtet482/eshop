package com.doc_byte.eshop.orders.repository;

import com.doc_byte.eshop.orders.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByUser_Id(Long id);
}
