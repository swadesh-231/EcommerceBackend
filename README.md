# E-Commerce Backend API

A comprehensive RESTful API for an e-commerce platform built with Spring Boot, featuring JWT authentication, role-based access control, and complete shopping cart & order management functionality.

## Features

### Authentication & Authorization
- JWT-based stateless authentication with cookie storage
- Role-based access control (USER, SELLER, ADMIN)
- Secure password encryption with BCrypt
- User registration and login

### Product Management
- CRUD operations for products
- Product categorization
- Image upload functionality
- Search products by keyword
- Filter products by category
- Pagination and sorting support
- Stock management
- Automatic special price calculation based on discount

### Shopping Cart
- Add/Remove products from cart
- Update product quantities
- Real-time price calculations
- Cart persistence per user
- View all carts (Admin only)

### Order Management
- Place orders with multiple items
- Payment information tracking
- Address management for delivery
- Order confirmation with payment gateway details

### Category Management
- CRUD operations for categories
- Pagination support
- Product listing by category

### Address Management
- Multiple addresses per user
- CRUD operations for addresses
- Address validation

## Tech Stack

- **Framework:** Spring Boot 3.x
- **Security:** Spring Security with JWT
- **Database:** PostgreSQL
- **ORM:** Spring Data JPA / Hibernate
- **Validation:** Jakarta Validation
- **Mapping:** ModelMapper
- **Build Tool:** Maven
- **Java Version:** 17+

## Getting Started

### Prerequisites
- JDK 17 or higher
- PostgreSQL 12+
- Maven 3.6+

### Installation

1. Clone the repository
```bash
git clone https://github.com/yourusername/ecommerce-backend.git
cd ecommerce-backend
```

2. Create PostgreSQL database
```sql
CREATE DATABASE Ecommerce;
```

3. Update `src/main/resources/application.properties`
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/Ecommerce
spring.datasource.username=your_username
spring.datasource.password=your_password

spring.app.jwtSecret=your_secret_key_here
spring.app.jwtExpirationMs=3000000
spring.ecom.app.jwtCookieName=springBootEcommerce

project.image=images/
```

4. Build the project
```bash
mvn clean install
```

5. Run the application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Default Users

The application creates default users on startup:

| Username | Password | Role |
|----------|----------|------|
| user1 | password1 | USER |
| seller1 | password2 | SELLER |
| admin | adminPass | ADMIN |

## API Endpoints

### Authentication

#### Register User
```
POST /api/auth/signup
Content-Type: application/json

{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "password123",
  "role": ["user"]
}
```

#### Login
```
POST /api/auth/signin
Content-Type: application/json

{
  "username": "john_doe",
  "password": "password123"
}
```

#### Get Current User
```
GET /api/auth/user
```

#### Get Username
```
GET /api/auth/username
```

#### Logout
```
POST /api/auth/signout
```

### Categories

#### Get All Categories (Public)
```
GET /api/public/categories?pageNumber=0&pageSize=10&sortBy=categoryId&sortOrder=asc
```

#### Create Category (Public)
```
POST /api/public/categories
Content-Type: application/json

{
  "categoryName": "Electronics"
}
```

#### Update Category (Public)
```
PUT /api/public/categories/{categoryId}
Content-Type: application/json

{
  "categoryName": "Updated Electronics"
}
```

#### Delete Category (Admin Only)
```
DELETE /api/admin/categories/{categoryId}
```

### Products

#### Get All Products (Public)
```
GET /api/public/products?pageNumber=0&pageSize=10&sortBy=productId&sortOrder=asc
```

#### Get Products by Category (Public)
```
GET /api/public/categories/{categoryId}/products?pageNumber=0&pageSize=10&sortBy=productId&sortOrder=asc
```

#### Search Products by Keyword (Public)
```
GET /api/public/products/keyword/{keyword}?pageNumber=0&pageSize=10&sortBy=productId&sortOrder=asc
```

#### Add Product (Admin Only)
```
POST /api/admin/categories/{categoryId}/product
Content-Type: application/json

{
  "productName": "iPhone 14",
  "productDescription": "Latest Apple iPhone",
  "quantity": 50,
  "price": 999.99,
  "discount": 10
}
```

#### Update Product (Admin Only)
```
PUT /api/admin/products/{productId}
Content-Type: application/json

{
  "productName": "iPhone 14 Pro",
  "productDescription": "Updated description",
  "quantity": 30,
  "price": 1099.99,
  "discount": 5
}
```

#### Delete Product (Admin Only)
```
DELETE /api/admin/products/{productId}
```

#### Upload Product Image
```
PUT /api/products/{productId}/image
Content-Type: multipart/form-data
image: <file>
```

### Cart

#### Add Product to Cart
```
POST /api/carts/products/{productId}/quantity/{quantity}
```

#### Get All Carts (Admin)
```
GET /api/carts
```

#### Get User's Cart
```
GET /api/carts/users/cart
```

#### Update Product Quantity in Cart
```
PUT /api/cart/products/{productId}/quantity/{operation}
```
*operation: "add" (adds 1) or "delete" (removes 1)*

#### Remove Product from Cart
```
DELETE /api/carts/{cartId}/product/{productId}
```

### Address

#### Create Address
```
POST /api/addresses
Content-Type: application/json

{
  "street": "123 Main St",
  "buildingName": "Apartment A",
  "city": "New York",
  "state": "NY",
  "country": "USA",
  "pincode": "10001"
}
```

#### Get All Addresses
```
GET /api/addresses
```

#### Get Address by ID
```
GET /api/addresses/{addressId}
```

#### Get User's Addresses
```
GET /api/addresses/users/addresses
```

#### Update Address
```
PUT /api/addresses/{addressId}
Content-Type: application/json

