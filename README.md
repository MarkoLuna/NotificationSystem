# 🚀 Notification System

A robust, multi-channel notification system designed to deliver categorized messages asynchronously via Kafka. This project implements a scalable, event-driven architecture using the **Strategy Pattern** for channel selection, **Keycloak** for OAuth2 identity management, and **MongoDB** for persistence.

---

## 📋 System Overview

The system receives a notification request containing a **Channel**, **Category**, and **Message Body**. The orchestrator validates the request against the authenticated user's identity and publishes the message to the appropriate Kafka topic for asynchronous delivery by dedicated channel consumers.

### Message Categories
- 🏅 **SPORTS**
- 💰 **FINANCE**
- 🎬 **MOVIES**

### Notification Channels
- 📱 **SMS**
- 📧 **EMAIL**
- 🔔 **PUSH**

---

## 🏗️ Architecture

The project is a **Multi-Module Gradle Project** following an **Event-Driven Architecture**. For a detailed technical deep-dive, see [architecture.md](architecture.md).

### 🧩 Module Structure & Port Assignments

| Module | Port | Responsibility |
| :--- | :--- | :--- |
| `keycloak` | `8000` | OAuth2 Authorization Server |
| `kafka` | `9092` | Message Broker |
| `mongodb` | `27017` | Notification Persistence |
| `notification-service` | `8080` | Orchestrator, Kafka Producer |
| `user-service` | `8084` | User Management API |
| `notifications-app` | `5173` | Frontend Web Application (React/Vite) |
| `email-notification` | `8081` | Kafka Consumer - Email Delivery |
| `sms-notification` | `8082` | Kafka Consumer - SMS Delivery |
| `push-notification` | `8083` | Kafka Consumer - Push Delivery |
| `notification-api` | `N/A` | Shared Library - Common DTOs and Enums |

### 🛠️ Design Patterns
- **Strategy Pattern**: Dynamically selects the notification handler (`EmailNotificationStrategy`, `SmsNotificationStrategy`, `PushNotificationStrategy`) based on the requested channel.
- **Event-Driven (Producer-Consumer)**: `notification-service` publishes to Kafka topics, and channel-specific modules consume and deliver.
- **Resource Server**: All services are secured as OAuth2 Resource Servers, validating JWTs issued by Keycloak.

---

## 📨 Kafka Topics (v1)

| Topic | Channel |
| :--- | :--- |
| `email-notification.v1` | EMAIL |
| `sms-notification.v1` | SMS |
| `push-notification.v1` | PUSH |

### Kafka Message Schema (`NotificationMessage`)

| Field | Type | Description |
| :--- | :--- | :--- |
| `message` | `String` | The notification body content. |
| `category` | `NotificationCategory` | `SPORTS`, `FINANCE`, or `MOVIES`. |
| `userName` | `String` | Authenticated username (from JWT `preferred_username` claim). |

## 🚀 Getting Started

### Prerequisites
- **Java 21**
- **Node.js** (LTS recommended)
- **npm** (v11.10.0 or later required for security features)
- **Docker & Docker Compose**
- **Gradle** (Wrapper included)

### 1. Start Infrastructure
Start the required dependencies (Kafka, MongoDB, Keycloak) using Docker:
```bash
docker compose -f docker/docker-compose-deps.yml up -d
```

### 2. Build the Project
```bash
./gradlew build
```

### 3. Run Services
Start the core services and consumers:
```bash
# Core Services
./gradlew :user-service:bootRun
./gradlew :notification-service:bootRun

# Consumers
./gradlew :email-notification:bootRun
./gradlew :sms-notification:bootRun
./gradlew :push-notification:bootRun
```

### 4. Start Frontend
Navigate to the frontend directory and start the development server:
```bash
cd notifications-app
npm install
npm run dev
```
The application will be accessible at `http://localhost:5173`. For more details, see the [frontend README](notifications-app/README.md).

---

## 🔐 Authentication (Keycloak)

Authentication is handled by **Keycloak** (running on port `8000`). All services are configured as **OAuth2 Resource Servers**.

### Keycloak Realm Details
- **Realm**: `dev`
- **Issuer URI**: `http://localhost:8000/realms/dev`
- **Default Clients**:
    - `newClient`: Client for application access (Secret: `newClientSecret`)

### Obtaining a Token
You can obtain a token using the Keycloak Token Endpoint:
`POST http://localhost:8000/realms/dev/protocol/openid-connect/token`

---

## 📡 API Reference

All endpoints require a valid `Bearer` token from Keycloak.

### 🔔 Notification Service (`:8080`)

#### Send Notification
`POST /notifications/send`
```json
{
  "channel": "EMAIL",
  "category": "SPORTS",
  "message": "Your team just scored!"
}
```

#### List My Notifications
`GET /notifications/my`
*Returns notifications created by the authenticated user.*

#### Filter Notifications
`GET /notifications?channel=EMAIL&category=SPORTS`

### 👤 User Service (`:8084`)

#### Get Users by Subscription
`GET /users?channel=EMAIL&category=SPORTS`

---

## 🧪 Testing & Documentation

### 🛠️ Bruno Collection
A comprehensive API collection is available in the `.bruno/NotificationSystem` directory. Import it into [Bruno](https://www.usebruno.com/) to quickly test all endpoints.

### 📖 Swagger UI (OpenAPI)
Each service provides interactive documentation via Swagger UI:
- **Notification Service**: `http://localhost:8080/swagger-ui/index.html`
- **User Service**: `http://localhost:8084/swagger-ui/index.html`

---

## 🛠️ Tech Stack

### Backend
- **Framework**: Spring Boot 3.4.5
- **Language**: Java 21
- **Messaging**: Apache Kafka (Event-Driven Architecture)
- **Database**: MongoDB (Persistence)
- **Identity Provider**: Keycloak (OAuth2)
- **Build Tool**: Gradle (Multi-module)
- **API Documentation**: SpringDoc OpenAPI (Swagger)

### Frontend
- **Framework**: React 19
- **Build Tool**: Vite 8
- **UI Library**: Material UI (MUI) 9
- **Navigation**: React Router 7
- **Testing**: Vitest
