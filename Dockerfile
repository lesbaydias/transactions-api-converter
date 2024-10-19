FROM maven:3.9.5-amazoncorretto-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

COPY --from=build /app/target/transactionservice-0.0.1-SNAPSHOT.jar transactionservice.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "transactionservice.jar"]
