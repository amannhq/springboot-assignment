# 🚗 RideShare Backend - Implementation Plan

## Project Overview
Build a mini Ride Sharing backend using **Spring Boot 3**, **MongoDB**, **JWT Authentication**, **Input Validation**, and **Global Exception Handling**.

---

## 📁 Folder Structure

```
src/
 ├── main/
 │    ├── java/
 │    │     └── org/example/rideshare/
 │    │           ├── RideShareApplication.java       # Main Application Entry Point
 │    │           ├── model/
 │    │           │     ├── User.java                 # User Entity
 │    │           │     └── Ride.java                 # Ride Entity
 │    │           ├── repository/
 │    │           │     ├── UserRepository.java       # User MongoDB Repository
 │    │           │     └── RideRepository.java       # Ride MongoDB Repository
 │    │           ├── service/
 │    │           │     ├── AuthService.java          # Authentication Service
 │    │           │     ├── UserService.java          # User Business Logic
 │    │           │     └── RideService.java          # Ride Business Logic
 │    │           ├── controller/
 │    │           │     ├── AuthController.java       # Auth Endpoints
 │    │           │     ├── RideController.java       # Ride Endpoints (USER)
 │    │           │     └── DriverController.java     # Driver Endpoints (DRIVER)
 │    │           ├── config/
 │    │           │     └── SecurityConfig.java       # Spring Security + JWT Config
 │    │           ├── dto/
 │    │           │     ├── request/
 │    │           │     │     ├── RegisterRequest.java
 │    │           │     │     ├── LoginRequest.java
 │    │           │     │     └── CreateRideRequest.java
 │    │           │     └── response/
 │    │           │           ├── AuthResponse.java
 │    │           │           ├── RideResponse.java
 │    │           │           └── ErrorResponse.java
 │    │           ├── exception/
 │    │           │     ├── GlobalExceptionHandler.java
 │    │           │     ├── NotFoundException.java
 │    │           │     ├── BadRequestException.java
 │    │           │     └── UnauthorizedException.java
 │    │           └── util/
 │    │                 └── JwtUtil.java              # JWT Token Utility
 │    └── resources/
 │            └── application.properties
 └── test/
       └── java/
```

---

## 📋 Implementation Tasks

### Phase 1: Project Setup & Configuration
| Task # | Task | Description | Status |
|--------|------|-------------|--------|
| 1.1 | Update `pom.xml` | Add Spring Boot, MongoDB, Security, JWT, Validation dependencies | ⬜ |
| 1.2 | Create `application.properties` | MongoDB connection, JWT secret, server port (8081) | ⬜ |
| 1.3 | Create Main Application Class | `RideShareApplication.java` with `@SpringBootApplication` | ⬜ |

### Phase 2: Models (Entities)
| Task # | Task | Description | Status |
|--------|------|-------------|--------|
| 2.1 | Create `User.java` | id, username, password, role (ROLE_USER/ROLE_DRIVER) | ⬜ |
| 2.2 | Create `Ride.java` | id, userId, driverId, pickupLocation, dropLocation, status, createdAt | ⬜ |

### Phase 3: Repositories
| Task # | Task | Description | Status |
|--------|------|-------------|--------|
| 3.1 | Create `UserRepository.java` | MongoRepository with `findByUsername()` | ⬜ |
| 3.2 | Create `RideRepository.java` | MongoRepository with custom queries for status, userId, driverId | ⬜ |

### Phase 4: DTOs & Validation
| Task # | Task | Description | Status |
|--------|------|-------------|--------|
| 4.1 | Create `RegisterRequest.java` | @NotBlank username, password, role with validation | ⬜ |
| 4.2 | Create `LoginRequest.java` | @NotBlank username, password | ⬜ |
| 4.3 | Create `CreateRideRequest.java` | @NotBlank pickupLocation, dropLocation | ⬜ |
| 4.4 | Create `AuthResponse.java` | token, username, role | ⬜ |
| 4.5 | Create `RideResponse.java` | All ride fields for API response | ⬜ |
| 4.6 | Create `ErrorResponse.java` | error, message, timestamp | ⬜ |

