#!/bin/bash

# Clean and package the Maven project, skipping tests
mvn clean package -DskipTests

# Copy the JAR file to the Docker directory
cp target/demo-0.0.1-SNAPSHOT.jar src/main/docker

# Navigate to the Docker directory
cd src/main/docker

# Bring down any existing Docker containers defined in the Docker Compose file
docker-compose down

# Remove the existing Docker image if it exists
docker rmi docker-spring-boot-postgres:latest

# Rebuild and start the Docker containers defined in the Docker Compose file
docker-compose up --build
