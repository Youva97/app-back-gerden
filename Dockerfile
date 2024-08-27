# Build part
FROM maven:3-openjdk-17 AS build
# Set the working directory in the container
WORKDIR /app
# Copy the pom.xml and the project files to the container
COPY pom.xml .
COPY src ./src
# Build the project
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-alpine

# Copy the JAR package into the image
COPY --from=build /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8082

# Run the App
ENTRYPOINT ["java", "-jar", "/app.jar"]