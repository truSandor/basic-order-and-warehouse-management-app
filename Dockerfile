FROM  maven:3-eclipse-temurin-17-alpine@sha256:24abed2b13a6ed90ab1bbb5ca239bb56a994381c2357d465fe8f45185a90bc69 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src /app/src
RUN mvn package -DskipTests
FROM eclipse-temurin:17-alpine@sha256:1ecf4fe36cb342580299254487b5d899a19c5238bbac06070ebc107f513eb473
WORKDIR /app
RUN addgroup --system javauser
RUN adduser -S -s /bin/false -G javauser javauser
COPY --from=builder /app/target/app-0.0.1-SNAPSHOT.jar /app/application.jar
RUN chown -R javauser:javauser /app
EXPOSE 8080
USER javauser
CMD ["java", "-jar", "application.jar"]