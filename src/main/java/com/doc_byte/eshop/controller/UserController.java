package com.doc_byte.eshop.controller;

import com.doc_byte.eshop.dto.ChangeUsernameRequest;
import com.doc_byte.eshop.dto.CreateUserRequest;
import com.doc_byte.eshop.dto.DeleteUserRequest;
import com.doc_byte.eshop.dto.PasswordChangeRequest;
import com.doc_byte.eshop.model.User;
import com.doc_byte.eshop.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Вывести всех пользователей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список пользователей получен"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping
    @Operation(summary = "Создание пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные пользователя"),
            @ApiResponse(responseCode = "409", description = "Пользователь с таким именем или email уже существует"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<String> createUser(@Valid @RequestBody CreateUserRequest request) {
        userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Аккаунт успешно создан");
    }

    @PostMapping("/change-password")
    @Operation(summary = "Смена пароля")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пароль успешно изменен"),
            @ApiResponse(responseCode = "400", description = "Неверное имя пользователя или пароль"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<String> changePassword(@Valid @RequestBody @NotNull PasswordChangeRequest request) {
        userService.changePassword(request.username(), request.oldPassword(), request.newPassword());
        return ResponseEntity.ok("Пароль успешно изменен");
    }

    @DeleteMapping("/delete-user")
    @Operation(summary = "Удаление пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Аккаунт успешно удален"),
            @ApiResponse(responseCode = "400", description = "Неверный пароль"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<String> deleteUser(@Valid @RequestBody @NotNull DeleteUserRequest request) {
        userService.deleteUser(request.username(), request.password());
        return ResponseEntity.ok("Аккаунт успешно удален");
    }

    @PostMapping("/update-username")
    @Operation(summary = "Смена имени пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Имя пользователя успешно изменено"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "409", description = "Имя уже занято, введите другое"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<String> changeUsername(@Valid @RequestBody @NotNull ChangeUsernameRequest request) {
        if (!userService.hasPermissionToChangeUsername(request.oldUsername())) {
            throw new AccessDeniedException("Имя можно имзенять раз в 30 дней");
        }
        userService.changeUsername(request.oldUsername(), request.newUsername());
        return ResponseEntity.ok("Имя пользователя успешно изменено");
    }
}
