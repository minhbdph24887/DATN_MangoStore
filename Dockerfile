# Stage 1: build
FROM gradle:8.7-jdk21 AS builder
WORKDIR /app
COPY . /app
RUN gradle clean build -x test --no-daemon

# Stage 2: run
FROM openjdk:21
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]