# Use the official OpenJDK image as the base image
FROM openjdk:17-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file of your API Gateway service into the container
COPY build/libs/api-gateway-0.0.1-SNAPSHOT.jar /app/api-gateway.jar

# Expose the port that the API Gateway service will run on
EXPOSE 8765

# Command to run the API Gateway service
CMD ["java", "-jar", "api-gateway.jar"]