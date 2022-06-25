FROM openjdk:11-slim-buster as build
COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .
COPY src src
RUN ./mvnw package

FROM adoptopenjdk/openjdk11:alpine-jre
EXPOSE 8000
ARG JAR_FILE=target/messenger_another_db-0.0.1-SNAPSHOT.jar
WORKDIR /opt/app  
COPY ${JAR_FILE} app.jar  
ENTRYPOINT ["java","-jar","app.jar"]