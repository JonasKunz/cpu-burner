FROM maven:3.9.7-eclipse-temurin-21-alpine as build

WORKDIR /build

# Copy the local code to the container
COPY . .

# Build /build/target/cpu-burner.jar
RUN ./mvnw package

# Download /build/elastic-otel-javaagent.jar
#ARG ELASTIC_OTEL_JAVAAGENT_VERSION=0.3.2
#RUN wget --tries=5 -qO- https://repo1.maven.org/maven2/co/elastic/otel/elastic-otel-javaagent/${ELASTIC_OTEL_JAVAAGENT_VERSION}/elastic-otel-javaagent-${ELASTIC_OTEL_JAVAAGENT_VERSION}.jar > ./elastic-otel-javaagent.jar


COPY ./elastic-otel-javaagent-0.4.1-SNAPSHOT.jar ./elastic-otel-javaagent.jar

FROM eclipse-temurin:21-alpine

COPY --from=build /build/target/cpu-burner.jar /
COPY --from=build /build/elastic-otel-javaagent.jar /

ENV OTEL_SERVICE_NAME=cpu-burner-otel
ENV ELASTIC_OTEL_UNIVERSAL_PROFILING_INTEGRATION_ENABLED=true

CMD ["java","-javaagent:./elastic-otel-javaagent.jar", "-Dotel.javaagent.debug=true", "-jar", "./cpu-burner.jar"]