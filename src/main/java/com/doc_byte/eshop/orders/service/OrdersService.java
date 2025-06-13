package com.doc_byte.eshop.orders.service;

import com.doc_byte.eshop.exceptions.ConflictException;
import com.doc_byte.eshop.exceptions.NotFoundException;
import com.doc_byte.eshop.orders.dto.CreateOrderDTO;
import com.doc_byte.eshop.orders.dto.OrderItemsDTO;
import com.doc_byte.eshop.orders.model.OrderItems;
import com.doc_byte.eshop.orders.model.Orders;
import com.doc_byte.eshop.orders.repository.OrderItemsRepository;
import com.doc_byte.eshop.orders.repository.OrderRepository;
import com.doc_byte.eshop.products.model.Product;
import com.doc_byte.eshop.products.repository.ProductsRepository;
import com.doc_byte.eshop.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdersService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductsRepository productsRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final ReceiptPdfGenerator receiptPdfGenerator;

    public void createOrder(@NotNull CreateOrderDTO request){
        Orders order = new Orders();
        order.setUser(userRepository.findById(request.userId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден")));
        order.setCreatedAt(Instant.now());
        order.setStatus("PENDING");

        order.setTotalPrice(BigDecimal.valueOf(0.0));
        order = orderRepository.save(order);

        BigDecimal totalPrice = BigDecimal.ZERO;

        for (OrderItemsDTO item : request.orderItems()) {
            Product product = productsRepository.findById(item.productId())
                    .orElseThrow(() -> new NotFoundException("Товар не найден"));

            if (product.getStockQuantity() < item.quantity()) {
                throw new ConflictException("Недостаточно товара на складе: " + product.getName());
            }

            OrderItems orderItem = new OrderItems();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(item.quantity());
            orderItem.setPriceAtPurchase(product.getPrice());

            orderItemsRepository.save(orderItem);

            BigDecimal price = product.getPrice();
            int quantity = item.quantity();
            totalPrice = price.multiply(BigDecimal.valueOf(quantity));

            product.setStockQuantity(product.getStockQuantity() - item.quantity());
            productsRepository.save(product);
        }

        order.setTotalPrice(totalPrice);
        orderRepository.save(order);

        List<OrderItems> items = orderItemsRepository.findByOrderId(order.getId());
        byte[] pdf = receiptPdfGenerator.generate(order, items);

        try{
            Files.createDirectories(Paths.get("receipts"));
        }
        catch (IOException e){
            throw new RuntimeException("Ошибка при создании директроии", e);
        }

        String filePath = "receipts/order_" + order.getId() + ".pdf";
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(pdf);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении чека", e);
        }
    }
}
