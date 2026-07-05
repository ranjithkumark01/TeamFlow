# TeamFlow

TeamFlow is a full-stack project management and RCA workflow application built with React, Spring Boot, and PostgreSQL.

## Stack

- Frontend: React 18, Vite, Tailwind CSS, React Router, Axios, React Hook Form
- Backend: Spring Boot 3, Java 21, Maven, Spring Security, JWT, JPA, Flyway
- Database: PostgreSQL
- Deployment: Docker Compose

## Features

- JWT registration, login, logout, and protected routes
- Role model: `ADMIN`, `MANAGER`, `DEVELOPER`, `REVIEWER`
- Project, task, dependency, comment, attachment, notification, RCA, and review workflows
- Drag-and-drop Kanban board with persisted task status updates
- Calendar view from task due dates
- Task dependency validation with cycle prevention
- File upload/download for task attachments
- Notification center with optional email notification delivery
- RCA creation and review workflow
- Dashboard analytics
- Reports and CSV export
- Search and filters
- User profile editing
- Light/dark theme persistence
- Responsive desktop, tablet, and mobile layout

## Project Structure

```text
frontend/   React application
backend/    Spring Boot API
database/   PostgreSQL schema and migration references
docs/       Architecture, API, deployment, and verification docs
```

## Run With Docker

```bash
docker compose up --build
```

Frontend: `http://localhost:3000`  
Backend: `http://localhost:8080`  
PostgreSQL: `localhost:5432`

## Run Locally

Start PostgreSQL first with database/user/password:

```text
database: teamflow
user: teamflow
password: teamflow
```

Backend:

```bash
cd backend
mvn spring-boot:run
```

Frontend:

```bash
cd frontend
npm install
npm run dev
```

Frontend dev URL: `http://localhost:5173`

## Auth

Public endpoints:

- `POST /api/auth/register`
- `POST /api/auth/login`

All other endpoints require:

```http
Authorization: Bearer <jwt>
```

## Documentation

- [Architecture](docs/ARCHITECTURE.md)
- [ER Diagram](docs/ER_DIAGRAM.md)
- [API Documentation](docs/API.md)
- [Docker Deployment](docs/DOCKER.md)
- [Testing Report](docs/TESTING_REPORT.md)

## Verification Summary

Both backend and frontend compile successfully. Frontend dev server starts successfully. Backend startup requires PostgreSQL; in this workspace PostgreSQL and Docker were not installed/running, so database-backed runtime E2E tests are documented as environment-blocked in the testing report.

