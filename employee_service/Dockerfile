# Start from OpenJDK base image
FROM eclipse-temurin:21-jdk as build

# Add a volume pointing to /tmp
VOLUME /tmp

# Copy the jar file
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java","-jar","/app.jar"]
