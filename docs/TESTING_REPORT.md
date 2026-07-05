# Testing Report

Date: 2026-07-02

## Build Verification

| Area | Command | Result |
|---|---|---|
| Backend | `mvn -DskipTests compile` | Passed |
| Frontend | `npm run build` | Passed |

Portable Maven and Node.js were used because `mvn` and `npm` were not installed on PATH in this workspace.

## Runtime Startup

| Application | Result |
|---|---|
| Frontend | Started successfully at `http://127.0.0.1:5173` |
| Backend | Blocked by missing PostgreSQL |

Frontend smoke checks returned HTTP 200 for:

- `/`
- `/login`
- `/projects`

Backend startup was retried after fixing a JWT secret parsing runtime issue. The remaining startup failure is:

```text
Unable to obtain connection from database:
Connection to localhost:5432 refused.
```

`docker`, `psql`, and a local PostgreSQL listener on `5432` were not available in this environment, so database-backed E2E API tests could not be completed here.

## Feature Verification Status

| Feature | Status |
|---|---|
| User registration | Implemented, compile verified, runtime blocked by DB |
| Login | Implemented, compile verified, runtime blocked by DB |
| JWT authentication | Implemented; startup JWT bug fixed |
| Protected routes | Implemented in frontend and backend |
| Profile update | Implemented |
| Project CRUD | Implemented |
| Task CRUD | Implemented |
| Task dependencies | Implemented with self, project, and cycle validation |
| Comments | Implemented |
| File uploads/downloads | Implemented |
| Notifications | Implemented |
| Email notifications | Implemented as optional SMTP-backed service |
| RCA workflow | Implemented |
| RCA review workflow | Implemented and updates RCA status |
| Dashboard | Implemented with backend analytics endpoint |
| Reports | Implemented |
| CSV export | Implemented |
| Kanban board | Implemented with drag-and-drop status updates |
| Calendar view | Implemented from task due dates |
| Dark/light theme | Implemented with localStorage persistence |
| Mobile responsiveness | Implemented responsive shell and grids |

## Required For Full Local E2E

Install Docker Desktop, then run:

```bash
docker compose up --build
```

Or install PostgreSQL locally and create:

```text
database: teamflow
user: teamflow
password: teamflow
```

Then start:

```bash
cd backend
mvn spring-boot:run
```

```bash
cd frontend
npm run dev
```
