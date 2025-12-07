# Heartfelt Tutoring API Documentation

## Overview
This document describes the REST API endpoints for the Heartfelt Tutoring application server built with Spring Boot.

## Base URL
```
http://localhost:8080
```

## Authentication
Most endpoints require JWT authentication. Include the JWT token in the Authorization header:
```
Authorization: Bearer <your-jwt-token>
```

## API Endpoints

### Authentication Module

#### Send Verification Code
- **Method**: POST
- **Endpoint**: `/api/auth/send-verification-code`
- **Description**: Send verification code to email for registration
- **Request Body**:
  ```json
  {
    "email": "user@example.com"
  }
  ```
- **Response**:
  ```json
  {
    "message": "验证码已发送到您的邮箱"
  }
  ```

#### Verify Email
- **Method**: POST
- **Endpoint**: `/api/auth/verify-email`
- **Description**: Verify email with code
- **Request Body**:
  ```json
  {
    "email": "user@example.com",
    "code": "123456"
  }
  ```
- **Response**:
  ```json
  {
    "message": "邮箱验证成功"
  }
  ```

#### Register
- **Method**: POST
- **Endpoint**: `/api/auth/register`
- **Description**: Register a new user
- **Request Body**:
  ```json
  {
    "email": "user@example.com",
    "username": "username",
    "password": "password"
  }
  ```
- **Response**:
  ```json
  {
    "token": "jwt-token",
    "userId": 1,
    "username": "username",
    "message": "注册成功"
  }
  ```

#### Login
- **Method**: POST
- **Endpoint**: `/api/auth/login`
- **Description**: Authenticate user
- **Request Body**:
  ```json
  {
    "identifier": "username or email",
    "password": "password",
    "loginType": "USERNAME|EMAIL|GOOGLE"
  }
  ```
- **Response**:
  ```json
  {
    "token": "jwt-token",
    "userId": 1,
    "username": "username",
    "message": "登录成功"
  }
  ```

#### Verify Token
- **Method**: POST
- **Endpoint**: `/api/auth/verify`
- **Description**: Verify JWT token validity
- **Headers**: `Authorization: Bearer <token>`
- **Response**:
  ```json
  {
    "valid": true,
    "userId": 1,
    "claims": {...},
    "message": "Token验证成功"
  }
  ```

### User Management Module

#### Get Users
- **Method**: GET
- **Endpoint**: `/api/users`
- **Description**: Get all users
- **Response**: Array of User objects

#### Get Users (with search)
- **Method**: POST
- **Endpoint**: `/api/users`
- **Description**: Get users with search criteria
- **Request Body**: SelectUserRequest object
- **Response**: Array of User objects

#### Get User by ID
- **Method**: GET
- **Endpoint**: `/api/users/{id}`
- **Description**: Get user by ID
- **Parameters**: `id` (path) - User ID
- **Response**: User object

#### Get Teachers
- **Method**: GET
- **Endpoint**: `/api/users/teachers`
- **Description**: Get all teacher summaries
- **Response**: Array of TeacherSummary objects

#### Update User
- **Method**: PUT
- **Endpoint**: `/api/users/{id}`
- **Description**: Update user profile
- **Parameters**: `id` (path) - User ID
- **Request Body**: UserUpdateDto object
- **Response**: Updated User object

#### Upload Avatar
- **Method**: POST
- **Endpoint**: `/api/users/upload-avatar/{id}`
- **Description**: Upload user avatar
- **Parameters**: `id` (path) - User ID
- **Content-Type**: multipart/form-data
- **Form Data**: `file` - Image file
- **Response**:
  ```json
  {
    "success": true,
    "message": "头像上传成功",
    "url": "/avatar/filename.jpg"
  }
  ```

### Comments Module

#### Get All Comments
- **Method**: GET
- **Endpoint**: `/api/comments`
- **Description**: Get all comments
- **Response**: Array of Comment objects

#### Get Comment by ID
- **Method**: GET
- **Endpoint**: `/api/comments/{id}`
- **Description**: Get comment by ID
- **Parameters**: `id` (path) - Comment ID
- **Response**: Comment object

#### Get Comments by User ID
- **Method**: GET
- **Endpoint**: `/api/comments/user/{userId}`
- **Description**: Get comments by user ID
- **Parameters**: `userId` (path) - User ID
- **Response**: Array of Comment objects

#### Create Comment
- **Method**: POST
- **Endpoint**: `/api/comments`
- **Description**: Create new comment
- **Request Body**: Comment object
- **Response**: Created Comment object

### Plans Module

