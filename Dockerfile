FROM bellsoft/liberica-openjdk-alpine:17
VOLUME /tmp
WORKDIR /app
COPY target/user-service-test-containers-0.0.1-SNAPSHOT.jar /app/
ENTRYPOINT ["java", "-jar", "user-service-test-containers-0.0.1-SNAPSHOT.jar"]