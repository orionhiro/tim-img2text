FROM eclipse-temurin:21-jdk-alpine

RUN apk add --no-cache maven
WORKDIR /app
COPY . /app
RUN mvn clean package -DskipTests

ENTRYPOINT ["java", "-jar", "/app/target/tim-backend-0.0.1-SNAPSHOT.jar"]