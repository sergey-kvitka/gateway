FROM eclipse-temurin:17-jdk-alpine
ADD target/*.jar gateway.jar
ENTRYPOINT ["java", "-jar", "/gateway.jar"]
