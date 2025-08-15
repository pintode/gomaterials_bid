FROM gradle:9-jdk21-alpine AS builder
WORKDIR /app
COPY --chown=gradle:gradle ./src ./src
COPY --chown=gradle:gradle ./build.gradle ./build.gradle
RUN --mount=type=cache,target=/root/.gradle/caches gradle clean build -x test --no-daemon

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/*-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]