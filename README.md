# Profile Matcher Service

## Description

The Profile Matcher Service is a Spring Boot application that fetches player profiles from a database and matches them against active campaigns. The application updates player profiles with campaigns they qualify for.

## Installation

To install this project, follow these steps:

1. Clone the repository
2. Start a local docker container with postgres image
3. Navigate to the project directory
4. Run `./mvnw clean install`
5. Populate postgres table by executing sqls in `resources/populate_db.sql`
6. Start the application with `./mvnw spring-boot:run`

## Usage

Once the application is running, you can interact with it using the API endpoints.

## API Endpoints

| HTTP Method | Endpoint                           | Description                                                |
|-------------|-----------------------------------|---------------------------------------------------|
| GET         | http://localhost:8080/get_client_config/97983be2-98b7-11e7-90cf-082e5f28d836 | Retrieves the player profile for a specific player ID. |


## License

This project is licensed under the [MIT License](LICENSE).
