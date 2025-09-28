# authservice

# 🔐 Authentication Service

A modern, secure, and scalable authentication system built with **Spring Boot 3**, **Java 17**, **React**, and **JWT**. This service supports role-based authentication, secure user management, and follows best practices for a stateless backend API.

- Java 17
- Spring Boot 3.5.2
- PostgreSQL 17.5
- Liquibase

## Using JWT RSA: 
### 1. Generate private key
openssl genrsa -out private-key.pem 2048

### 2. Generate public key from private key
openssl rsa -in private-key.pem -pubout -out public-key.pem

## 🧰 Tech Stack

| Layer      | Tech                                             |
|------------|--------------------------------------------------|
| Backend    | Java 17, Spring Boot 3, Spring Security          |
| Frontend   | React, React Router v6                           |
| Auth       | JWT (stateless), BCrypt for password hashing     |
| Database   | PostgreSQL                                       |
| DevOps     | Docker, GitHub Actions (CI/CD)                   |

---

## 📐 Architecture Overview

- Stateless backend using JWT (no session storage)
- PostgreSQL database for persistent user, role, and optional refresh token storage
- Secure password storage with `BCryptPasswordEncoder`
- React frontend stores JWT and uses Bearer Token for auth
- CI/CD pipeline with GitHub Actions and Docker for containerized deployment

---

## 🔄 Authentication Flow

### 1. Signup

`POST /as/auth/signup`

- Accepts user data and list of role IDs
- Roles are mapped via `roleRepository.findAllById()`
- Password is hashed using `BCrypt`
- Returns a minimal user DTO

### 2. Login

`POST /as/auth/login`

- Authenticates credentials
- Returns a JWT token (access token only)

### 3. Authorization

- JWT sent via `Authorization: Bearer <token>` header
- Custom `JwtAuthFilter` validates token and populates `SecurityContext`

---

## 📁 Project Structure
com.nest.authservice
├── controller # REST endpoints
├── service # Business logic
├── repository # Data access (JPA)
├── model # Entities & DTOs
├── security # JWT Filter, JWT Service
├── config # Security config
├── exception # Custom exceptions
└── AuthServiceApplication.java

---

## 🗃️ Database Schema

### Tables

- `users(id, email, password, first_name, last_name, created_at, updated_at)`
- `roles(id, role_name)`
- `user_roles(user_id, role_id)`
- `refresh_tokens(user_id, token, expiry)` *(optional)*

---

## 🔧 Notable Features

- Stateless authentication using JWT
- Role-based authorization
- Password hashing with BCrypt
- Change detection on user update (custom `NoChangesDetectedException`)
- Custom JWT authentication filter for secure request filtering
- Clean and modular codebase using DTOs and layered architecture
- RESTful API endpoints with proper HTTP status codes

---

## 🛡️ Security Config

- `SecurityFilterChain` uses stateless config
- CSRF disabled for REST APIs
- JWT filter excludes `/auth/signup` and `/auth/login`
- Roles injected from DB into `SecurityContext`

---

## ✅ API Endpoints

| Endpoint                  | Method | Description                      | Auth Required |
|---------------------------|--------|----------------------------------|----------------|
| `/as/auth/signup`         | POST   | Register new user with roles     | ❌             |
| `/as/auth/login`          | POST   | Login and receive JWT            | ❌             |
| `/as/auth/allUsers`       | GET    | Get list of all users            | ✅             |
| `/as/auth/updateUser`     | PUT    | Update user fields and roles     | ✅             |

---

## 📦 DTOs

- `RequestUserDTO` – For signup and updates (includes role IDs)
- `ResponseUserDTO` – Minimal user info for client
- `RequestUpdateUser` – Update input model

---

## 🚨 Exception Handling

- `UserNotFoundException`: Returns 404
- `NoChangesDetectedException`: Returns 422 with custom JSON payload

```json
{
  "status": 422,
  "path": "/as/auth/updateUser",
  "message": "No user changes detected"
}
