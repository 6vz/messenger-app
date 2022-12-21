# Messenger

A sample spring-boot application using kotlin, rabbitmq and postgresql.
Application allows sending messages between defined users. Messages are routed through `RabbitMQ` and stored in `PostgreSQL` database.

## REST API

Application exposes five endpoints:
* GET `/message/{user}/sent`
* GET `/message/{user}/received`
* GET `/message/{user}/received/from/{sender}`
* POST `/message/{user}/send`
* POST `/user/{user}`

## Technical requirements

The project was built and tested using `java` version 15.
Besides java, `maven` is needed to build. `Maven` version 3.6.3 was used to build.

## Building and running

### Unit/Integration tests

Run the unit and integration tests with the following command:

```bash
./mvnw clean verify
```

### Running

Run the following instructions to build and run from a source:

```bash
shell
./mvnw clean install -DskipTests
./mvnw docker:start
./mvnw spring-boot:run
```

## Further steps
The current implementation is only a draft of the application. 
Below is a list of improvements that should be made in order for the application to be used for non-educational purposes:
* API versioning
* REST parameters validation
* Security 
* Entity extension 
* Microservice related patterns like circuits breakers (if the application is to be used as a microservice)
* Containerization
* Profiles (in current implementation application uses only one property set)



