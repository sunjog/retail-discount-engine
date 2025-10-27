# Use Amazon Corretto 17 as base
FROM public.ecr.aws/corretto/corretto:17-alpine

# Set working directory
WORKDIR /app

# Copy the built JAR into the container
COPY target/retail-discount-engine-0.0.1-SNAPSHOT.jar app.jar

# Set the entrypoint for Lambda Runtime Interface Emulator (if testing locally)
ENTRYPOINT ["java", "-jar", "app.jar"]
