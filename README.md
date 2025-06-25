
# 🛒 Ecommerce Backend

A full-featured **Spring Boot** backend application for an eCommerce platform. This project is designed with a clean architecture, proper use of DTOs, controller-service-repo layers, and integrates essential modules like authentication, product management, cart operations, and order processing.

---

## 🚀 Features

- ✅ User Registration & Login (JWT-based authentication)
- ✅ Role-based access control (Admin/User)
- ✅ Product & Category Management
- ✅ Cart Management
- ✅ Order Placement
- ✅ Address Management
- ✅ RESTful APIs
- ✅ Global Exception Handling
- ✅ Clean DTO structure for API responses
- ✅ Environment-based configuration

---

## 🛠️ Technologies Used

- Java 17+
- Spring Boot
- Spring Security (JWT)
- Hibernate / JPA
- PostgreSQL (can be swapped with H2/MySQL)
- Maven

---

## 🧩 Module Overview

### 🧑 Authentication
- Signup/Login with JWT generation
- Secured endpoints using filters

### 📦 Products & Categories
- Add/Update/Delete/View products (admin)
- View products & categories (user)

### 🛒 Cart
- Add to cart / Remove from cart
- View cart for current user

### 📬 Address
- CRUD operations on user addresses

### 📦 Orders
- Place an order for items in cart
- View all past orders

---

## 📁 Folder Structure

```
src/
└── main/
    ├── java/com/project/ecommercebackend/
    │   ├── config/               # Security and app configuration
    │   ├── controller/           # REST controllers for various modules
    │   ├── dto/                  # DTOs for request/response payloads
    │   ├── model/                # Entity models
    │   ├── repository/           # JPA repositories
    │   ├── service/              # Business logic layer
    │   └── EcommerceBackendApplication.java
    └── resources/
        ├── application.properties
```

---

## 🖥️ Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/swadesh-231/EcommerceBackend.git
cd EcommerceBackend
```

### 2. Configure MySQL Database

Update your `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
spring.datasource.username=yourUsername
spring.datasource.password=yourPassword
spring.jpa.hibernate.ddl-auto=update
```

### 3. Run the Application

```bash
./mvnw spring-boot:run
```

Access the APIs on: `http://localhost:8080/api/...`

---



## 🤝 Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

---

## 📬 Contact

**Swadesh Chatterjee**  
📧 [swadeshchatterjee512@gmail.com]  
🔗 [LinkedIn](https://www.linkedin.com/in/swadeshchatterjee/)  
🔗 [GitHub](https://github.com/swadesh-231)

---

> ⭐ If you liked this project, consider giving it a star on GitHub!
