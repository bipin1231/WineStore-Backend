# Step 1: Use official OpenJDK 17 image
FROM openjdk:17-jdk-slim

# Step 2: Set working directory inside container
WORKDIR /app

# Step 3: Copy Maven wrapper and project files
COPY mvnw pom.xml ./
COPY .mvn .mvn

# Step 4: Copy source code
COPY src ./src

# Step 5: Make mvnw executable (Linux permission)
RUN chmod +x mvnw

# Step 6: Build the project inside container
RUN ./mvnw clean package -DskipTests

# Step 7: Expose the port Spring Boot runs on
EXPOSE 8080

# Step 8: Run the JAR file
CMD ["java", "-jar", "target/winestore-0.0.1-SNAPSHOT.jar"]
