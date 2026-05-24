# Battlegrounds

Spring Boot application for managing users (example project).

## Requirements
- Java 21 (dev image uses Eclipse Temurin)
- Docker & Docker Compose
- Maven (optional if running outside container)

## Run (local / development)
1. Expose host UID/GID for file ownership (created automatically):
   - docker/local/.env contains UID and GID used by the entrypoint.
2. Start with compose (local):
   docker compose -f docker/local/docker-compose.yml up --build -d
3. App will be available at http://localhost:8008

## Logging
- Logs are written to both stdout and files by `logback-spring.xml`.
- Host-mounted logs directory: `./logs`
- Files: `app-YYYY-MM-DD.log` (daily rotated) and `app.log` symlink -> today's file.
- To stream logs on host: `tail -f ./logs/app-$(date +%F).log` or use `docker logs -f spring-boot-app`.

## API responses and error format
All responses use a common wrapper `ApiResponse<T>`:
- statusCode (int), status ("success"/"error"), message (String), data (T)

Error payload (`data`) is `ErrorDetails` with fields:
- timestamp (ISO), status (http code), error (text), path (request URI)

Example error JSON:
{
  "statusCode":404,
  "status":"error",
  "message":"User Not Found with id: 123",
  "data":{
    "timestamp":"2026-05-24T06:26:41+00:00",
    "status":404,
    "error":"Not Found",
    "path":"/users/123"
  }
}

## Useful commands
- Restart compose: `docker compose -f docker/local/docker-compose.yml up --build -d`
- Inspect mounts: `docker inspect spring-boot-app --format '{{json .Mounts}}'`
- Tail logs: `tail -f logs/app-$(date +%F).log`

## Notes
- The dev entrypoint creates today's dated logfile and chowns the logs directory to the host UID/GID to make files editable on host.
- Remove `src/main/java/com/turf/battlegrounds/exception/ErrorResponse.java` if unused.

## Contact
Maintainers: project repository owner
