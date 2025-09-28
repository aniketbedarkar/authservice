# authservice
User Authentication and Authorization Microservice


## 🛠️ Tech Stack

- Java 17
- Spring Boot 3.5.2
- PostgreSQL 17.5
- Liquibase

## Using JWT RSA: 
### 1. Generate private key
openssl genrsa -out private-key.pem 2048

### 2. Generate public key from private key
openssl rsa -in private-key.pem -pubout -out public-key.pem