### Phase 5: Exception Handling
| Task # | Task | Description | Status |
|--------|------|-------------|--------|
| 5.1 | Create `NotFoundException.java` | Custom exception for 404 errors | ⬜ |
| 5.2 | Create `BadRequestException.java` | Custom exception for 400 errors | ⬜ |
| 5.3 | Create `UnauthorizedException.java` | Custom exception for 401 errors | ⬜ |
| 5.4 | Create `GlobalExceptionHandler.java` | @ControllerAdvice with all exception handlers | ⬜ |

### Phase 6: JWT Utility & Security
| Task # | Task | Description | Status |
|--------|------|-------------|--------|
| 6.1 | Create `JwtUtil.java` | Generate token, validate token, extract claims | ⬜ |
| 6.2 | Create `JwtAuthenticationFilter.java` | OncePerRequestFilter for JWT validation | ⬜ |
| 6.3 | Create `CustomUserDetailsService.java` | Implement UserDetailsService for Spring Security | ⬜ |
| 6.4 | Create `SecurityConfig.java` | Configure security filter chain, BCrypt, permit auth endpoints | ⬜ |

### Phase 7: Services (Business Logic)
| Task # | Task | Description | Status |
|--------|------|-------------|--------|
| 7.1 | Create `AuthService.java` | Register (BCrypt), Login (JWT generation) | ⬜ |
| 7.2 | Create `UserService.java` | Get user by username, get current user | ⬜ |
| 7.3 | Create `RideService.java` | Create ride, accept ride, complete ride, get rides | ⬜ |

### Phase 8: Controllers (API Endpoints)
| Task # | Task | Description | Status |
|--------|------|-------------|--------|
| 8.1 | Create `AuthController.java` | POST /api/auth/register, POST /api/auth/login | ⬜ |
| 8.2 | Create `RideController.java` | POST /api/v1/rides, GET /api/v1/user/rides, POST /api/v1/rides/{id}/complete | ⬜ |
| 8.3 | Create `DriverController.java` | GET /api/v1/driver/rides/requests, POST /api/v1/driver/rides/{id}/accept | ⬜ |

---

## 🔐 API Endpoints Summary

| Role | Method | Endpoint | Description |
|------|--------|----------|-------------|
| PUBLIC | POST | `/api/auth/register` | Register new user |
| PUBLIC | POST | `/api/auth/login` | Login & get JWT token |
| USER | POST | `/api/v1/rides` | Create new ride request |
| USER | GET | `/api/v1/user/rides` | Get logged-in user's rides |
| DRIVER | GET | `/api/v1/driver/rides/requests` | Get all pending ride requests |
| DRIVER | POST | `/api/v1/driver/rides/{rideId}/accept` | Accept a ride request |
| USER/DRIVER | POST | `/api/v1/rides/{rideId}/complete` | Complete a ride |

---

## 🔧 Dependencies (pom.xml)

```xml
<!-- Spring Boot Starter Parent: 3.2.0+ -->
- spring-boot-starter-web
- spring-boot-starter-data-mongodb
- spring-boot-starter-security
- spring-boot-starter-validation
- lombok
- jjwt-api (0.12.3)
- jjwt-impl (0.12.3)
- jjwt-jackson (0.12.3)
```

---

## ⚙️ Configuration (application.properties)

```properties
# Server
server.port=8081

# MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017/rideshare

# JWT
jwt.secret=your-256-bit-secret-key-here-minimum-32-chars
jwt.expiration=86400000

# Logging
logging.level.org.springframework.security=DEBUG
```

---

## 📊 Entity Definitions

### User Entity
| Field | Type | Description |
|-------|------|-------------|
| id | String | MongoDB ObjectId (auto-generated) |
| username | String | Unique username |
| password | String | BCrypt encoded password |
| role | String | ROLE_USER or ROLE_DRIVER |

### Ride Entity
| Field | Type | Description |
|-------|------|-------------|
| id | String | MongoDB ObjectId (auto-generated) |
| userId | String | Passenger's user ID (FK) |
| driverId | String | Driver's user ID (FK, nullable) |
| pickupLocation | String | Pickup location |
| dropLocation | String | Drop location |
| status | String | REQUESTED / ACCEPTED / COMPLETED |
| createdAt | Date | Timestamp of ride creation |

---

## 🔄 Status Flow

```
REQUESTED  →  ACCEPTED  →  COMPLETED
    ↑             ↑            ↑
  (User        (Driver      (User or
 creates)     accepts)      Driver)
```

