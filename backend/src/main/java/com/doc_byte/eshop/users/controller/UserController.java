package com.doc_byte.eshop.users.controller;

import com.doc_byte.eshop.users.dto.*;
import com.doc_byte.eshop.users.model.User;
import com.doc_byte.eshop.users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import javafx.util.Pair;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/get-all-users")
    @Operation(summary = "Вывести всех пользователей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список пользователей получен"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<List<GetAllUsers>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/create-user")
    @Operation(summary = "Создание пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные пользователя"),
            @ApiResponse(responseCode = "409", description = "Пользователь с таким именем или email уже существует"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<Map<String, Object>> createUser(@Valid @RequestBody CreateUserRequest request) {
        User user = userService.createUser(request);

        if (user != null) {
            return ResponseEntity.ok(Map.of(
                    "userId", user.getId(),
                    "username", user.getUsername(),
                    "isAdmin", user.getRole()
            ));
        }
        return ResponseEntity.status(400).body(Map.of("error", "Неверный логин или пароль"));
    }

    @PatchMapping("/change-password")
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

    @PatchMapping("/update-username")
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
        Pair<Integer, User> result = userService.getDaysUntilUsernameChangeAllowed(request.oldUsername());
        int daysLeft = result.getKey();

        if (daysLeft > 0) {
            throw new AccessDeniedException(
                    String.format("Имя можно изменить только через %d дней", daysLeft)
            );
        }

        userService.changeUsername(request.oldUsername(), request.newUsername());
        return ResponseEntity.ok("Имя пользователя успешно изменено");
    }

    @PostMapping("/login")
    @Operation(summary = "Вход в аккаунт")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный вход, возвращает токен и данные пользователя"),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос (неверный формат email/пароля)"),
            @ApiResponse(responseCode = "401", description = "Неверные учетные данные (email или пароль)"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен (пользователь заблокирован или нет прав)"),
            @ApiResponse(responseCode = "404", description = "Пользователь с указанным email не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginDTO loginDTO){
        User user = userService.login(loginDTO);

        if (user != null) {
            return ResponseEntity.ok(Map.of(
                    "userId", user.getId(),
                    "username", user.getUsername(),
                    "isAdmin", user.getRole()
            ));
        }
        return ResponseEntity.status(400).body(Map.of("error", "Неверный логин или пароль"));
    }

    @GetMapping("/profile-{username}")
    @Operation(summary = "Вывод данных о пользователе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Дааные выведены"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<Map<String, Object>> userInfo(@PathVariable String username, @NotNull UserInfoDTO infoDTO){
        Pair<Integer, User> result = userService.getDaysUntilUsernameChangeAllowed(username);

        int daysLeft = result.getKey();
        User user = result.getValue();

        return ResponseEntity.ok(Map.of(
                "username", user.getUsername(),
                "email", user.getEmail(),
                "Creation date", user.getCreatedAt(),
                "Days to change username", daysLeft
        ));
    }

    @PutMapping("/change-role")
    @Operation(summary = "Смена роли пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Роль изменена"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public void changeUserRole(@RequestBody @NotNull ChangeUserRole changeUserRole){
        userService.changeUserRole(changeUserRole);
    }
}
