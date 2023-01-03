FROM adoptopenjdk/openjdk11
VOLUME /tmp
COPY target/drone-service-0.0.1-SNAPSHOT.jar drone-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "drone-service.jar"]