---

## 🛡️ Security Configuration

1. **Public Endpoints**: `/api/auth/**` - No authentication required
2. **User Endpoints**: `/api/v1/rides/**`, `/api/v1/user/**` - Requires ROLE_USER
3. **Driver Endpoints**: `/api/v1/driver/**` - Requires ROLE_DRIVER
4. **JWT Filter**: Validates token on every request (except public)
5. **Password Encoding**: BCryptPasswordEncoder

---

## 📝 Validation Rules

| DTO | Field | Validation |
|-----|-------|------------|
| RegisterRequest | username | @NotBlank, @Size(min=3, max=50) |
| RegisterRequest | password | @NotBlank, @Size(min=4) |
| RegisterRequest | role | @NotBlank, @Pattern(ROLE_USER\|ROLE_DRIVER) |
| LoginRequest | username | @NotBlank |
| LoginRequest | password | @NotBlank |
| CreateRideRequest | pickupLocation | @NotBlank |
| CreateRideRequest | dropLocation | @NotBlank |

---

## 🚨 Error Response Format

```json
{
  "error": "VALIDATION_ERROR",
  "message": "Pickup is required",
  "timestamp": "2025-12-06T12:00:00Z"
}
```

---

## ✅ Checklist for Submission

- [ ] Complete functioning API
- [ ] Proper folder structure (as specified)
- [ ] DTOs with Jakarta Validation
- [ ] Global Exception Handling
- [ ] JWT Authentication implemented correctly
- [ ] BCrypt password encoding
- [ ] Role-based authorization (USER vs DRIVER)
- [ ] README explaining endpoints
- [ ] Postman collection (optional)

---

## 🧪 Testing Commands (curl)

### Register User
```bash
curl -X POST http://localhost:8081/api/auth/register \
-H "Content-Type: application/json" \
-d '{"username":"john","password":"1234","role":"ROLE_USER"}'
```

### Register Driver
```bash
curl -X POST http://localhost:8081/api/auth/register \
-H "Content-Type: application/json" \
-d '{"username":"driver1","password":"abcd","role":"ROLE_DRIVER"}'
```

### Login
```bash
curl -X POST http://localhost:8081/api/auth/login \
-H "Content-Type: application/json" \
-d '{"username":"john","password":"1234"}'
```

### Create Ride (with token)
```bash
curl -X POST http://localhost:8081/api/v1/rides \
-H "Content-Type: application/json" \
-H "Authorization: Bearer <token>" \
-d '{"pickupLocation":"Koramangala","dropLocation":"Indiranagar"}'
```

### Get User's Rides
```bash
curl -X GET http://localhost:8081/api/v1/user/rides \
-H "Authorization: Bearer <token>"
```

### Get Pending Requests (Driver)
```bash
curl -X GET http://localhost:8081/api/v1/driver/rides/requests \
-H "Authorization: Bearer <token>"
```

### Accept Ride (Driver)
```bash
curl -X POST http://localhost:8081/api/v1/driver/rides/{rideId}/accept \
-H "Authorization: Bearer <token>"
```

### Complete Ride
```bash
curl -X POST http://localhost:8081/api/v1/rides/{rideId}/complete \
-H "Authorization: Bearer <token>"
```

---

## 📚 Best Practices Applied

1. **Clean Architecture**: Controller → Service → Repository pattern
2. **DTO Pattern**: Separate request/response objects from entities
3. **Input Validation**: Jakarta Bean Validation with meaningful messages
4. **Exception Handling**: Global @ControllerAdvice for consistent error responses
5. **Security**: Stateless JWT authentication with BCrypt password encoding
6. **Role-Based Access**: @PreAuthorize annotations for endpoint security
7. **MongoDB**: Using Spring Data MongoDB repositories
8. **Lombok**: Reduce boilerplate code (@Data, @Builder, etc.)

---

## 🏃 Execution Order

1. **Phase 1**: Setup project structure and dependencies
2. **Phase 2**: Create models/entities
3. **Phase 3**: Create repositories
4. **Phase 4**: Create DTOs with validation
5. **Phase 5**: Implement exception handling
6. **Phase 6**: Configure JWT and Security
7. **Phase 7**: Implement services
8. **Phase 8**: Create controllers
9. **Phase 9**: Test all endpoints
