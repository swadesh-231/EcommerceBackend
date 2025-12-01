# E-Commerce Backend API

A comprehensive RESTful API for an e-commerce platform built with Spring Boot, featuring secure authentication, product management, shopping cart functionality, and order processing.

## 🚀 Features

### Authentication & Authorization
- **JWT-based Authentication** with HTTP-only cookies
- **Role-based Access Control** (USER, SELLER, ADMIN)
- Secure user registration and login
- Password encryption using BCrypt
- Protected endpoints with role-specific access

### User Management
- User registration with email validation
- Multiple user roles (User, Seller, Admin)
- User profile management
- Address management for delivery

### Product Management
- Create, read, update, and delete products
- Product categorization
- Image upload and management
- Product search by keyword
- Pagination and sorting support
- Special pricing with discount calculations

### Category Management
- Category CRUD operations
- Category-based product filtering
- Paginated category listing

### Shopping Cart
- Add products to cart
- Update product quantities
- Remove products from cart
- Calculate total cart amount
- Automatic price and discount updates
- Cart persistence per user

### Order Processing
- Place orders from cart
- Multiple payment method support
- Payment gateway integration (PG details tracking)
- Order status tracking
- Automatic inventory management
- Order history with detailed items

### Address Management
- Multiple delivery addresses per user
- CRUD operations for addresses
- Address validation
- Link addresses to orders

## 🛠️ Technology Stack

- **Framework**: Spring Boot 3.x
- **Security**: Spring Security with JWT
- **Database**: PostgreSQL
- **ORM**: JPA/Hibernate
- **Validation**: Jakarta Validation
- **Documentation**: SpringDoc OpenAPI (Swagger)
- **Mapping**: ModelMapper
- **Build Tool**: Maven

## 📋 Prerequisites

- Java 17 or higher
- PostgreSQL 12 or higher
- Maven 3.6 or higher

## ⚙️ Configuration

### Database Setup

1. Create a PostgreSQL database:
```sql
CREATE DATABASE Ecommerce;
```

2. Update `application.properties` with your database credentials:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/Ecommerce
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### JWT Configuration

Configure JWT settings in `application.properties`:
```properties
spring.app.jwtSecret=your_secret_key
spring.app.jwtExpirationMs=3000000
spring.ecom.app.jwtCookieName=springBootEcommerce
```

### Image Upload

Set the image upload directory:
```properties
project.image=images/
```

## 🚦 Getting Started

1. **Clone the repository**
```bash
git clone <repository-url>
cd ecommerce-backend
```

2. **Build the project**
```bash
mvn clean install
```

3. **Run the application**
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## 📚 API Endpoints

### Authentication
- `POST /api/auth/signup` - Register new user
- `POST /api/auth/signin` - User login
- `POST /api/auth/signout` - User logout
- `GET /api/auth/user` - Get current user details
- `GET /api/auth/username` - Get current username

### Categories
- `GET /api/public/categories` - Get all categories (paginated)
- `POST /api/public/categories` - Create new category
- `PUT /api/public/categories/{categoryId}` - Update category
- `DELETE /api/admin/categories/{categoryId}` - Delete category (Admin only)

### Products
- `GET /api/public/products` - Get all products (paginated)
- `POST /api/admin/categories/{categoryId}/product` - Add product (Admin only)
- `GET /api/public/categories/{categoryId}/products` - Get products by category
- `GET /api/public/products/keyword/{keyword}` - Search products
- `PUT /api/admin/products/{productId}` - Update product (Admin only)
- `DELETE /api/admin/products/{productId}` - Delete product (Admin only)
- `PUT /api/products/{productId}/image` - Update product image

### Cart
- `POST /api/carts/products/{productId}/quantity/{quantity}` - Add product to cart
- `GET /api/carts` - Get all carts
- `GET /api/carts/users/cart` - Get user's cart
- `PUT /api/cart/products/{productId}/quantity/{operation}` - Update cart item quantity
- `DELETE /api/carts/{cartId}/product/{productId}` - Remove product from cart

### Orders
- `POST /api/order/users/payments/{paymentMethod}` - Place order

### Addresses
- `POST /api/addresses` - Create address
- `GET /api/addresses` - Get all addresses
- `GET /api/addresses/{addressId}` - Get address by ID
- `GET /api/addresses/users/addresses` - Get user addresses
- `PUT /api/addresses/{addressId}` - Update address
- `DELETE /api/addresses/{addressId}` - Delete address

## 🔐 Default Users

The application creates default users on startup:

1. **User**
   - Username: `user1`
   - Email: `user1@example.com`
   - Password: `password1`
   - Role: USER

2. **Seller**
   - Username: `seller1`
   - Email: `seller1@example.com`
   - Password: `password2`
   - Role: SELLER

3. **Admin**
   - Username: `admin`
   - Email: `admin@example.com`
   - Password: `adminPass`
   - Roles: USER, SELLER, ADMIN

## 📖 API Documentation

Access Swagger UI for interactive API documentation:
```
http://localhost:8080/swagger-ui.html
```

## 🏗️ Project Structure

```
src/main/java/com/project/ecommercebackend/
├── config/              # Application configuration
├── controller/          # REST controllers
├── dto/                 # Data Transfer Objects
├── exception/           # Custom exceptions and handlers
├── model/               # Entity classes
├── repository/          # JPA repositories
├── security/            # Security configuration and JWT utilities
├── service/             # Business logic services
└── utils/               # Utility classes
```

## 🔒 Security Features

- JWT token-based authentication
- HTTP-only cookies for token storage
- CSRF protection disabled (stateless REST API)
- BCrypt password encoding
- Role-based access control
- Protected endpoints
- Stateless session management

## 📦 Key Dependencies

- Spring Boot Starter Web
- Spring Boot Starter Security
- Spring Boot Starter Data JPA
- PostgreSQL Driver
- JSON Web Token (JWT)
- ModelMapper
- Lombok
- Jakarta Validation
- SpringDoc OpenAPI

## 🎯 Business Logic Highlights

### Automatic Price Calculation
- Special prices calculated automatically: `specialPrice = price - (price × discount%)`
- Cart totals updated in real-time

### Inventory Management
- Stock validation before adding to cart
- Automatic inventory deduction on order placement
- Quantity tracking per product

### Cart Management
- Prevents duplicate products in cart
- Automatic quantity updates
- Price synchronization with product changes

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request


