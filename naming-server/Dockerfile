# Use the official OpenJDK 17 image as the base image
FROM openjdk:17-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file of your Eureka Naming Server service into the container
COPY build/libs/naming-server-0.0.1-SNAPSHOT.jar /app/naming-server.jar

# Expose the port that the Eureka Naming Server service will run on
EXPOSE 8761

# Command to run the Eureka Naming Server service
CMD ["java", "-jar", "naming-server.jar"]