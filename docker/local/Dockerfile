FROM maven:3.9.6-eclipse-temurin-21

WORKDIR /app

# Tools for watching mounted source changes
RUN apt-get update \
  && apt-get install -y --no-install-recommends inotify-tools \
  && rm -rf /var/lib/apt/lists/*

# Copy only pom first (better caching)
COPY pom.xml ./

RUN mvn dependency:go-offline

# Dev entrypoint (source code is mounted via docker-compose)
COPY docker/local/dev-entrypoint.sh /app/dev-entrypoint.sh
RUN chmod +x /app/dev-entrypoint.sh

# App will run with mounted source code
EXPOSE 8080

CMD ["/app/dev-entrypoint.sh", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5007", "-XX:StartFlightRecording=filename=/tmp/profile.jfr,dumponexit=true"]
