# SecureTasker - Backend Developer Assignment

## Project Overview
SecureTasker is a full-stack task management application with JWT-based authentication, role-based access control, and a clean layered architecture. It demonstrates secure REST APIs with Spring Boot and a React Vite frontend for task CRUD workflows.

## Tech Stack
**Backend**
- Java 17, Spring Boot, Spring Security, JWT
- Spring Data JPA (Hibernate), MySQL
- Lombok, Jakarta Validation
- Springdoc OpenAPI (Swagger UI)

**Frontend**
- React (Vite)
- Axios

## Folder Structure
```
/Backend
  /src/main/java/com/securetasker
    /controller
    /service
    /repository
    /entity
    /dto
    /config
    /security
    /exception
  /src/main/resources
  pom.xml
  .env.example
/Frontend
  /src
    /api
    /styles
    App.jsx
  index.html
  vite.config.js
  package.json
  .env.example
README.md
```

## Setup Instructions
### 1) Backend
1. Create a MySQL database named `securetasker`.
2. Copy `Backend/.env.example` to `.env` and update credentials. Use a JWT secret with at least 32 characters.
3. From `Backend`, run:
   - `mvn spring-boot:run`

### 2) Frontend
1. Copy `Frontend/.env.example` to `.env` and update the API URL if needed.
2. From `Frontend`, run:
   - `npm install`
   - `npm run dev`

Backend runs on `http://localhost:8080` and Swagger UI at `http://localhost:8080/swagger-ui.html`.
Frontend runs on `http://localhost:5173`.

## API Endpoints
**Auth**
- `POST /api/v1/auth/register`
- `POST /api/v1/auth/login`

**Tasks** (JWT required)
- `POST /api/v1/tasks`
- `GET /api/v1/tasks`
- `GET /api/v1/tasks/{id}`
- `PUT /api/v1/tasks/{id}`
- `DELETE /api/v1/tasks/{id}`

## Authentication Flow
1. User registers or logs in.
2. Server returns a JWT token.
3. Frontend stores the token in `localStorage`.
4. Axios interceptor attaches the token to all API requests.
5. Spring Security validates the token on each request.

## Role-Based Access Control (RBAC)
- `ROLE_USER` can only access their own tasks.
- `ROLE_ADMIN` can access all tasks.
- Enforcement is done via method-level security and ownership checks in the service layer.

## Admin Credentials (Seeded)
- Email: `securetasker@admin.com`
- Password: `Admin@12345`

## Scalability Notes
- **Modular architecture:** controller-service-repository layers isolate concerns for easier growth.
- **Microservices:** split auth and task services when scaling teams or traffic.
- **Load balancing:** scale application servers horizontally behind a load balancer.
- **DB scaling:** use read replicas and sharding as needed; move to managed MySQL.
- **Caching (future scope):** add Redis for sessionless caching and rate limiting.

## Simulated Commit History
- feat: initialize Spring Boot project and Vite frontend structure
- feat: configure MySQL and JPA entities
- feat: implement user registration and login with JWT
- feat: setup Spring Security with JWT filter and authentication
- feat: implement role-based access control using annotations
- feat: add task CRUD APIs with ownership validation
- feat: implement DTO validation and global exception handling
- feat: integrate Swagger for API documentation
- feat: create React Vite frontend with authentication pages
- feat: connect frontend with backend using Axios
- feat: implement protected routes and dashboard functionality
- docs: add complete README with setup and scalability notes
