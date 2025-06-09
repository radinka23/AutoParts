FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests -e -X

FROM eclipse-temurin:24-jdk-alpine
WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
