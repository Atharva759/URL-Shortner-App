# Use official Java 25 JDK image
FROM eclipse-temurin:25-jdk

# Set working directory inside container
WORKDIR /app

# Copy the Spring Boot JAR (wildcard will pick the JAR in target/)
COPY target/*.jar app.jar

# Expose port your app will run on
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
