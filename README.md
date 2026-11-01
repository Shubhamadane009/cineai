# CineAI

AI-assisted movie discovery and ticket booking platform. Spring Boot backend, PostgreSQL, React frontend.
The backend owns all business rules; the AI layer (coming later) only calls controlled backend tools.

## Architecture

Modular monolith, layered: Controller -> Service -> Repository.

## Quick start
1. Copy `.env.example` to `.env`
2. `docker compose up -d`
3. `cd backend` and run `mvnw.cmd spring-boot:run` (Windows) or `./mvnw spring-boot:run`
4. `cd frontend`, `npm install`, `npm run dev`

## API (current)
| Endpoint | Description |
|---|---|
| GET /api/movies?genre= | List movies, optional case-insensitive genre filter |
| GET /api/movies/{id} | One movie (404 if missing) |
| GET /api/cinemas?city= | List cinemas, optional city filter |
| GET /api/shows?movieId=&date=&city= | Upcoming shows for a movie |

## Roadmap
Seat management and booking with concurrency control, idempotent APIs, JWT security,
Kafka notifications, LLM tool calling, integration tests, Swagger.