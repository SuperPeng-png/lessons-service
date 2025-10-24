# Lessons Service

Spring Boot service for managing tutor-led lessons. The project ships with environment-specific configuration, Docker tooling and CI/CD pipelines so it can be deployed consistently across development, staging and production.

## Project structure

```
src/main/java/com/yourname/tutor/
├── TutorApplication.java          # Spring Boot entry point
├── config/                        # Cross-cutting configuration (OpenAPI, etc.)
├── controller/                    # REST controllers
├── service/                       # Business services
├── repository/                    # Spring Data repositories
└── model/
    ├── dto/                       # Data transfer objects
    └── entity/                    # JPA entities
docs/
└── openapi.yaml                   # Manually curated API specification
env/
├── .env.dev                       # Default development variables
├── .env.staging                   # Example staging configuration
└── .env.prod                      # Example production configuration
```

## Environment profiles

The application defines three Spring profiles with isolated credentials and tuning:

| Profile  | Purpose      | Datasource defaults | Notes |
|----------|--------------|---------------------|-------|
| `dev`    | Local development | `jdbc:postgresql://localhost:5432/lessons_service_dev` | Enables SQL logging, uses Flyway disabled & `ddl-auto=update` for rapid iteration. |
| `staging`| Pre-production | `jdbc:postgresql://staging-db:5432/lessons_service` | Flyway migrations enabled, SQL formatting disabled. |
| `prod`   | Production   | Values injected via environment (`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`) | Exposes Prometheus metrics and increases the connection pool. |

Set the active profile through the `SPRING_PROFILES_ACTIVE` environment variable. When no profile is provided the application defaults to `dev`.

Sample environment variable collections are provided inside the `env/` folder. Copy one of them to the repository root as `.env` (ignored by Git) or reference it directly via Docker Compose commands:

```bash
cp env/.env.dev .env  # for local development
```

Update the credentials inside `.env.staging`/`.env.prod` before deploying to shared environments.

## Running locally

### With Docker Compose

1. Select the appropriate environment file (development shown here):

   ```bash
   cp env/.env.dev .env
   ```

2. Build and start the stack:

   ```bash
   docker compose up --build
   ```

   The service listens on [http://localhost:8080](http://localhost:8080). Health is continuously monitored via Docker health checks hitting `/actuator/health`.

### With Maven

Ensure you have a PostgreSQL instance running and update `DB_*` variables as needed, then run:

```bash
./mvnw spring-boot:run
```

To execute the full build locally:

```bash
./mvnw clean verify
```

## Monitoring & observability

- [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html) is enabled for all profiles. Useful endpoints include:
  - `GET /actuator/health` – readiness/liveness checks (used by Docker health checks).
  - `GET /actuator/info`
  - `GET /actuator/metrics` – aggregated Micrometer metrics, with Prometheus scraping exposed in production profile.
- Docker images expose a container-level `HEALTHCHECK` mirroring the Actuator health endpoint.
- Application-specific logs default to `INFO` level, with verbose DEBUG logging for the `com.yourname.tutor` package in the `dev` profile.

## API documentation

- Interactive Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- Source specification: [`docs/openapi.yaml`](docs/openapi.yaml)

The OpenAPI metadata is defined programmatically in `OpenApiConfig` so server URLs and contact details stay consistent across generated docs.

## CI/CD pipelines

### GitHub Actions

`./github/workflows/ci.yml` runs on every push and pull request targeting `main`:

1. Checks out the repository.
2. Sets up Java 21 and caches Maven dependencies.
3. Executes `./mvnw verify` with the `dev` profile.
4. Builds the Docker image when changes land on `main`.

### GitLab CI

`.gitlab-ci.yml` mirrors the GitHub workflow for GitLab-hosted mirrors:

1. `maven-test` job compiles and tests the project with Java 21.
2. `build-docker-image` runs on `main` branches, producing a container image ready for upload to your registry (`registry.example.com` placeholder).

Update registry URLs and secret variables before enabling deployments.

## Useful commands

- Check application health: `curl http://localhost:8080/actuator/health`
- Tail container logs: `docker compose logs -f app`
- Run database migrations manually (staging/prod): `./mvnw -P${SPRING_PROFILES_ACTIVE} flyway:migrate`

The API exposes a sample endpoint at `GET /api/tutors` which returns a list of tutors using the DTO layer.
