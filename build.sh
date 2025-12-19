#!/usr/bin/env bash
# Build script for Render deployment

set -o errexit  # Exit on error

echo "🔧 Starting build process..."

# Clean and build the project
echo "📦 Building with Maven..."
./mvnw clean package -DskipTests

echo "✅ Build completed successfully!"
