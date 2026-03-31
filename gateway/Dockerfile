#FROM eclipse-temurin:21-jre-jammy
#WORKDIR /app
#COPY target/shareit-gateway-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java", "-jar", "app.jar"]

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
# Исправлено: копируем именно гейтвейный jar
COPY target/shareit-gateway-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

