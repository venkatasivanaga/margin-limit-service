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
*(Instructions to be added once Docker Compose is configured)*