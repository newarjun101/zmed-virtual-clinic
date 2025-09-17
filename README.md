# ZMed - Virtual Clinic API

**Project Name:** ZMed  
**Description:** ZMed is a virtual clinic backend API built with Kotlin and Spring Boot. It handles doctor management, appointment booking, user interactions, and real-time chat via WebSocket.

---

## Table of Contents
* [Features](#features)
* [Tech Stack](#tech-stack)
* [Requirements](#requirements)
* [Project Setup](#project-setup)
* [Database Setup](#database-setup)
* [Running the Application](#running-the-application)
* [API Documentation](#api-documentation)

---

## Features
* Manage doctors and patients
* Book appointments with availability checks
* JWT-based authentication and authorization
* Real-time chat between doctor and patient using WebSocket
* Swagger/OpenAPI integration for API docs
* Clean architecture: controller, service, repository, entity, DTO layers

---

## Tech Stack
* **Backend:** Kotlin, Spring Boot, Spring Data JPA
* **Database:** PostgreSQL
* **Authentication:** JWT (JSON Web Token)
* **Real-time Communication:** WebSocket
* **API Docs:** Swagger/OpenAPI

---

## Requirements
* Java 17+
* Gradle or Maven
* PostgreSQL 12+
* IDE: IntelliJ IDEA or VS Code (recommended)

---

## Project Setup

### 1. Clone Repository
```bash
git clone https://github.com/newarjun101/zmed-virtual-clinic
cd zmed
```

### 2. Configure Gradle/Maven
Ensure dependencies include:
* Spring Boot Starter Web
* Spring Boot Starter Data JPA
* PostgreSQL Driver
* Jackson Kotlin Module
* Spring Security
* Spring WebSocket
* Swagger/OpenAPI

---

## Database Setup (PostgreSQL)

1. **Install PostgreSQL**
* **Ubuntu:**
```bash
sudo apt update
sudo apt install postgresql postgresql-contrib
```
* **Mac (Homebrew):**
```bash
brew install postgresql
brew services start postgresql
```

2. **Create Database and User**
```sql
-- Connect to PostgreSQL
psql -U postgres

-- Create database
CREATE DATABASE zmed;

-- Create user
CREATE USER db_username WITH PASSWORD 'db_password';

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE zmed TO db_username;
```

3. **Configure application.properties / application.yml**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/zmed_db
spring.datasource.username=db_username
spring.datasource.password=db_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

---

## Running the Application

### Using Gradle
```bash
./gradlew bootRun
```

### Using Maven
```bash
mvn spring-boot:run
```

* Application will start on `http://localhost:9000` (default port).

---

## API Documentation
* Swagger UI: `http://localhost:9000/swagger-ui/index.html`
* OpenAPI JSON: `http://localhost:9000/v3/api-docs`

---

## Notes
* Use **Postman or Swagger UI** to test endpoints.
* JWT token required for protected endpoints.
* `/api/v1/auth/**` endpoints are public (login/refresh).
* Real-time chat between doctor and patient is available via WebSocket.

#### Inspired from: [Figma Online Doctor Appointment UI Kit](https://www.figma.com/design/9hk08w0Ag6LbQBpufEs9bu/Online-Doctor-Appointment-App-UI-Kit---Community--Community-?node-id=2279-14904&t=xmNdpJAPXzAHl91s-0)

