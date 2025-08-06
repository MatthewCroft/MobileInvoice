# Backend Development Guidelines

This file defines conventions for backend code.

## Architecture

- RESTful services must follow a controller-service-repository layering pattern.
  - **Controller**: Handles HTTP requests and responses.
  - **Service**: Encapsulates business logic.
  - **Repository**: Manages data persistence.

## Data Models

- Define dedicated request DTO classes for each incoming API payload.
- Validate all incoming request DTOs to ensure they are neither empty nor null.
- Services must convert request DTOs into entity models before repository interaction.
- Service methods should return domain models representing business concepts.
- Controllers must return these domain models in API responses.

## Testing

- Each layer requires unit tests to verify its behaviour.
- Provide a Spring Boot integration test that exercises the full request pipeline.
- When creating tests, ensure entity or model objects set all required fields and use unique test values since data is not cleaned up.

## API Responses

- Convert `UUID` values to string when returning them in API responses.

