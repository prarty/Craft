# Use the official OpenJDK 17 image as the base image
FROM openjdk:17-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file of your onboarding service into the container
COPY build/libs/onboarding-service-0.0.1-SNAPSHOT.jar /app/onboarding-service.jar

# Expose the port that the onboarding service service will run on
EXPOSE 9003

# Command to run the onboarding service
CMD ["java", "-jar", "onboarding-service.jar"]