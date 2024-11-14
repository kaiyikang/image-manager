# TODO - Main Title

## First Time Run

```bash
cp .env.sample .env
source .env
docker volume create ${VOLUME_NAME}
# volumes name can be found in 'docker-compose.yml'
docker compose up -d
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
