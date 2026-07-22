# Battlegrounds

Spring Boot API for user management.

## Run locally

```bash
docker compose -f docker/local/docker-compose.yml up --build -d
```

The API is available at `http://localhost:8008`.

## Users API

All endpoints require a JWT access token:

```http
Authorization: Bearer <access-token>
```

Base path: `/api/v1/users`

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/v1/users/{id}` | Get a user by ID. |
| `GET` | `/api/v1/users` | List all users. |
| `POST` | `/api/v1/users` | Create a user. |

### Create a user

```bash
curl -X POST http://localhost:8008/api/v1/users \
  -H "Authorization: Bearer <access-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "johndoe",
    "email": "john@example.com",
    "phone_no": "9876543210",
    "password": "Password1!"
  }'
```

`username` must be 3–20 characters. `password` must be 8–20 characters and include uppercase, lowercase, numeric, and special characters.
