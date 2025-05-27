# Use a slim JDK runtime (Java 21)
FROM eclipse-temurin:21-jre

# Copy the jar file from your build directory
COPY build/libs/moviereview-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080 (Spring Boot default)
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "/app.jar"]
