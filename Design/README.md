# DocByte — Интернет-магазин техники

DocByte — это полноценный интернет-магазин с авторизацией, корзиной, оформлением заказов и админ-панелью. Проект разработан на **Spring Boot** (backend) и **React + Vite** (frontend). Используется **PostgreSQL** для хранения данных.

## 🛠️ Технологии

### Backend:
- Java 21, Spring Boot 3+
- Spring Security (JWT)
- Spring Data JPA
- PostgreSQL
- PDF генерация (iText)
- Swagger / OpenAPI

### Frontend:
- React
- Vite
- TailwindCSS
- React Router
- Custom CSS-модули

## ⚙️ Установка

### Backend

> Требования: Java 21+, PostgreSQL

1. Клонировать репозиторий:
   ```bash
   git clone https://github.com/pashtet482/eshop.git
   cd eshop/backend
   ```

2. Создать базу данных `eshop` и настроить `application.yml`:

   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/eshop
       username: your_db_user
       password: your_db_password
   ```

3. Запустить backend:
   ```bash
   ./mvnw spring-boot:run
   ```

### Frontend

> Требования: Node.js 18+

1. Перейти в папку:
   ```bash
   cd frontend
   ```

2. Установить зависимости и запустить:
   ```bash
   npm install
   npm run dev
   ```

## 🧪 Демонстрация

**Frontend:**  
📍 [http://b438c344c53a.vps.myjino.ru](http://b438c344c53a.vps.myjino.ru)

**Swagger:**  
📍 [http://b438c344c53a.vps.myjino.ru/swagger-ui/index.html](http://b438c344c53a.vps.myjino.ru/swagger-ui/index.html)

## 📦 Функциональность

- Регистрация, вход, выход
- JWT авторизация
- Просмотр товаров, фильтрация по категориям
- Корзина
- Оформление заказа
- Админ-панель:
  - Управление товарами, пользователями, заказами
  - Генерация PDF-чека и отчётов
- Профиль пользователя: смена имени/пароля, удаление аккаунта
- Адаптивный интерфейс с поддержкой тёмной темы

## 🔐 Авторизация

- Авторизация через `POST /users/login`
- Токен передаётся в `Authorization: Bearer <token>`

## 📁 Структура

```
eshop/
├── backend/
│   ├── src/main/java/com/doc_byte/eshop
│   └── ...
├── frontend/
│   ├── src/
│   └── ...
```

## 🧑‍💻 Автор

**pashtet482**  
🔗 [github.com/pashtet482](https://github.com/pashtet482)

---

💬 Если есть предложения или баги — создавайте issue или pull request.
