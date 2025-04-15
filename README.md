# Travel Planner API Backend

This is a backend API for the Travel Planner application built using Javalin framework, Hibernate ORM, PostgreSQL, and several other libraries. The API provides functionality to manage countries, users, and access controls. It supports features such as authentication, role-based access control, and CORS configuration.

The backend application can be found at: `https://github.com/qmer05/travel_planner_backend`.

## Table of Contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Testing](#testing)
- [Configuration](#configuration)
- [License](#license)

## Features

- Javalin framework for routing and handling HTTP requests
- Hibernate ORM for database interaction
- PostgreSQL as the database
- Role-based security with token authentication
- Swagger route overview for easy API exploration
- Custom error handling for API exceptions

## Prerequisites

Before running this application, ensure that you have the following installed on your machine:

- **Java 17** or later
- **Maven**
- **PostgreSQL**
- **Docker** (if you want to run the application using Docker)
- **JUnit 5** for testing

## Installation

1. Clone this repository:

```bash
git clone https://github.com/qmer05/travel_planner_backend
cd travel_planner_backend
```

2. Install the project dependencies using Maven:

```bash
mvn install
```

3. Set up PostgreSQL (if not using Docker):
- Create a PostgreSQL database.
- Update the connection properties in src/main/resources/application.properties or rely on environment variables.

4. If you want to use Docker, you can configure the Dockerfile to run the database.

## Running the Application
The application can be started using Maven with the following command:

```bash
mvn exec:java
```

Alternatively, you can run a packaged JAR by running:

```bash
java -jar target/app.jar
```

### Default Server Configuration
- The server will be accessible at http://localhost:7060 by default.
- The API base path is /api, and you can view route details at /routes.

## API Endpoints
### Example Endpoints
Here are some sample API endpoints you can interact with. Replace base_url with the actual URL of your deployed application.

1. Get all countries
```bash
GET /api/countries
```

Returns a list of all countries.

2. Get a specific country

```bash
GET /api/countries/{id}
```

Retrieves details for the country with the specified ID.

3. Create a new country

```bash
POST /api/countries
```

Requires a JSON body containing the country details, such as name, capital, languages, currencies, etc.

4. Authenticate user

```bash
POST /api/login
```

This endpoint is used for user login. You need to send user credentials in a JSON format.

### Error Handling
- General exceptions are logged and returned as error responses.
- Custom exceptions for API errors (ApiException) return appropriate HTTP status codes and a warning message.

## Testing
You can run tests using Maven's surefire-plugin. The tests are set up for unit and integration testing.

### Running tests

```bash
mvn test
```

Test cases are written using JUnit 5. Additional integration tests can be performed using TestContainers for PostgreSQL.

## Configuration
### Hibernate Configuration
This project uses Hibernate ORM to interact with the PostgreSQL database. The hibernate.cfg.xml and HibernateConfig class manage the connection and setup.

1. By default, the application connects to the database running locally (localhost:5432).

2. The connection details are managed through environment variables, such as:

```bash
DB_NAME, CONNECTION_STR, DB_USERNAME, DB_PASSWORD
```

For testing, the application uses a PostgreSQL Docker container with TestContainers.

### Javalin Configuration
- Javalin serves as the HTTP server and web framework. The server is configured to expose routes with access control and CORS settings.
- Routes are registered in the ApplicationConfig class, where the base path is set to /api.

### CORS Configuration
CORS is enabled for the API to allow all origins:

```java
config.bundledPlugins.enableCors(cors -> {
    cors.addRule(it -> {
        it.anyHost(); // Allow all origins
    });
});
```

This can be customized to restrict access to specific origins.

## License
This project is licensed under the MIT License. See the LICENSE file for more details.

```markdown
### Customizing the README

- Replace `yourusername` in the clone URL with your actual GitHub username.
- Update the configuration details based on your environment if necessary (e.g., database setup, authentication, etc.).

This `README.md` provides an overview of how to use and configure your backend API for your hotel
```