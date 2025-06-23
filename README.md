# authservice

# 🔐 Authentication Service

A modern, secure, and scalable authentication system built with **Spring Boot 3**, **Java 17**, **React**, and **JWT**. This service supports role-based authentication, secure user management, and follows best practices for a stateless backend API.

---

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

