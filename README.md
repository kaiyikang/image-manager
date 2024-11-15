# TODO - Main Title

## First Time Run

```bash
cp .env.sample .env
source .env
docker volume create ${VOLUME_NAME}
# volumes name can be found in 'docker-compose.yml'
docker compose up -d
```

Use bash script to execute the DB.

```bash
psql -h localhost -p 5432 -U ${POSTGRES_USER} -d ${POSTGRES_DB} -W ${POSTGRES_PASSWORD}
```

## Spring Initializer

- project: Maven
- Language: Java
- Spring Boot: 3.3.5
- Project Metadata:
  - Group: com.imageservice
  - Artifact: image-storage
  - Name: Image Storage Service
  - Description: A microservice for managing image upload and download operations with RESTful APIs, supporting secure file storage, retrieval and basic image management functionalities
  - Package Name: com.imageservice
  - Packaging: Jar
  - Java: 17
- Dependencies:
  - PostgreSQL Driver
  - Spring Data JPA
  - Spring Web
  - Lombok

## Connection between Spring Boot and Postgres

1. Open Postgres Services
2. Prepare the configuration for `application.properties`.
3. Run the application.

The configuration of `application.properties`:

```bash
spring.application.name=Image Storage Service
# URL connect to the DB
spring.datasource.url=jdbc:postgresql://localhost:5432/${POSTGRES_DB}
spring.datasource.username=${POSTGRES_USER}
spring.datasource.password=${POSTGRES_PASSWORD}
# Config JPA
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
```

## Unintall

```bash
source .env
docker compose down
docker volume rm dbykai
docker image rm postgres-ykai
```

## Reference

[how to create a postgres database in docker](https://1kevinson.com/how-to-create-a-postgres-database-in-docker/?source=post_page-----c849ec5daec6--------------------------------
)
[Using Postgres Effectively in Spring Boot Applications](https://hackernoon.com/using-postgres-effectively-in-spring-boot-applications)
[How to Upload and Download Image into SQL Database with Spring Boot](https://medium.com/@kouomeukevin/how-to-upload-and-download-image-into-sql-database-with-spring-boot-c849ec5daec6)
