# CineAI Project Context

## Stack
Java 21, Spring Boot 4.1.2 (Hibernate 7, Jackson 3), Maven, PostgreSQL 16 (Docker, host port 5433),
React + Vite (ESLint). Planned: JWT, Kafka, LLM tool calling.

## Architecture
Modular monolith, package-by-feature under com.cineai.
Controller -> Service -> Repository. DTOs (records) for all API I/O; entities never leave the service layer.

## How to run
1. `docker compose up -d` (from repo root)
2. Backend: `cd backend` then
   `mvnw.cmd spring-boot:run "-Dspring-boot.run.jvmArguments=-Xms128m -Xmx512m -XX:MaxMetaspaceSize=256m"`
3. Frontend: `cd frontend`, `npm install`, `npm run dev` -> http://localhost:5173

## Endpoints (all GET)
- /api/movies, /api/movies?genre=thriller (case-insensitive), /api/movies/{id}
- /api/cinemas, /api/cinemas?city=pune
- /api/shows?movieId=1[&date=2026-11-02][&city=pune]  (movieId required)

## Decisions
- IDs: Long IDENTITY. Money: BigDecimal NUMERIC(10,2). Enums: EnumType.STRING.
- All @ManyToOne are LAZY + optional=false; open-in-view=false.
- Show table is named "shows". Times are LocalDateTime (single-city MVP).
- N+1 avoided with JOIN FETCH in show queries.
- Errors: ResourceNotFound -> 404, BadRequest/type mismatch/missing param -> 400, catch-all -> 500 (logged).
- Empty result = 200 [], unknown parent resource = 404.
- Seeder is idempotent (skips if movies exist); data dates are relative to today.
- CORS: global on /api/**, origin from app.cors.allowed-origins (default http://localhost:5173).
- Day 1 schema via ddl-auto=update (Flyway later).
- JVM timezone forced to Asia/Kolkata in main(): Postgres 16 rejects the legacy "Asia/Calcutta" alias.
- Spring Boot 4.1.2 chosen over 3.5.x (what Initializr offered).

## Known limitations
- No tests yet. No pagination. No show-overlap validation. No seats/bookings.
- ddl-auto=update and show-sql=true are dev-only. Dev DB password is in .env and an application.yml default.
- LocalDateTime ignores timezones.
- Genre list is duplicated in the React UI (a GET /api/genres endpoint would remove that).

## Progress
- [x] Day 1: Docker Postgres, Spring Boot setup, entities, Movie/Cinema/Show APIs,
      exception handling, seed data, CORS, React movie list
- [ ] Day 2: Seat/ShowSeat, seat map, holds with expiry, booking, concurrency, idempotency
- [ ] Day 3: Spring Security + JWT (remember CORS in the security filter chain)
- [ ] Day 4: Kafka
- [ ] Day 5-6: LLM integration and tool calling
- [ ] Day 7: Integration tests, Docker, Swagger, README