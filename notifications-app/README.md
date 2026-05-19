# Frontend

## 📌 Project Overview

**Notifications app** is a frontend web application built with **React 19** and **Vite**, designed for high-performance notification management. It provides a sleek, responsive interface for users to authenticate, track real-time notifications, and broadcast messages across multiple channels.

The application serves as a reference implementation for **JWT-based authentication**, **protected routing**, and **state-driven UI** using Material UI (MUI).

---

## 🚀 Key Features

- 🔐 **Secure Authentication**: Robust login system with JWT token handling and persistent sessions.
- 🔄 **Session Management**: Automatic logout on token expiration and unauthorized access detection.

---

## 🛠️ Tech Stack

- **Framework**: [React 19](https://react.dev/)
- **Build Tool**: [Vite 8](https://vitejs.dev/)
- **UI Library**: [Material UI (MUI) 9](https://mui.com/)
- **Styling**: Emotion (Styled Components)
- **Networking**: [Axios](https://axios-http.com/)
- **Navigation**: [React Router 7](https://reactrouter.com/)
- **Utilities**: JWT Decode

---

## 📁 Project Structure

```text
src/
 ├── api/                # Axios instance & interceptors setup
 ├── auth/               # AuthContext & Session management logic
 ├── components/         # Reusable UI components (Navbar, Toast, etc.)
 ├── pages/              # Main view containers (Dashboard, Login)
 ├── assets/             # Static resources and images
 ├── App.jsx             # Root routing and provider orchestration
 └── main.jsx            # Application entry point
```

---

## ⚙️ Installation & Setup

### 1. Prerequisites
Ensure you have [Node.js](https://nodejs.org/) installed (LTS recommended) and **npm v11.10.0** or later required for security features.

### 2. Clone and Install
```bash
git clone <repository-url>
cd notifications-app
npm install
```

### 3. Environment Configuration
Create a `.env` file in the root directory:
```env
VITE_API_URL=http://localhost:8080
VITE_AUTH_URL=http://localhost:8000/realms/dev/protocol/openid-connect/token
VITE_CLIENT_ID=newClient
VITE_CLIENT_SECRET=newClientSecret
```

### 4. Development Server
```bash
npm run dev
```
The application will be accessible at `http://localhost:5173`.

### 5. Unit Testing
Run the test suite using Vitest:
```bash
npm test
```

---

## 🌐 Backend Integration

NotifyHub is integrated with a **Keycloak** authorization server and a **Spring Boot** notification service.

### Authentication (Keycloak)
- **Endpoint**: `VITE_AUTH_URL`
- **Method**: `POST` (form-urlencoded)
- **Grant Type**: `password`

### Notifications (Notification Service)
- `GET /notifications/my`: Retrieves notifications for the authenticated user.
- `POST /notifications/send`: Dispatches a new notification.

**Request Schema:**
```json
{
  "channel": "SMS | EMAIL | PUSH",
  "category": "SPORTS | FINANCE | MOVIES",
  "message": "string"
}
```

---

## 📄 License

This project is open-source and available under the MIT License.
