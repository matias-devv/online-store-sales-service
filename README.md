# 🧾 Sales Service

<br>

## 📌 Overview
Sales Service is the central business service of the system.
It is responsible for orchestrating the complete sales flow by coordinating multiple microservices and persisting the final business transaction.
This service does not manage users, shopping carts, products, stock, or payments directly.
Instead, it validates, aggregates, and consolidates data coming from other services to generate a consistent and immutable sale record.

<br>

## 🏗 Architecture
This service follows a classic MVC architecture, adapted to a microservices environment:

- **Controller**: REST endpoints (Spring Web MVC)
- **Service**: Business logic and orchestration
- **Repository**: Persistence layer (JPA / MySQL)
- **DTOs**: Clear separation between internal models and external contracts

The service acts as an orchestrator, not a data owner for external domains.

<br>

## 🔄 Orchestration Flow (Core Responsibility)
The sales creation process follows this sequence:

<br>

1. **User validation**

- Verifies that the user exists
- Confirms ownership of the shopping cart

<br>

2. **Shopping Cart validation**

- Ensures the cart exists
- Retrieves the total price (calculated exclusively by the Shopping Cart Service)

<br>

3. **Product validation**

- Retrieves full product information from Products Service
- Does not modify stock
- Product quantities are taken from the shopping cart

<br>

4. **Sale creation**

- Persists the Sale entity as the main transaction

<br>

5. **Sale Detail generation**

- Creates a snapshot of the sale
- Copies product data, quantities, and prices at the moment of purchase

<br>

This design guarantees data consistency and historical accuracy, which is especially important for audit and legal purposes.

<br>

## 🧩 Sale & SaleDetail Model
**Sale**
-Represents the main business transaction linked to user and shopping cart.
**SaleDetail**
- Internal entity (same repository and database) that acts as a snapshot of the transaction. Intentionally duplicates product data to avoid future dependency on external services.

This is a deliberate design decision.

<br>

## 🔗 Inter-service Communication
All communication with external services is done via **OpenFeign**:

- Users Service
- Shopping Carts Service
- Products Service

No RestTemplate, no WebClient. Contracts are explicit and strongly typed.

<br>

## 🛡 Resilience & Fault Tolerance
This service uses **Resilience4j** with:

- CircuitBreaker
- Retry Strategy

Approach:

- Fail-fast for critical dependencies (Products, Shopping Carts)
- Explicit exceptions when a dependent service is unavailable
- Controlled fallback behavior during validation steps

This avoids silent failures and prevents inconsistent sales from being created.

<br>

## ▶️ How to Run the Project
### ✅ Prerequisites

- Java 17
- Maven
- MySQL

<br>

## 🔗 Required Services (Must Be Running)
This service depends on the following microservices, which must be running before starting sales-service:

- **Eureka Server** - http://localhost:8761
- **API Gateway** - Routes all external requests
- **Users Service** (users-service) - Used to validate user existence and ownership
- **Products Service** (products-service) - Used to retrieve product information
- **Shopping Carts Service** (carts-service) - Used to validate shopping carts and calculate total price


⚠️ If any of these services are unavailable, resilience mechanisms (Circuit Breaker + Retry) will be triggered.

<br>

## 🗄️ Database Setup
Ensure the following databases exist before starting the application:

- sales_service
- products_service
- users_service
- shopping_carts_service

<br>

## ▶️ Run the Application
From the project root directory:

```
mvn spring-boot:run
```
The service will start on:
```
http://localhost:8084
```

<br>

## 🌐 Service Access


### Using API Gateway (Recommended)
```
http://localhost:8080/sales-service/sale
```
This is the recommended approach in a full microservices environment.

<br>

### Standalone Mode (Without Gateway)
```
http://localhost:8086/sale
```

Useful for local development or isolated testing.

<br>

## 📘 API Documentation (Swagger)
Once the service is running:
```
http://localhost:8086/swagger-ui.html
```
All endpoints, request/response schemas, and examples are generated automatically using Springdoc OpenAPI.

<br>

## 📚 What This Service Demonstrates

- Practical application of MVC in microservices
- Business orchestration across multiple services
- Clear separation of responsibilities
- Snapshot-based consistency model
- Resilient inter-service communication
- Clean API documentation

Despite being conceptually simple, this service consolidates core backend principles that scale correctly in distributed systems.

<br>

## 🚀 Possible Improvements

 - Global exception handling
 - Authentication and authorization
 - Stock management integration
 - Unit and integration tests
 - Feign mocks for isolated testing

<br>

## 🔑 Final Technical Positioning
- Sales Service is the Central Business Service of the system.
- It owns the sales domain logic and coordinates external services without violating domain boundaries.
