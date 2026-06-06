#!/usr/bin/env bash
set -euo pipefail

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

# Map secrets to Spring Boot expected environment variables if they were loaded
[ -n "${DB_USER:-}" ] && export SPRING_DATASOURCE_USERNAME="$DB_USER"
[ -n "${DB_PASSWORD:-}" ] && export SPRING_DATASOURCE_PASSWORD="$DB_PASSWORD"
# DB_NAME and JWT_SECRET are used directly via ${DB_NAME} and ${JWT_SECRET} in properties

cd /app

# Ensure logs directory exists and create today's dated logfile and app.log; set ownership to host user
LOG_PATH="${LOG_PATH:-/app/logs}"
mkdir -p "$LOG_PATH"
# create today's dated file for server logs and a separate app.log for other logs
TODAY=$(date +%F)
DatedFile="$LOG_PATH/app-$TODAY.log"
touch "$DatedFile"
touch "$LOG_PATH/app.log"
# set ownership if LOCAL_UID/LOCAL_GID provided
if [ -n "${LOCAL_UID-}" ] && [ -n "${LOCAL_GID-}" ]; then
  chown -R "${LOCAL_UID}:${LOCAL_GID}" "$LOG_PATH" || true
fi

# Initial compile so DevTools has classes to watch.
mvn -q -DskipTests compile

# Run Spring Boot (foreground is managed by this script).
mvn -DskipTests spring-boot:run -Dspring-boot.run.fork=false -Dspring-boot.run.addResources=true &
boot_pid="$!"

compile() {
  # Avoid overlapping compiles if multiple filesystem events arrive together.
  (
    flock 9
    mvn -q -DskipTests compile
  ) 9>/tmp/mvn-compile.lock
}

cleanup() {
  if kill -0 "$boot_pid" 2>/dev/null; then
    kill "$boot_pid" || true
  fi
}
trap cleanup EXIT

if command -v inotifywait >/dev/null 2>&1; then
  (
    while inotifywait -r -e modify,create,delete,move /app/src/main/java /app/src/main/resources >/dev/null 2>&1; do
      compile || true
    done
  ) &
  watcher_pid="$!"

  wait "$boot_pid" || true
  kill "$watcher_pid" >/dev/null 2>&1 || true
else
  echo "inotifywait not found; running without hot reload compile watcher" >&2
  wait "$boot_pid"
fi

