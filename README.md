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
