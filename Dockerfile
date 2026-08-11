# Step 1: Build the Java JAR file using Maven
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
# 1. Change 'backend' to your actual subfolder name if different
WORKDIR /app/backend 
# 2. Build from inside the folder where pom.xml sits
RUN mvn clean package -DskipTests 

# Step 2: Run the app using JDK 17
FROM eclipse-temurin:17-jre
WORKDIR /app
# Make sure this points to the target folder inside your backend directory
COPY --from=build /app/backend/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]