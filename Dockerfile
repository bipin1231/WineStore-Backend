# Step 1: Use a valid Java 17 base image
FROM eclipse-temurin:17-jdk

# Step 2: Set working directory
WORKDIR /app

# Step 3: Copy Maven wrapper and project files
COPY mvnw pom.xml ./
COPY .mvn .mvn

# Step 4: Copy source code
COPY src ./src

# Step 5: Make mvnw executable
RUN chmod +x mvnw

# Step 6: Build the project
RUN ./mvnw clean package -DskipTests

# Step 7: Expose Spring Boot port
EXPOSE 8080

# Step 8: Run the JAR file
CMD ["java", "-jar", "target/winestore-0.0.1-SNAPSHOT.jar"]
