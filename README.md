# Senopi

Dicoding notes app the backend project, but it's powered by Spring Boot.

## Overview

This project was made using Spring Boot.
Like general backend applications, it can process a request and send a
response, store into a database,
authenticate request & authorize resource
(we use [JWT](https://jwt.io/)), process queue job, send emails, store images, etc.

For the application feature, we build it according to
the [original project](https://github.com/dicodingacademy/a271-backend-menengah-labs).

## Usage

We have documented the [OpenAPI spec](https://swagger.io/specification/) of the app.
You can
check [here](https://github.com/stackpan/senopi/tree/main/docs).

## Installation

### Prerequisite:
- JRE version 17 or later
- Apache Maven

1. Database setup
You can use PostgreSQL as database of this application. Make sure to set up the database name, username, and password. Once finished, configure this app by filling these keys in application configuration:
```
spring.datasource.url=jdbc:postgresql://<yourpgserverhost>:<port>/<dbname>
spring.datasource.username=
spring.datasource.password=
spring.datasource.driver-class-name=org.postgresql.Driver
```
You can follow the example file in [`application.yaml.example`](https://github.com/stackpan/senopi/blob/main/src/main/resources/application.yaml.example) file inside resource classpath

2. AMQP server setup
Senopi has a feature to exporting notes using queue job so it will require an AMQP server to fulfill this functionality. We have used RabbitMQ as AMQP server because it is easy to use. To configure AMQP server, fill these keys in application configuration:
```
spring.rabbitmq.host=
spring.rabbitmq.port=
spring.rabbitmq.username=
spring.rabbitmq.password=
```

You can follow the example file in [`application.yaml.example`](https://github.com/stackpan/senopi/blob/main/src/main/resources/application.yaml.example) file inside resource classpath

And set up the consumer app that you can find [here](https://github.com/stackpan/senopi-consumer)

3. JWT Secret
For security purpose this app need private and public key for generating JWT tokens. You can generate two `.pem` file as private and public key. And configure the app to locate the files location in application configuration:
```
rsa.private-key=
rsa.public-key=
```

You can follow the example file in [`application.yaml.example`](https://github.com/stackpan/senopi/blob/main/src/main/resources/application.yaml.example) file inside resource classpath

4. Running the app

Once those setups above is finished. You can start the application:
`mvn spring-boot:run`

### Frontend Service

To install the frontend service,
clone [this repository](https://github.com/stackpan/notes-client) and follow the instructions inside it.
