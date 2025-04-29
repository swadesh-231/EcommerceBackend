
# E-Commerce Backend API (Spring Boot)

A robust and scalable RESTful API for an e-commerce platform built using Spring Boot, Spring Security, JWT, and Spring Data JPA. It supports complete backend functionality for user authentication, product management, category management, shopping cart, orders, and address handling.


## Tech Stack

**Language:** Java21

**Server:** Spring Boot,Spring Security,JWT (JSON Web Tokens) for stateless authentication

**Database:** PostgreSQL (for relational data storage),Spring Data JPA (for ORM and database interaction)

**Other Technologies:** Lombok (to reduce boilerplate code), Swagger / Postman (for API testing and documentation)



# 🚀 Features

🔐 Authentication & Authorization
- User SignUp & SignIn with JWT-based security.
- Role-based access control: USER, ADMIN, and SELLER.
- Secure password encryption using BCrypt.
- JWT cookie support with login/logout endpoints.

📦 Address Management

- Create, update, delete addresses.
- Fetch all addresses or user-specific addresses.

🛒 Cart Management

- Add products to cart.

- View or update user cart.

- Remove items from the cart.

🛍️ Product Management

- Admin can create, update, and delete products.

- Search products by category or keyword.

- Upload/update product images.

🏷️ Category Management


- Admin can create, update, and delete categories.

- Paginated and sortable category listing.

📦 Order Management

- Place orders with payment and shipping details.

- Handle multiple payment gateways (credit card, PayPal, etc.).




##  API Endpoints

#### 🔐 AuthController (/api/auth)


| Method | Endpoint    | Description                       |
| :-------- | :------- | :-------------------------------- |
| `POST`      | `/signup` | 	Register new user |
| `POST` | `/signin` | 	Authenticate user and get JWT |
| `GET`    || `/username`    || Get current user's username |
| `GET`    || `/user`    || Get current user'sinfo         |
| `POST`   || `/signout`     || Logout and clear JWT cookie   |



#### 📦 AddressController (/api/addresses)

| Method | Endpoint    | Description                       |
| :-------- | :------- | :-------------------------------- |
| POST   || /                      || Add new address                 |
| GET    || /                      || Get all addresses (admin/debug)|
| GET    || /{id}                  || Get address by ID               |
| GET    || /users/addresses       || Get current user's addresses    |
| PUT    || /{id}                  || Update address by ID            |
| DELETE || /{id}                  || Delete address by ID            |



#### 🛒 CartController (/api/carts)


| Method | Endpoint    | Description                       |
| :-------- | :------- | :-------------------------------- |
| POST   || /products/{productId}/quantity/{quantity}              || Add product to cart                |
| GET    || /                                                      || Get all carts (admin/debug)        |
| GET    || /users/cart                                            || Get current user's cart            |
| PUT    || /products/{productId}/quantity/{operation}             || Increment/decrement quantity       |
| DELETE || /{cartId}/product/{productId}                          || Remove product from cart           |





#### 🏷️CategoryController (/api/public/categories)


| Method | Endpoint    | Description                       |
| :-------- | :------- | :-------------------------------- |
| GET    || /                                    || Get paginated & sorted categories |
| POST   || /                                    || Create a new category          |
| PUT    || /{categoryId}                        || Update category                |
| DELETE || /api/admin/categories/{id}           || Delete category (admin only)   |



#### 🛍️ ProductController (/api/public/products)


| Method | Endpoint    | Description                       |
| :-------- | :------- | :-------------------------------- |
| POST   || /api/admin/categories/{id}/product                   || Add product to a category           |
| GET    || /                                                    || Get all products                    |
| GET    || /keyword/{keyword}                                   || Search products by keyword          |
| GET    || /categories/{id}/products                            || Get products by category            |
| PUT    || /api/admin/products/{id}                             || Update product                      |
| DELETE || /api/admin/products/{id}                             || Delete product                      |
| PUT    || /products/{id}/image                                 || Upload product image      



#### 📦 OrderController (/api/order)


| Method | Endpoint    | Description                       |
| :-------- | :------- | :-------------------------------- |
| POST   || /users/payments/{paymentMethod}         || Place order with payment info    |     
