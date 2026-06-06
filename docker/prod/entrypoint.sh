#!/bin/sh
set -eu

# Load all docker secrets (files in /run/secrets) into environment variables
if [ -d /run/secrets ]; then
  for f in /run/secrets/*; do
    [ -f "$f" ] || continue
    name=$(basename "$f")
    # export with the same name; trim CRLF
    val=$(cat "$f")
    export "$name"="$val"
  done
fi

# If DB_HOST/DB_PORT/DB_NAME provided as env or secret, compose the SPRING_DATASOURCE_URL
if [ -n "${DB_HOST:-}" ] && [ -n "${DB_PORT:-}" ] && [ -n "${DB_NAME:-}" ]; then
  export SPRING_DATASOURCE_URL="jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}"
fi

# If DB_USER secret provided, export SPRING_DATASOURCE_USERNAME
if [ -n "${DB_USER:-}" ]; then
  export SPRING_DATASOURCE_USERNAME="$DB_USER"
fi

# If DB_PASSWORD secret provided, export SPRING_DATASOURCE_PASSWORD
if [ -n "${DB_PASSWORD:-}" ]; then
  export SPRING_DATASOURCE_PASSWORD="$DB_PASSWORD"
fi

# If JWT_SECRET provided as secret, export to env JWT_SECRET (application uses this)
if [ -n "${JWT_SECRET:-}" ]; then
  export JWT_SECRET="$JWT_SECRET"
fi

# Allow JAVA_OPTS to be passed from environment
JAVA_CMD="java ${JAVA_OPTS:-} -jar /app/app.jar"

exec sh -c "$JAVA_CMD"