#### Get All Plans
- **Method**: GET
- **Endpoint**: `/api/plans`
- **Description**: Get all plans
- **Response**: Array of Plan objects

#### Get Plan by ID
- **Method**: GET
- **Endpoint**: `/api/plans/{id}`
- **Description**: Get plan by ID
- **Parameters**: `id` (path) - Plan ID
- **Response**: Plan object

#### Get Plans by User ID
- **Method**: GET
- **Endpoint**: `/api/plans/user/{userId}`
- **Description**: Get plans by user ID
- **Parameters**: `userId` (path) - User ID
- **Response**: Array of Plan objects

#### Get Plans by Completion Status
- **Method**: GET
- **Endpoint**: `/api/plans/status/{isCompleted}`
- **Description**: Get plans by completion status
- **Parameters**: `isCompleted` (path) - Boolean completion status
- **Response**: Array of Plan objects

#### Create Plan
- **Method**: POST
- **Endpoint**: `/api/plans`
- **Description**: Create new plan
- **Request Body**: PlanDetail object
- **Response**: Created Plan object

#### Toggle Plan Completion
- **Method**: PUT
- **Endpoint**: `/api/plans/toggle/{id}`
- **Description**: Toggle plan completion status
- **Parameters**: `id` (path) - Plan ID
- **Response**: Updated Plan object

#### Update Plan
- **Method**: PUT
- **Endpoint**: `/api/plans/{id}`
- **Description**: Update plan
- **Parameters**: `id` (path) - Plan ID
- **Request Body**: PlanDetail object
- **Response**: Updated Plan object

#### Delete Plan
- **Method**: DELETE
- **Endpoint**: `/api/plans/{id}`
- **Description**: Delete plan
- **Parameters**: `id` (path) - Plan ID
- **Response**: No content

### Appointments Module

#### Get All Appointments
- **Method**: GET
- **Endpoint**: `/api/appointments`
- **Description**: Get all appointments
- **Response**: Array of Appointment objects

#### Get Appointment by ID
- **Method**: GET
- **Endpoint**: `/api/appointments/{id}`
- **Description**: Get appointment by ID
- **Parameters**: `id` (path) - Appointment ID
- **Response**: Appointment object

#### Get Appointments by User ID
- **Method**: GET
- **Endpoint**: `/api/appointments/user/{userId}`
- **Description**: Get appointments by user ID
- **Parameters**: `userId` (path) - User ID
- **Response**: Array of Appointment objects

#### Create Appointment
- **Method**: POST
- **Endpoint**: `/api/appointments`
- **Description**: Create new appointment
- **Request Body**: Appointment object
- **Response**: Created Appointment object

#### Update Appointment
- **Method**: PUT
- **Endpoint**: `/api/appointments/{id}`
- **Description**: Update appointment
- **Parameters**: `id` (path) - Appointment ID
- **Request Body**: Appointment object
- **Response**: Updated Appointment object

#### Delete Appointment
- **Method**: DELETE
- **Endpoint**: `/api/appointments/{id}`
- **Description**: Delete appointment
- **Parameters**: `id` (path) - Appointment ID
- **Response**: No content

### Rewards Module

#### Get All Rewards
- **Method**: GET
- **Endpoint**: `/api/rewards`
- **Description**: Get all rewards
- **Response**: Array of Reward objects

#### Get Reward by ID
- **Method**: GET
- **Endpoint**: `/api/rewards/{id}`
- **Description**: Get reward by ID
- **Parameters**: `id` (path) - Reward ID
- **Response**: Reward object

#### Get Rewards by User ID
- **Method**: GET
- **Endpoint**: `/api/rewards/user/{userId}`
- **Description**: Get rewards by user ID
- **Parameters**: `userId` (path) - User ID
- **Response**: Array of Reward objects

#### Create Reward
- **Method**: POST
- **Endpoint**: `/api/rewards`
- **Description**: Create new reward
- **Request Body**: Reward object
- **Response**: Created Reward object

#### Update Reward
- **Method**: PUT
- **Endpoint**: `/api/rewards/{id}`
- **Description**: Update reward
- **Parameters**: `id` (path) - Reward ID
- **Request Body**: Reward object
- **Response**: Updated Reward object

#### Delete Reward
- **Method**: DELETE
- **Endpoint**: `/api/rewards/{id}`
- **Description**: Delete reward
- **Parameters**: `id` (path) - Reward ID
- **Response**: No content

### Wallets Module

#### Get All Wallets
- **Method**: GET
- **Endpoint**: `/api/wallets`
- **Description**: Get all wallets
- **Response**: Array of Wallet objects

