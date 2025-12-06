#!/bin/bash
# ============================================
# RideShare API - Development Runner
# ============================================
# Usage: ./run-dev.sh

cd "$(dirname "$0")"

# Load environment variables from .env file
if [ -f .env ]; then
    echo "Loading environment variables from .env..."
    export $(grep -v '^#' .env | xargs)
fi

# Set JAVA_HOME if using Homebrew OpenJDK 21
if [ -d "/opt/homebrew/opt/openjdk@21" ]; then
    export JAVA_HOME=/opt/homebrew/opt/openjdk@21
fi

echo "Starting RideShare API with profile: ${SPRING_PROFILES_ACTIVE:-dev}"
echo "MongoDB: ${MONGODB_URI:0:50}..."
echo "Server Port: ${SERVER_PORT:-8081}"
echo ""

# Run Spring Boot
mvn spring-boot:run
