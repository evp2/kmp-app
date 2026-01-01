# Stage 1: Build the Web application
FROM gradle:8.4-jdk17 AS build

# Install libatomic which is required by Node.js on some architectures (like arm64)
USER root
RUN apt-get update && apt-get install -y libatomic1 && rm -rf /var/lib/apt/lists/*
USER gradle

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

# Build the wasmJs browser distribution
RUN ./gradlew :composeApp:wasmJsBrowserDistribution --no-daemon

# Stage 2: Serve the application with Nginx
FROM nginx:alpine
# Copy the build output to nginx's serve directory
COPY --from=build /home/gradle/src/composeApp/build/dist/wasmJs/productionExecutable /usr/share/nginx/html

# Expose port 80
EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
