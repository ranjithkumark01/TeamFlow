# Docker Deployment

## Build And Start

```bash
docker compose up --build
```

## Services

| Service | Port | Description |
|---|---:|---|
| frontend | `3000` | React app served by Nginx |
| backend | `8080` | Spring Boot API |
| postgres | `5432` | PostgreSQL database |

## Environment Variables

Backend:

```text
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/teamflow
SPRING_DATASOURCE_USERNAME=teamflow
SPRING_DATASOURCE_PASSWORD=teamflow
TEAMFLOW_JWT_SECRET=<long-secret>
TEAMFLOW_JWT_EXPIRATION_MS=3600000
TEAMFLOW_UPLOAD_DIR=uploads
TEAMFLOW_MAIL_ENABLED=false
```

PostgreSQL:

```text
POSTGRES_DB=teamflow
POSTGRES_USER=teamflow
POSTGRES_PASSWORD=teamflow
```

## Production Notes

- Replace default JWT secret.
- Use managed PostgreSQL or persistent Docker volume.
- Configure SMTP and set `TEAMFLOW_MAIL_ENABLED=true` for email notifications.
- Put frontend and backend behind HTTPS in production.

