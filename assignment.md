🚀 RideShare Backend – Student Helper Document (NO TESTING VERSION)
(Reference Guide for Today’s In-Class Mini Project)
1️⃣ Project Overview
You will build a mini Ride Sharing backend using:

Spring Boot
MongoDB
JWT Authentication
Input Validation
Global Exception Handling
This project uses everything we learned in previous classes:

✔ Mongo Repository
✔ DTOs
✔ Validation
✔ JWT login + authorization
✔ Request → Service → Repository clean architecture

2️⃣ Entities & Relationships (Whiteboard Diagrams)
📌 Entity 1 — User
User
 ├─ id : String
 ├─ username : String
 ├─ password : String
 ├─ role : String   → ROLE_USER / ROLE_DRIVER
📌 Entity 2 — Ride
Ride
 ├─ id : String
 ├─ userId : String         → Passenger (FK)
 ├─ driverId : String?      → Driver (FK)
 ├─ pickupLocation : String
 ├─ dropLocation : String
 ├─ status : String         → REQUESTED / ACCEPTED / COMPLETED
 ├─ createdAt : Date
📌 Relationship Diagram (Board Diagram)
 USER (ROLE_USER)       DRIVER (ROLE_DRIVER)
        │                         │
        │ requests                │ accepts
        ▼                         ▼
    ┌────────────────────────────────┐
    │              RIDE              │
    ├────────────────────────────────┤
    │ userId     → USER.id          │
    │ driverId   → DRIVER.id        │
    │ status     → REQUESTED/ACCEPT │
    └────────────────────────────────┘
3️⃣ Folder Structure (Students MUST Follow Exactly)
src/
 ├── main/
 │    ├── java/
 │    │     └── org/example/rideshare/
 │    │           ├── model/
 │    │           ├── repository/
 │    │           ├── service/
 │    │           ├── controller/
 │    │           ├── config/
 │    │           ├── dto/
 │    │           ├── exception/
 │    │           └── util/
 │    └── resources/
 │            └── application.properties
4️⃣ What Students Must Implement (Feature Checklist)
These are the mandatory features.

🧑‍🤝‍🧑 User Registration + Login (JWT)
Endpoints:

POST /api/auth/register
POST /api/auth/login
Rules:

✔ Store password BCrypt encoded
✔ Return JWT token on login
✔ User role is either ROLE_USER or ROLE_DRIVER

🚕 Request a Ride (Passenger)
POST /api/v1/rides
Request body:

{
  "pickupLocation": "Koramangala",
  "dropLocation": "Indiranagar"
}
Rules:

Must be logged in as USER
Status = REQUESTED
userId = logged in user
🚗 Driver: View Pending Ride Requests
GET /api/v1/driver/rides/requests
Return all rides with status REQUESTED.

✔ Driver Accepts a Ride
POST /api/v1/driver/rides/{rideId}/accept
Rules:

Must have ROLE_DRIVER
Ride must be REQUESTED
Assign driverId = logged in driver id
Status → ACCEPTED
✔ Complete Ride (Driver or User)
POST /api/v1/rides/{rideId}/complete
Rules:

Must be ACCEPTED
Set status → COMPLETED
✔ User Gets Their Own Rides
GET /api/v1/user/rides
Filter rides by userId.

5️⃣ Input Validation Cheat Sheet
Use Jakarta validations in DTOs:

@NotBlank
@Size(min = 3)
@Valid
Example DTO:

public class CreateRideRequest {
    @NotBlank(message = "Pickup is required")
    private String pickupLocation;

    @NotBlank(message = "Drop is required")
    private String dropLocation;
}
6️⃣ Global Exception Handling Cheat Sheet
Your folder:

exception/
 ├── GlobalExceptionHandler.java
 ├── NotFoundException.java
 └── BadRequestException.java
Example error response:
{
  "error": "VALIDATION_ERROR",
  "message": "Pickup is required",
  "timestamp": "2025-01-20T12:00:00Z"
}
7️⃣ JWT Cheat Sheet (Very Important)
JWT goes in every request header:
Authorization: Bearer <token>
Token contains:

username
role
issuedAt
expiry
JWT Flow Diagram
LOGIN  →  JWT TOKEN  →  STORE IN CLIENT  →  SEND WITH EVERY REQUEST
8️⃣ API Summary Table
Role	Endpoint	Action
PUBLIC	/api/auth/register	Create User
PUBLIC	/api/auth/login	Return JWT
USER	/api/v1/rides	Create Ride
USER	/api/v1/user/rides	View My Rides
DRIVER	/api/v1/driver/rides/requests	View All Pending
DRIVER	/api/v1/driver/rides/{id}/accept	Accept Ride
USER/DRIVER	/api/v1/rides/{id}/complete	Complete Ride
9️⃣ CURL Commands for Basic Testing
Register USER
curl -X POST http://localhost:8081/api/auth/register \
-H "Content-Type: application/json" \
-d '{"username":"john","password":"1234","role":"ROLE_USER"}'
Register DRIVER
curl -X POST http://localhost:8081/api/auth/register \
-H "Content-Type: application/json" \
-d '{"username":"driver1","password":"abcd","role":"ROLE_DRIVER"}'
Login
curl -X POST http://localhost:8081/api/auth/login \
-H "Content-Type: application/json" \
-d '{"username":"john","password":"1234"}'
Create Ride
curl -X POST http://localhost:8081/api/v1/rides \
-H "Authorization: Bearer <token>" \
-d '{"pickupLocation":"A","dropLocation":"B"}'
🔟 Student Assignment Requirements
Students must submit:

✔ Complete functioning API
✔ Proper folder structure
✔ DTOs + Validation
✔ Exception Handling
✔ JWT Auth implemented correctly
✔ Postman collection (optional)
✔ README explaining endpoints