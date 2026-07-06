# TeamFlow

## Project Overview

TeamFlow is a full-stack collaborative platform designed for software engineering teams to manage projects, tasks, incident investigations (RCA), notifications, and reporting from a single application. It provides an easy-to-use interface for planning, tracking, and collaborating on projects while improving team productivity and visibility.

---

## Features Implemented

- User Registration & Login
- JWT Authentication & Authorization
- Project Management
- Task Management
- Kanban, Calendar & List Views
- Task Dependencies
- Root Cause Analysis (RCA)
- Review Workflow
- In-App Notifications
- Dashboard & Reports
- CSV Export
- Light & Dark Theme
- Responsive User Interface
- Database Migration using Flyway

---

## Technologies Used

### Frontend
- React.js
- Vite
- HTML
- CSS
- JavaScript

### Backend
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate

### Database
- PostgreSQL
- Flyway Migration

### Build Tools
- Maven
- npm

### Version Control
- Git
- GitHub

---

## Software Requirements

- Java 21
- Node.js (LTS)
- Maven
- PostgreSQL
- Git

---

## Project Structure

```
TeamFlow
│
├── backend
├── frontend
├── database
├── docs
├── README.md
└── docker-compose.yml
```

---

## Setup Instructions

### Clone Repository

```bash
git clone <repository-url>
```

### Backend Setup

```bash
cd backend
mvn spring-boot:run
```

### Frontend Setup

```bash
cd frontend
npm install
npm run dev
```

Frontend runs on:

```
http://localhost:5173
```

Backend runs on:

```
http://localhost:8080
```

---

## Database Configuration

Before running the application, update the PostgreSQL configuration in:

```
backend/src/main/resources/application.properties
```

Example:

```properties
spring.datasource.url=jdbc:postgresql://localhost:<your_postgresql_port>/teamflow
spring.datasource.username=postgres
spring.datasource.password=<your_postgresql_password>
```

Replace:

- `<your_postgresql_port>` with your PostgreSQL port.
- `<your_postgresql_password>` with your PostgreSQL password.

---

## Environment Variables

Configure the following values in `application.properties`.

- Database URL
- Database Username
- Database Password
- JWT Secret
- JWT Expiration

---

## Assumptions

- PostgreSQL is installed locally.
- The TeamFlow database has been created before starting the application.
- Java 21 is installed.
- Node.js and npm are installed.
- Maven is installed.

---

## Known Limitations

- Email notifications are basic.
- Internet connection is required.
- Local database configuration is required.
- No cloud deployment.
- Limited admin features.

---

## Future Improvements

- Real-time notifications
- File sharing
- Team chat
- Cloud deployment
- Mobile application
- AI-based task recommendations
- Performance optimization

---

## Author

**Ranjith Kumar**

B.Tech – Information Technology

GitHub Repository:
GitHub Repository:
https://github.com/ranjithkumark01/TeamFlow

---

## License

This project is developed for educational and assignment purposes.