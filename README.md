# Lessons Service

Spring Boot service skeleton for managing tutor-led lessons. The project was generated from [start.spring.io](https://start.spring.io) with the following dependencies:

- Spring Web
- Spring Data JPA
- PostgreSQL Driver
- Lombok

## Project structure

```
src/main/java/com/yourname/tutor/
├── TutorApplication.java          # Spring Boot entry point
├── controller/                    # REST controllers
├── service/                       # Business services
├── repository/                    # Spring Data repositories
└── model/
    ├── dto/                       # Data transfer objects
    └── entity/                    # JPA entities
```

## Getting started

1. Configure a PostgreSQL database and update `src/main/resources/application.properties` with your connection details.
2. Build the project:

   ```bash
   ./mvnw clean package
   ```

3. Run the application:

   ```bash
   ./mvnw spring-boot:run
   ```

The API exposes a sample endpoint at `GET /api/tutors` which returns a list of tutors using the DTO layer.
