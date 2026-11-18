Spring Boot Security & JWT Template

A production-ready, highly secure backend boilerplate built with Java (Spring Boot) and Spring Security, implementing stateless JWT (JSON Web Token) authentication, custom permission evaluation, and centralized exception handling.

Tech Stack

Language: Java 17+

Framework: Spring Boot

Security: Spring Security, JWT (JSON Web Token), BCrypt Password Encoding

Persistence: Spring Data JPA, Hibernate

Database Support: Relational Database support (PostgreSQL / MySQL)

Build Tool: Maven

Project Architecture

src/main/java/dev/mucino/backend/
├── controllers/          # REST Controllers & Centralized Exception Handler
├── dtos/                 # Data Transfer Objects (Requests & Responses)
├── entities/             # JPA Entities (User, Role)
├── exceptions/           # Custom Business Exceptions
├── models/               # Shared Error Models and Payloads
├── repositories/         # Spring Data JPA Repositories
├── security/             # Spring Security Config, Custom Filters, & Evaluators
│   └── filter/           # JWT Authentication & Validation Filters
└── services/             # Business Logic & User Details Services


Key Features

Stateless JWT Authentication: Secure login endpoint that generates signed JWT tokens, paired with a custom validation filter for subsequent requests.

Role-Based & Custom Permissions: Includes CustomPermissionEvaluator and Role management for fine-grained access control across endpoints.

Centralized Exception Handling: Unified error responses using @RestControllerAdvice (HandlerExceptionController) to return clean, standard error payloads.

Password Encryption: Secure credential hashing utilizing BCryptPasswordEncoder.

Environment Ready: Includes an example configuration file (aplication.properties.example) to easily manage database and security secrets.

Getting Started

1. Prerequisites

Java JDK 17 or higher installed.

Maven installed.

A relational database (PostgreSQL or MySQL).

2. Configuration

Clone the repository and create your application properties file based on the provided template:

cd src/main/resources
cp aplication.properties.example application.properties


Update application.properties with your local database credentials and JWT secret keys.

3. Run the Application

From the root directory of the project, run:

./mvnw spring-boot:run


The server will start on http://localhost:8080.

License

This project is open-source and available under the MIT License.
