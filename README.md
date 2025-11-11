# DocByte — Online Electronics Store

**DocByte** is a full-featured online store with authentication, a shopping cart, order management, and an admin panel.  
The project is built with **Spring Boot** (backend) and **React + Vite** (frontend), using **PostgreSQL** as the main database.

## 🛠️ Technologies

### Backend:
- Java 21, Spring Boot 3+
- Spring Security (JWT)
- Spring Data JPA
- PostgreSQL
- PDF generation (iText)
- Swagger / OpenAPI

### Frontend:
- React  
- Vite  
- TailwindCSS  
- React Router  
- Custom CSS modules  

## ⚙️ Setup

### Backend

> Requirements: Java 21+, PostgreSQL

1. Clone the repository:
   ```bash
   git clone https://github.com/pashtet482/eshop.git
   cd eshop/backend
   ```

2. Create a database named `eshop` and configure your ` application.properties`:
   ```yaml
   spring:
     datasource:
       url: your_db
       username: your_db_user
       password: your_db_password
   ```

3. Run the backend:
   ```bash
   ./mvnw spring-boot:run
   ```

### Frontend

> Requirements: Node.js 18+

1. Navigate to the frontend folder:
   ```bash
   cd frontend
   ```

2. Install dependencies and start the development server:
   ```bash
   npm install
   npm run dev
   ```

## 📦 Features

- User registration, login, and logout  
- JWT authentication  
- Product browsing with category filtering  
- Shopping cart  
- Order placement  
- Admin panel:
  - Manage products, users, and orders  
  - Generate PDF receipts and reports  
- User profile: change name/password, delete account  
- Responsive design with dark mode support  

## 🔐 Authentication

- Login via `POST /users/login`  
- JWT token is passed in the `Authorization: Bearer <token>` header  

## 📁 Project Structure

```
eshop/
├── backend/
│   ├── src/main/java/com/doc_byte/eshop
│   └── ...
├── frontend/
│   ├── src/
│   └── ...
```

## 🧑‍💻 Author

**pashtet482**  
🔗 [github.com/pashtet482](https://github.com/pashtet482)

---

💬 Found a bug or have a suggestion?  
Feel free to open an issue or a pull request.
