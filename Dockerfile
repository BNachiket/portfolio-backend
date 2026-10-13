FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN printf '%s' '<settings><mirrors><mirror><id>google-maven-central</id><name>Google Maven Central mirror</name><url>https://maven-central.storage-download.googleapis.com/maven2/</url><mirrorOf>central</mirrorOf></mirror></mirrors></settings>' > /tmp/settings.xml \
 && (mvn -s /tmp/settings.xml -q -B -DskipTests package || (sleep 30 && mvn -s /tmp/settings.xml -q -B -DskipTests package))

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/portfolio-api.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]