{
  "street": "456 New St",
  "buildingName": "Building B",
  "city": "Los Angeles",
  "state": "CA",
  "country": "USA",
  "pincode": "90001"
}
```

#### Delete Address
```
DELETE /api/addresses/{addressId}
```

### Orders

#### Place Order
```
POST /api/order/users/payments/{paymentMethod}
Content-Type: application/json

{
  "addressId": 1,
  "pgName": "Stripe",
  "pgPaymentId": "pi_1234567890",
  "pgStatus": "SUCCESS",
  "pgResponseMessage": "Payment completed successfully"
}
```

## Project Structure

```
src/main/java/com/project/ecommercebackend/
├── config/                     # Configuration classes
│   ├── AppConfig.java
│   └── AppConstants.java
├── controller/                 # REST Controllers
│   ├── AddressController.java
│   ├── AuthController.java
│   ├── CartController.java
│   ├── CategoryController.java
│   ├── OrderController.java
│   └── ProductController.java
├── dto/                        # Data Transfer Objects
│   ├── AddressDTO.java
│   ├── CartDTO.java
│   ├── CategoryDTO.java
│   ├── OrderDTO.java
│   ├── ProductDTO.java
│   └── ...
├── exception/                  # Exception handling
│   ├── APIException.java
│   ├── ResourceNotFoundException.java
│   └── MyGlobalExceptionHandler.java
├── model/                      # Entity classes
│   ├── Address.java
│   ├── Cart.java
│   ├── CartItem.java
│   ├── Category.java
│   ├── Order.java
│   ├── OrderItem.java
│   ├── Payment.java
│   ├── Product.java
│   ├── Role.java
│   └── User.java
├── repository/                 # JPA Repositories
│   ├── AddressRepository.java
│   ├── CartRepository.java
│   ├── CategoryRepository.java
│   ├── OrderRepository.java
│   ├── ProductRepository.java
│   └── ...
├── security/                   # Security configuration
│   ├── SecurityConfig.java
│   ├── jwt/
│   ├── request/
│   ├── response/
│   └── service/
├── service/                    # Service interfaces
│   └── impl/                   # Service implementations
└── utils/                      # Utility classes
    └── AuthUtil.java
```

## Database Schema

### Main Entities

- **users**: User accounts and authentication
- **roles**: User roles (ROLE_USER, ROLE_SELLER, ROLE_ADMIN)
- **user_role**: Many-to-many relationship between users and roles
- **products**: Product catalog
- **categories**: Product categories
- **cart**: Shopping cart for each user
- **cart_items**: Items in shopping cart
- **orders**: Customer orders
- **order_items**: Items in an order
- **payments**: Payment information
- **addresses**: Delivery addresses

### Key Relationships

- User → Cart (One-to-One)
- User → Address (One-to-Many)
- User → Role (Many-to-Many)
- Cart → CartItem (One-to-Many)
- Category → Product (One-to-Many)
- Product → CartItem (One-to-Many)
- Order → OrderItem (One-to-Many)
- Order → Payment (One-to-One)
- Order → Address (Many-to-One)

## Security

### JWT Authentication
- Tokens are stored in HTTP-only cookies
- Token expiration: 50 minutes (3000000ms)
- Cookie path: `/api`
- Cookie name: `springBootEcommerce`

### Password Security
- Passwords are encrypted using BCrypt
- Minimum password length: 6 characters

### Role-Based Access Control
- **Public endpoints**: Authentication, product browsing, category viewing
- **User endpoints**: Cart management, order placement, address management
- **Admin endpoints**: Product/category deletion, user management

## Error Handling

The API uses a global exception handler that returns consistent error responses:

```json
{
  "message": "Error message here",
  "status": false
}
```

### Common HTTP Status Codes
- `200 OK`: Successful GET, PUT requests
- `201 CREATED`: Successful POST requests
- `204 NO CONTENT`: Successful DELETE requests
- `400 BAD REQUEST`: Validation errors, business logic errors
- `401 UNAUTHORIZED`: Authentication required
- `404 NOT FOUND`: Resource not found

## Validation

Input validation is implemented using Jakarta Validation annotations:

### Address Validation
- Street: minimum 5 characters
- Building Name: minimum 5 characters
- City: minimum 4 characters
- State: minimum 2 characters
- Country: minimum 2 characters
- Pincode: minimum 5 characters

### User Validation
- Username: 3-20 characters
- Email: valid email format
- Password: 6-40 characters

### Category Validation
- Category Name: required, not blank

## Features Implemented

✅ User registration and authentication  
✅ JWT-based security  
✅ Role-based access control  
✅ Product CRUD operations  
✅ Category management  
✅ Shopping cart functionality  
✅ Order placement  
✅ Address management  
✅ Payment information tracking  
✅ Image upload for products  
✅ Search and filtering  
✅ Pagination and sorting  
✅ Global exception handling  
✅ Input validation  

## Notes

- The application automatically creates default users and roles on startup
- Product special prices are calculated automatically based on discount percentage
- Cart totals are updated automatically when items are added/removed
- Product quantities are decremented when orders are placed
- All authenticated endpoints require a valid JWT token in cookies

## Future Enhancements

Potential improvements for this project:
- Email notifications for orders
- Order status tracking
- Product reviews and ratings
- Wishlist functionality
- Admin dashboard
- Payment gateway integration
- Unit and integration tests
- API documentation with Swagger
- Caching with Redis
- File storage with AWS S3

## License

This project is open source and available under the MIT License.

## Author

Your Name  
GitHub: [swadesh-231]((https://github.com/swadesh-231))  
Email: swadeshchatterjee512@gmail.com
