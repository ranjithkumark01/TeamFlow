# API Documentation

Base URL: `/api`

All endpoints except auth require:

```http
Authorization: Bearer <jwt>
```

## Authentication

| Method | Path | Description |
|---|---|---|
| POST | `/auth/register` | Register user and return JWT |
| POST | `/auth/login` | Authenticate and return JWT |

## CRUD Resources

Each resource supports:

| Method | Path | Description |
|---|---|---|
| GET | `/{resource}?page=0&size=20` | Paginated list |
| GET | `/{resource}/{id}` | Retrieve one |
| POST | `/{resource}` | Create |
| PUT | `/{resource}/{id}` | Update |
| DELETE | `/{resource}/{id}` | Delete |

Resources:

- `/users`
- `/projects`
- `/project-members`
- `/tasks`
- `/task-relations`
- `/comments`
- `/attachments`
- `/notifications`
- `/root-cause-analyses`
- `/rca-reviews`

## Search And Filters

| Method | Path | Query |
|---|---|---|
| GET | `/projects` | `query`, `createdById`, `page`, `size` |
| GET | `/tasks` | `query`, `status`, `priority`, `projectId`, `assigneeId`, `page`, `size` |
| GET | `/notifications` | `recipientId`, `read`, `page`, `size` |
| GET | `/root-cause-analyses` | `status`, `taskId`, `page`, `size` |

## Business Endpoints

| Method | Path | Description |
|---|---|---|
| GET | `/analytics/dashboard` | Dashboard totals and grouped task metrics |
| GET | `/profile` | Current authenticated user profile |
| PUT | `/profile` | Update current authenticated user profile |
| POST | `/files/upload` | Multipart file upload for task attachment |
| GET | `/files/{fileName}` | Download uploaded file |
| GET | `/exports/tasks.csv` | Export tasks as CSV |
| GET | `/exports/projects.csv` | Export projects as CSV |
| GET | `/exports/root-cause-analyses.csv` | Export RCA records as CSV |

## Response Envelope

```json
{
  "success": true,
  "message": "Task created",
  "data": {},
  "timestamp": "2026-07-02T..."
}
```

## Notable Validation

- Task dependency cannot reference the same task.
- Task dependency must stay within the same project.
- Task dependency cannot introduce a cycle.
- RCA review decision updates RCA status.
- Passwords are stored with BCrypt.
- JWT protects every non-auth endpoint.

