FROM maven:3.8.8-eclipse-temurin-21-alpine
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]