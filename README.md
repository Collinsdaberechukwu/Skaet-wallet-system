# Skaet-wallet-system
Skaet-wallet-system Service Application


Skaet Wallet System is a backend fintech application designed to handle secure wallet operations such as user onboarding, wallet funding, withdrawals, transfers, and transaction management.

This system is built with a focus on **scalability, reliability, and security**, incorporating industry-standard practices like **rate limiting, idempotency, and distributed locking**.

---

## Features

### User Management
- User registration with secure password hashing
- JWT-based authentication
- Role-based access control

### Wallet Operations
- Create wallet automatically on user registration
- Fund wallet
- Withdraw funds
- Transfer funds between wallets
- Reverse transactions

### Transactions
- Transaction history with filtering and pagination
- Transaction status tracking (SUCCESS, REVERSED, etc.)

###  Reliability & Safety
- **Rate Limiting (Resilience4j)** to prevent abuse
- **Idempotency** to prevent duplicate transactions
- **Redis-based locking** to prevent concurrent wallet access issues
- **Atomic transactions** using `@Transactional`

---

## Architecture


Controller → Service Layer → Repository → Database
↓
Redis (Locking & Idempotency)
↓
Resilience4j (Rate Limiting)


---

## Tech Stack

- **Java 17**
- **Spring Boot**
- **Spring Security (JWT)**
- **MySQL**
- **Redis**
- **Docker & Docker Compose**
- **Resilience4j**
- **Lombok**
- **Maven**

---

## Project Setup

###  Prerequisites

- Java 17
- Maven
- Docker & Docker Compose

---

## Running with Docker

```bash
cd docker
docker-compose up --build

Services:

Wallet API → http://localhost:8990

MySQL Database

Redis

PhpMyAdmin


API Testing (Postman)

Base URL:

http://localhost:8990/api/v1/wallets
Sample Endpoints
Register User
POST /register
Login
POST /login
Fund Wallet
POST /fund
Header: Idempotency-Key
Withdraw
POST /withdraw
Header: Idempotency-Key
Transfer
POST /transfer
Header: Idempotency-Key
Reverse Transaction
POST /reverse
Header: Idempotency-Key
Transaction History
GET /transactions/{walletId}
Security

Passwords are encrypted using PasswordEncoder

JWT tokens used for authentication

Role-based authorization implemented

Rate Limiting (Resilience4j)

Configured using fixed window strategy:

Login: 5 requests/minute

Wallet operations: 10 requests/minute

resilience4j:
  ratelimiter:
    instances:
      authLimiter:
        limitForPeriod: 5
        limitRefreshPeriod: 1m
      walletLimiter:
        limitForPeriod: 10
        limitRefreshPeriod: 1m
Idempotency Handling

Each financial request requires an Idempotency-Key

Prevents duplicate transactions

Stored in both:

Redis (fast lookup)

Database (audit/logging)

Concurrency Control

Redis-based locking:

lock:wallet:{walletId}

Prevents:

Double spending

Race conditions

Future Improvements

Integrate payment gateway (Paystack / Flutterwave)

Add circuit breaker & retry mechanisms

Add notification service (email/SMS)

Improve monitoring (Prometheus + Grafana)

Author

Collins Okafor
Backend Engineer | Java | Fintech Systems
