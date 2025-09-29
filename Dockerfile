# Use an OpenJDK base image (adjust the version if needed)
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file from your local machine into the container
COPY build/libs/authservice-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your application will be running on (8090 in your case)
EXPOSE 8085

# Run the JAR file with Java
ENTRYPOINT ["java", "-jar", "app.jar"]
