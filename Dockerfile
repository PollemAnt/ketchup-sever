
FROM gradle:8.2.1-jdk17 AS build
WORKDIR /app
COPY . .
RUN ./gradlew fatJar --no-daemon


FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/build/libs/app-all.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
