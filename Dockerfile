# Step 1: Build stage (Using Java 21)
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy configuration and source code
COPY pom.xml .
COPY api/src ./src

# Build the executable Spring Boot JAR
RUN mvn clean package -DskipTests

# Step 2: Runtime stage (Using Java 21)
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the compiled jar
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]