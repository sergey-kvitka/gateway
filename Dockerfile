FROM eclipse-temurin:17-jdk-alpine
ARG JAR_FILE=target/*.jar
ADD JAR_FILE gateway.jar
ENTRYPOINT ["java", "-jar", "/gateway.jar"]