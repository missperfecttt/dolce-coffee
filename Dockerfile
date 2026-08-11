# Step 1: Build stage (Using Java 21)
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy configuration and map the source code correctly
COPY pom.xml .
COPY api/src ./src

# Execute the Maven package step AND repackage into an executable JAR
RUN mvn clean package spring-boot:repackage -DskipTests

# Execute the Maven package step
RUN mvn clean package -DskipTests

# Step 2: Runtime stage (Using Java 21)
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the compiled jar
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]