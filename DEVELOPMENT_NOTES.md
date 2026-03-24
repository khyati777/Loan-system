# Development Notes for Loan Application Service

## Overall Approach

The objective was to build a Spring Boot REST service for evaluating loan applications based on a set of business rules. The approach involved a standard layered architecture (Controller, Service, Domain, Repository). Java 17 and Spring Boot 3.0 were used as per the requirements. All financial calculations were implemented using `BigDecimal` to ensure precision, as mandated by the business context.

## Key Design Decisions

1.  **Layered Architecture**: Adopting a clear separation into Controller, Service, Domain, and Repository layers.
2.  **DTOs for API Interaction**: Using Data Transfer Objects (`LoanApplicationRequest` and `LoanApplicationResponse`) for incoming requests and outgoing responses. 
3   **Global Exception Handling**: Implemented a `@RestControllerAdvice` to centralize exception handling, specifically for `MethodArgumentNotValidException` (validation errors) and general `Exception` types. This ensures consistent error responses across the API.
4    **Lombok**: Used Lombok (`@Data`, `@RequiredArgsConstructor`) 

## Improvements You Would Make With More Time

1.  **External Configuration for Rates**: Move base interest rate and premium percentages to external configuration (e.g., `application.properties`, a dedicated configuration service) to allow for easier adjustments without code changes.
2.  **Asynchronous Processing**: For high-volume applications, consider making the loan processing asynchronous (e.g., using a message queue like Kafka or RabbitMQ) to improve responsiveness and resilience.
3  **Security**: Implement proper authentication and authorization mechanisms (e.g., OAuth2, JWT) to secure the REST endpoints.
4   **Logging and Monitoring**: Implement robust logging with structured logs and integrate with monitoring tools (e.g., Prometheus, Grafana) for better observability in production.