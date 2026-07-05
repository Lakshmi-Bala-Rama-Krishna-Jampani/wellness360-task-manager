# Contributing

Thank you for your interest in contributing to the Task Manager API.

## Development Setup

1. Install JDK 21
2. Clone the repository
3. Run `./mvnw clean verify`

## Pull Request Guidelines

- Keep changes focused and well-tested
- Maintain or improve test coverage (>= 90% line coverage on core packages)
- Follow existing package structure and naming conventions
- Update README and OpenAPI annotations when API behavior changes

## Commit Messages

Use clear, imperative commit messages:

- `feat: add task filtering by status`
- `fix: handle invalid UUID in path variable`
- `test: add integration test for complete endpoint`

## Code Style

- Java 21 features are welcome where they improve clarity
- No business logic in controllers
- All API calls and persistence access through service/repository layers
- Avoid logging secrets or credentials
