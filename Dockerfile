FROM maven:3.9.7-eclipse-temurin-21-alpine as build

WORKDIR /build

# Copy the local code to the container
COPY . .

# Build /build/target/cpu-burner.jar
RUN ./mvnw package

# Download /build/elastic-otel-javaagent.jar
ARG ELASTIC_OTEL_JAVAAGENT_VERSION=1.0.0
RUN wget --tries=5 -qO- https://repo1.maven.org/maven2/co/elastic/otel/elastic-otel-javaagent/${ELASTIC_OTEL_JAVAAGENT_VERSION}/elastic-otel-javaagent-${ELASTIC_OTEL_JAVAAGENT_VERSION}.jar > ./elastic-otel-javaagent.jar


FROM ubuntu:jammy@sha256:7c75ab2b0567edbb9d4834a2c51e462ebd709740d1f2c40bcd23c56e974fe2a8
RUN apt update && apt -y install openjdk-21-dbg

COPY --from=build /build/target/cpu-burner.jar /
COPY --from=build /build/elastic-otel-javaagent.jar /

ENV OTEL_SERVICE_NAME=cpu-burner-otel
ENV ELASTIC_OTEL_UNIVERSAL_PROFILING_INTEGRATION_ENABLED=true

CMD ["java","-jar", "./cpu-burner.jar"]
