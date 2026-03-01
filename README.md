# Margin & Risk Limits Service

A high-performance, enterprise-grade Java Spring Boot microservice responsible for pre-trade risk validation and margin eligibility. 

This service acts as a "Risk Gatekeeper." It evaluates incoming trade requests against predefined exposure limits and account balances in real-time, ensuring trades are only executed if the client has sufficient margin. To ensure strict compliance and traceability, all limit breaches and status changes are asynchronously logged to an immutable NoSQL audit trail.

## Tech Stack
* **Framework:** Java 21 + Spring Boot 3.4
* **Core Database (Relational):** PostgreSQL (Managed via Flyway)
* **Audit Database (NoSQL):** MongoDB
* **Testing:** JUnit 5, Mockito, Testcontainers
* **DevOps:** Docker, GitHub Actions, Kubernetes 

## System Architecture

1. **Synchronous Validation:** REST endpoints validate trade requests against stored limits in PostgreSQL.
2. **Asynchronous Auditing:** An event-driven mechanism pushes audit events to MongoDB without blocking the main trading path.
3. **Observability:** Integrated Actuator and Micrometer metrics for real-time monitoring.

## Local Development

### Prerequisites
* Docker & Docker Compose
* Java 21 (if running locally outside of Docker)

### Running the Application (Recommended)
You can spin up the entire stack (API, PostgreSQL, MongoDB) using Docker Compose:

```bash
docker-compose up -d --build
```

The service will be available at `http://localhost:8080`.

### API Documentation
Once the application is running, you can explore and test the REST endpoints interactively via the Swagger UI:
* **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
* **Metrics (Actuator):** [http://localhost:8080/actuator/metrics](http://localhost:8080/actuator/metrics)

### Running Tests
To run the integration tests, run:

```bash
mvn clean test
```



