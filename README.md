# 🚗 RideShare Backend API

A mini Ride Sharing backend built with **Spring Boot 3**, **MongoDB**, **JWT Authentication**, **Input Validation**, and **Global Exception Handling**.

## 📋 Features

- ✅ User Registration & Login with JWT
- ✅ Role-based authorization (USER / DRIVER)
- ✅ BCrypt password encoding
- ✅ Create ride requests (Passengers)
- ✅ View pending rides (Drivers)
- ✅ Accept rides (Drivers)
- ✅ Complete rides (User/Driver)
- ✅ Input validation with Jakarta Validation
- ✅ Global exception handling

## 🏗️ Tech Stack

- **Framework**: Spring Boot 3.2.0
- **Database**: MongoDB
- **Security**: Spring Security + JWT (jjwt 0.12.3)
- **Validation**: Jakarta Bean Validation
- **Build Tool**: Maven
- **Language**: Java 17

## 📁 Project Structure

```
src/main/java/org/example/rideshare/
├── RideShareApplication.java      # Main entry point
├── model/
│   ├── User.java                  # User entity
│   └── Ride.java                  # Ride entity
├── repository/
│   ├── UserRepository.java        # User MongoDB repository
│   └── RideRepository.java        # Ride MongoDB repository
├── service/
│   ├── AuthService.java           # Authentication logic
│   ├── UserService.java           # User business logic
│   └── RideService.java           # Ride business logic
├── controller/
│   ├── AuthController.java        # Auth endpoints
│   ├── RideController.java        # Ride endpoints
│   └── DriverController.java      # Driver endpoints
├── config/
│   ├── SecurityConfig.java        # Security configuration
│   ├── CustomUserDetailsService.java
│   └── JwtAuthenticationFilter.java
├── dto/
│   ├── request/
│   │   ├── RegisterRequest.java
│   │   ├── LoginRequest.java
│   │   └── CreateRideRequest.java
│   └── response/
│       ├── AuthResponse.java
│       ├── RideResponse.java
│       └── ErrorResponse.java
├── exception/
│   ├── GlobalExceptionHandler.java
│   ├── NotFoundException.java
│   ├── BadRequestException.java
│   └── UnauthorizedException.java
└── util/
    └── JwtUtil.java               # JWT token utility
```

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Maven 3.6+
- MongoDB (running on localhost:27017)

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd rideshare
   ```

2. **Start MongoDB**
   ```bash
   mongod
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. The API will be available at `http://localhost:8081`

## 📡 API Endpoints

| Role | Method | Endpoint | Description |
|------|--------|----------|-------------|
| PUBLIC | POST | `/api/auth/register` | Register new user |
| PUBLIC | POST | `/api/auth/login` | Login & get JWT token |
| USER | POST | `/api/v1/rides` | Create new ride request |
| USER | GET | `/api/v1/user/rides` | Get user's rides |
| DRIVER | GET | `/api/v1/driver/rides/requests` | Get pending rides |
| DRIVER | POST | `/api/v1/driver/rides/{rideId}/accept` | Accept a ride |
| USER/DRIVER | POST | `/api/v1/rides/{rideId}/complete` | Complete a ride |

## 🧪 Testing with cURL

### Register a User
```bash
curl -X POST http://localhost:8081/api/auth/register \
-H "Content-Type: application/json" \
-d '{"username":"john","password":"1234","role":"ROLE_USER"}'
```

### Register a Driver
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

### Create a Ride (User)
```bash
curl -X POST http://localhost:8081/api/v1/rides \
-H "Content-Type: application/json" \
-H "Authorization: Bearer <USER_TOKEN>" \
-d '{"pickupLocation":"Koramangala","dropLocation":"Indiranagar"}'
```

### Get User's Rides
```bash
curl -X GET http://localhost:8081/api/v1/user/rides \
-H "Authorization: Bearer <USER_TOKEN>"
```

### Get Pending Rides (Driver)
```bash
curl -X GET http://localhost:8081/api/v1/driver/rides/requests \
-H "Authorization: Bearer <DRIVER_TOKEN>"
```

### Accept a Ride (Driver)
```bash
curl -X POST http://localhost:8081/api/v1/driver/rides/{rideId}/accept \
-H "Authorization: Bearer <DRIVER_TOKEN>"
```

### Complete a Ride
```bash
curl -X POST http://localhost:8081/api/v1/rides/{rideId}/complete \
-H "Authorization: Bearer <TOKEN>"
```

## 📊 Request/Response Examples

### Register Request
```json
{
  "username": "john",
  "password": "1234",
  "role": "ROLE_USER"
}
```

### Auth Response
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "john",
  "role": "ROLE_USER",
  "message": "User registered successfully"
}
```

### Create Ride Request
```json
{
  "pickupLocation": "Koramangala",
  "dropLocation": "Indiranagar"
}
```

### Ride Response
```json
{
  "id": "507f1f77bcf86cd799439011",
  "userId": "507f1f77bcf86cd799439010",
  "driverId": null,
  "pickupLocation": "Koramangala",
  "dropLocation": "Indiranagar",
  "status": "REQUESTED",
  "createdAt": "2025-12-06T10:30:00.000+00:00"
}
```

### Error Response
```json
{
  "error": "VALIDATION_ERROR",
  "message": "Pickup location is required",
  "timestamp": "2025-12-06T10:30:00Z"
}
```

## 🔄 Ride Status Flow

```
REQUESTED  →  ACCEPTED  →  COMPLETED
    ↑             ↑            ↑
  (User        (Driver      (User or
 creates)     accepts)      Driver)
```

## ⚙️ Configuration

Edit `src/main/resources/application.properties`:

```properties
# Server
server.port=8081

# MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017/rideshare

# JWT
jwt.secret=YourSecretKey
jwt.expiration=86400000  # 24 hours
```

## 📝 License

This project is for educational purposes.
