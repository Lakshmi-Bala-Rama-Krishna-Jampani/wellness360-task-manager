# Task Manager API

Production-grade RESTful Task Management API built with **Spring Boot 3.4**, **Java 21**, and clean architecture principles.

[![CI](https://github.com/Lakshmi-Bala-Rama-Krishna-Jampani/wellness360-task-manager/actions/workflows/ci.yml/badge.svg)](https://github.com/Lakshmi-Bala-Rama-Krishna-Jampani/wellness360-task-manager/actions/workflows/ci.yml)

---

## Project Overview

This service provides a fully documented, authenticated REST API for managing tasks. It demonstrates enterprise patterns including layered architecture, standardized error handling, OpenAPI documentation, structured logging, and comprehensive automated testing.

Built with clean architecture, standardized error handling, and comprehensive test coverage.

---

## Architecture

```
Client → Security Filter → Trace/Payload Filters → Controller → Service → Repository (ConcurrentHashMap)
```

See [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md) for requirements analysis, diagrams, and security review.

---

## Tech Stack

| Component | Technology |
|-----------|------------|
| Language | Java 21 |
| Framework | Spring Boot 3.4.5 |
| Build | Maven 3.9 (wrapper included) |
| Security | Spring Security + BCrypt Basic Auth |
| Validation | Jakarta Validation |
| API Docs | SpringDoc OpenAPI 3 |
| Testing | JUnit 5, Mockito, MockMvc |
| Coverage | JaCoCo (>90% on core packages) |
| Container | Docker + docker-compose |
| CI | GitHub Actions |

---

## Requirements

- JDK 21+
- Docker (optional)

---

## Installation

```bash
git clone https://github.com/Lakshmi-Bala-Rama-Krishna-Jampani/wellness360-task-manager.git
cd wellness360-task-manager
```

---

## Build

```bash
# Windows
mvnw.cmd clean package

# Linux/macOS
./mvnw clean package
```

---

## Run

```bash
# Windows
mvnw.cmd spring-boot:run

# Linux/macOS
./mvnw spring-boot:run
```

Application starts at **http://localhost:8080**

### Docker

```bash
docker compose up --build
```

---

## Test

```bash
mvnw.cmd clean verify
```

Coverage report: `target/site/jacoco/index.html`

---

## Swagger URL

- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

> Swagger endpoints require authentication (same credentials as API).

---

## Authentication

All endpoints except **`GET /actuator/health`** require HTTP Basic Authentication.

| Variable | Default |
|----------|---------|
| `APP_SECURITY_USERNAME` | `admin` |
| `APP_SECURITY_PASSWORD` | `admin123` |

### Authentication Flow

1. Client sends `Authorization: Basic base64(username:password)` header
2. Spring Security `BasicAuthenticationFilter` extracts credentials
3. `UserDetailsService` loads in-memory user with BCrypt-hashed password
4. On success, request proceeds to controller; on failure, returns `401`

---

## API Examples

### Create Task

```bash
curl -u admin:admin123 -X POST http://localhost:8080/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Complete architecture review",
    "description": "Review system design document",
    "due_date": "2026-12-31",
    "status": "PENDING"
  }'
```

**Response (201):**

```json
{
  "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "title": "Complete architecture review",
  "description": "Review system design document",
  "due_date": "2026-12-31",
  "status": "PENDING",
  "created_at": "2026-07-04T18:30:00Z",
  "updated_at": "2026-07-04T18:30:00Z",
  "links": {
    "self": { "href": "/tasks/3fa85f64-5717-4562-b3fc-2c963f66afa6", "method": "GET" },
    "complete": { "href": "/tasks/3fa85f64-5717-4562-b3fc-2c963f66afa6/complete", "method": "PATCH" }
  }
}
```

### List Tasks

```bash
curl -u admin:admin123 http://localhost:8080/tasks
```

### Get Task

```bash
curl -u admin:admin123 http://localhost:8080/tasks/{id}
```

### Update Task

```bash
curl -u admin:admin123 -X PUT http://localhost:8080/tasks/{id} \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Updated title",
    "description": "Updated",
    "due_date": "2026-12-31",
    "status": "IN_PROGRESS"
  }'
```

### Complete Task

```bash
curl -u admin:admin123 -X PATCH http://localhost:8080/tasks/{id}/complete
```

### Delete Task

```bash
curl -u admin:admin123 -X DELETE http://localhost:8080/tasks/{id}
```

### Error Response Format

```json
{
  "timestamp": "2026-07-04T18:30:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Task not found with id: ...",
  "path": "/tasks/invalid-id",
  "trace_id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890"
}
```

---

## Folder Structure

```
src/main/java/com/wellness360/taskmanager/
├── advice/          # Global exception handler
├── config/          # App, OpenAPI, filters
├── constants/       # API constants
├── controller/      # REST controllers
├── dto/             # Request/response DTOs
├── enums/           # TaskStatus
├── exception/       # Domain exceptions
├── mapper/          # Entity ↔ DTO mapping
├── model/           # Domain entities
├── repository/      # Persistence layer
├── security/        # Spring Security config
├── service/         # Business logic
├── util/            # Helpers (UUID, status, trace)
└── validation/      # Custom validators

src/test/java/       # Unit, integration, security tests
docs/                # Architecture documentation
postman/             # Postman collection
.github/workflows/   # CI pipeline
```

---

## Assumptions

1. Single in-memory user for authentication demo
2. Data does not persist across restarts
3. API JSON uses snake_case field names
4. `COMPLETED` is a terminal status (cannot revert via PUT)
5. Health endpoint is public for orchestration probes

---

## Design Decisions

| Decision | Rationale |
|----------|-----------|
| Clean layered architecture | Separation of concerns, testability |
| ConcurrentHashMap | Thread-safe O(1) operations for in-memory scope |
| UUID identifiers | Collision-free without central ID service |
| Standardized ErrorResponse | Consistent client error handling |
| HATEOAS links | REST Level 3 discoverability |
| Trace ID propagation | Request correlation in logs |
| Idempotent complete | Safe retries for PATCH /complete |

---

## Future Improvements

- PostgreSQL/JPA persistence with Flyway migrations
- JWT/OAuth2 replacing Basic Auth
- Pagination, filtering, and sorting on GET /tasks
- Role-based authorization (admin vs viewer)
- Rate limiting and request throttling
- Kubernetes manifests and Helm chart
- Distributed tracing (OpenTelemetry)

---

## Troubleshooting

| Issue | Solution |
|-------|----------|
| `401 Unauthorized` | Include `-u admin:admin123` or set env credentials |
| `400 Invalid UUID` | Ensure path uses valid UUID format |
| `400 Validation failed` | Check required fields and due_date is not in past on create |
| Port 8080 in use | Set `SERVER_PORT=8081` or change `application.yml` |
| Maven not found | Use included wrapper: `mvnw.cmd` (Windows) or `./mvnw` |

---

## License

MIT — see [LICENSE](LICENSE)
