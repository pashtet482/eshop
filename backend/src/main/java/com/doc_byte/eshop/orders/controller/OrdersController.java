package com.doc_byte.eshop.orders.controller;

import com.doc_byte.eshop.orders.dto.AllOrders;
import com.doc_byte.eshop.orders.dto.ChangeStatusDTO;
import com.doc_byte.eshop.orders.dto.CreateOrderDTO;
import com.doc_byte.eshop.orders.service.OrdersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrdersController {

    private final OrdersService ordersService;

    @PostMapping("/create-order")
    @Operation(summary = "Создание нового заказа")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Заказ успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<String> createOrder(@Valid @RequestBody CreateOrderDTO request){
        ordersService.createOrder(request);
        return ResponseEntity.status(HttpStatus.OK).body("Заказ успешно оформлен");
    }

    @PutMapping("/change-status")
    @Operation(summary = "Изменение статуса заказа")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Статус успешно обновлен"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<String> changeStatus(@Valid @RequestBody ChangeStatusDTO request){
        ordersService.changeStatus(request);
        return ResponseEntity.status(HttpStatus.OK).body("Статус успешно обновлен");
    }

    @GetMapping("/{id}/receipt")
    @Operation(summary = "Скачивание чека заказа")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Чек успешно скачан"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<byte[]> downloadReceipt(@PathVariable Long id) {
        byte[] pdf = ordersService.generateReceipt(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "receipt_order_" + id + ".pdf");

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Вывод заказов пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Данные успешно выведены"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<List<AllOrders>> userOrders(@PathVariable Long userId){
        return ResponseEntity.ok(ordersService.userOrders(userId));
    }

    @GetMapping("/get-all-orders")
    @Operation(summary = "Вывод все заказазов (для админа)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Данные успешно выведены"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<List<AllOrders>> allOrders(){
        return ResponseEntity.ok(ordersService.allOrders());
    }
}
