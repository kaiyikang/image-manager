FROM postgres:17.0-alpine
# https://1kevinson.com/how-to-create-a-postgres-database-in-docker/?source=post_page-----c849ec5daec6--------------------------------

LABEL author="YKai"
LABEL description="Postgres Image for Blog"
LABEL version="1.0"

# Docker automatically executes the initialization script in this directory,
# when the container is first started.
COPY *.sql /docker-entrypoint-initdb.d/