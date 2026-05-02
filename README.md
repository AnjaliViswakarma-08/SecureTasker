# 🛡️ SecureTasker
A Full-Stack Task Management Application with JWT Auth and Role-Based Access Control

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.X-brightgreen.svg)
![React](https://img.shields.io/badge/React-18-blue.svg)
![MySQL](https://img.shields.io/badge/MySQL-8.0-orange.svg)
![License](https://img.shields.io/badge/License-MIT-purple.svg)

<br />

## 📖 Project Overview
SecureTasker is a full-stack task management application demonstrating secure REST APIs with Spring Boot and a React Vite frontend for task CRUD workflows. It features a robust backend built on clean, layered architecture principles. 

> **Note:** The Frontend of this application is **vibe coded** ✨ — built intuitively and rapidly to provide a smooth, functional interface for the robust backend APIs.

---

## ✨ Key Features
- **Secure Authentication:** JWT-based login and registration.
- **Role-Based Access Control (RBAC):** Admin and User roles with distinct permissions.
- **Task Management:** Create, Read, Update, and Delete tasks securely.
- **Data Isolation:** Users can only view and manage their own tasks.
- **API Documentation:** Interactive Swagger UI documentation.
- **Vibe Coded UI:** A responsive, rapidly prototyped frontend.

---

## 🛠️ Tech Stack

### Backend
- **Core:** Java 17, Spring Boot 3
- **Security:** Spring Security, JWT (JSON Web Tokens)
- **Database:** MySQL, Spring Data JPA (Hibernate)
- **Tools:** Lombok, Jakarta Validation
- **Documentation:** Springdoc OpenAPI (Swagger UI)

### Frontend
- **Framework:** React 18 (Vite)
- **Networking:** Axios with Interceptors
- **Styling:** CSS3

---

## 📂 Folder Structure

```text
SecureTasker/
├── Backend/                 # Spring Boot Application
│   ├── src/main/java/       # Source Code (Controllers, Services, Repositories, Security)
│   ├── src/main/resources/  # application.properties
│   ├── pom.xml              # Maven Dependencies
│   └── .env.example         # Environment variables template
├── Frontend/                # React Application
│   ├── src/                 # React Components, API handlers, Styles
│   ├── index.html           # Entry point
│   ├── vite.config.js       # Vite configuration
│   └── package.json         # Node dependencies
└── README.md                # Project documentation
```

---

## 🚀 Setup & Installation

### 1️⃣ Database Configuration
1. Install MySQL and ensure it's running.
2. Create a new database named `securetasker`:
   ```sql
   CREATE DATABASE securetasker;
   ```

### 2️⃣ Backend Setup
1. Navigate to the Backend directory: `cd Backend`
2. Create an environment file: Copy `.env.example` to `.env`.
3. Update `.env` with your database credentials and a strong JWT secret (at least 32 characters):
   ```properties
   DB_USERNAME=root
   DB_PASSWORD=yourpassword
   JWT_SECRET=your_super_secret_jwt_key_that_is_at_least_32_characters_long
   ```
4. Run the application:
   ```bash
   mvn spring-boot:run
   ```
> The backend will start on `http://localhost:8080`.
> Swagger Documentation is available at `http://localhost:8080/swagger-ui.html`.

### 3️⃣ Frontend Setup
1. Navigate to the Frontend directory: `cd Frontend`
2. Create an environment file: Copy `.env.example` to `.env` (update `VITE_API_BASE_URL` if needed).
3. Install dependencies:
   ```bash
   npm install
   ```
4. Start the development server:
   ```bash
   npm run dev
   ```
> The frontend will start on `http://localhost:5173`.

---

## 🔐 Authentication Flow
1. **Login/Register:** User submits credentials to the backend.
2. **Token Generation:** Server validates and returns a JWT.
3. **Storage:** Frontend securely stores the token (e.g., `localStorage`).
4. **Interceptors:** Axios interceptor automatically attaches the `Authorization: Bearer <token>` header to all outgoing API requests.
5. **Validation:** Spring Security validates the token on each request, granting or denying access.

---

## 🛡️ Role-Based Access Control (RBAC)
- `ROLE_USER`: Standard user. Can only read, update, and delete **their own** tasks.
- `ROLE_ADMIN`: Administrator. Can access and manage **all** tasks across the system.
- **Enforcement:** Achieved via Spring Security method-level annotations and strict ownership checks in the service layer.

**Admin Credentials (If seeded in DB):**
- **Email:** `securetasker@admin.com`
- **Password:** `Admin@12345`

---

## 🌐 API Endpoints Overview

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `POST` | `/api/v1/auth/register` | Register a new user | No |
| `POST` | `/api/v1/auth/login` | Authenticate and get JWT | No |
| `POST` | `/api/v1/tasks` | Create a new task | Yes |
| `GET`  | `/api/v1/tasks` | Get all tasks (User specific/Admin all) | Yes |
| `GET`  | `/api/v1/tasks/{id}` | Get a specific task by ID | Yes |
| `PUT`  | `/api/v1/tasks/{id}` | Update a task | Yes |
| `DELETE`| `/api/v1/tasks/{id}` | Delete a task | Yes |

---

## 📈 Scalability & Future Scope
- **Modular Architecture:** The Controller-Service-Repository layers isolate concerns, making the codebase easy to maintain and test.
- **Microservices Ready:** The authentication and task management domains can be easily split into separate microservices as traffic scales.
- **Caching:** Future integration with Redis to cache frequently accessed tasks and handle rate-limiting.
- **Containerization:** Ready to be containerized using Docker for deployment via Kubernetes or AWS ECS.