#### Get Wallet by ID
- **Method**: GET
- **Endpoint**: `/api/wallets/{id}`
- **Description**: Get wallet by ID
- **Parameters**: `id` (path) - Wallet ID
- **Response**: Wallet object

#### Get Wallet by User ID
- **Method**: GET
- **Endpoint**: `/api/wallets/user/{userId}`
- **Description**: Get wallet by user ID
- **Parameters**: `userId` (path) - User ID
- **Response**: Wallet object

#### Get Wallet by Phone Number
- **Method**: GET
- **Endpoint**: `/api/wallets/phone/{phoneNumber}`
- **Description**: Get wallet by phone number
- **Parameters**: `phoneNumber` (path) - Phone number
- **Response**: Wallet object

#### Create Wallet
- **Method**: POST
- **Endpoint**: `/api/wallets`
- **Description**: Create new wallet
- **Request Body**: Wallet object
- **Response**: Created Wallet object

#### Update Wallet
- **Method**: PUT
- **Endpoint**: `/api/wallets/{id}`
- **Description**: Update wallet
- **Parameters**: `id` (path) - Wallet ID
- **Request Body**: Wallet object
- **Response**: Updated Wallet object

#### Delete Wallet
- **Method**: DELETE
- **Endpoint**: `/api/wallets/{id}`
- **Description**: Delete wallet
- **Parameters**: `id` (path) - Wallet ID
- **Response**: No content

### Orders Module

#### Get All Orders
- **Method**: GET
- **Endpoint**: `/api/orders`
- **Description**: Get all orders
- **Response**: Array of Order objects

#### Get Order by ID
- **Method**: GET
- **Endpoint**: `/api/orders/{id}`
- **Description**: Get order by ID
- **Parameters**: `id` (path) - Order ID
- **Response**: Order object

#### Get Orders by User ID
- **Method**: GET
- **Endpoint**: `/api/orders/user/{userId}`
- **Description**: Get orders by user ID
- **Parameters**: `userId` (path) - User ID
- **Response**: Array of Order objects

#### Get Orders by State
- **Method**: GET
- **Endpoint**: `/api/orders/state/{state}`
- **Description**: Get orders by state
- **Parameters**: `state` (path) - Order state
- **Response**: Array of Order objects

#### Create Order
- **Method**: POST
- **Endpoint**: `/api/orders`
- **Description**: Create new order
- **Request Body**: Order object
- **Response**: Created Order object

#### Update Order
- **Method**: PUT
- **Endpoint**: `/api/orders/{id}`
- **Description**: Update order
- **Parameters**: `id` (path) - Order ID
- **Request Body**: Order object
- **Response**: Updated Order object

#### Delete Order
- **Method**: DELETE
- **Endpoint**: `/api/orders/{id}`
- **Description**: Delete order
- **Parameters**: `id` (path) - Order ID
- **Response**: No content

## Data Models

### User
```json
{
  "id": 1,
  "username": "string",
  "email": "string",
  "password": "string",
  "avatarUrl": "string",
  "role": "string",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

### LoginRequest
```json
{
  "identifier": "string",
  "password": "string",
  "loginType": "USERNAME|EMAIL|GOOGLE"
}
```

### LoginResponse
```json
{
  "token": "string",
  "userId": 1,
  "username": "string",
  "message": "string"
}
```

### Comment
```json
{
  "id": 1,
  "content": "string",
  "userId": 1,
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

### Plan
```json
{
  "id": 1,
  "content": "string",
  "deadline": "datetime",
  "user": "User object",
  "isCompleted": false,
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

### Appointment
```json
{
  "id": 1,
  "userId": 1,
  "teacherId": 1,
  "scheduledTime": "datetime",
  "status": "string",
  "notes": "string",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

### Reward
```json
{
  "id": 1,
  "userId": 1,
  "title": "string",
  "description": "string",
  "points": 100,
  "createdAt": "datetime"
}
```

### Wallet
```json
{
  "id": 1,
  "userId": 1,
  "balance": 100.0,
  "phoneNumber": "string",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

### Order
```json
{
  "id": 1,
  "userId": 1,
  "amount": 100.0,
  "state": "string",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

## HTTP Status Codes
- 200 OK: Successful request
- 201 Created: Resource created successfully
- 204 No Content: Successful deletion
- 400 Bad Request: Invalid request data
- 401 Unauthorized: Authentication required
- 404 Not Found: Resource not found
- 500 Internal Server Error: Server error

## Notes
- All datetime fields are in ISO 8601 format
- Authentication is required for most endpoints except registration and login
- File uploads are handled via multipart/form-data
