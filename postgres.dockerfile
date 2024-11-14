FROM postgres:17.0-alpine

LABEL author="YKai"
LABEL description="Postgres Image for Blog"
LABEL version="1.0"

# Docker automatically executes the initialization script in this directory,
# when the container is first started.
COPY db/*.sql /docker-entrypoint-initdb.d/