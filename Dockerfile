# Stage 1: Build the application
FROM gradle:latest AS build
WORKDIR /app
COPY . .
RUN gradle build --no-daemon

# Stage 2: final image
FROM openjdk:17
MAINTAINER aliguvenbas@gmail.com
WORKDIR /app
COPY --from=build /app/build/libs/xyzbank-*.jar xyzbank.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "xyzbank.jar"]
