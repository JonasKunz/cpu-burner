This project is a CPU-bound demo application for showcasing the elastic profiling capabilities.

To build the application, run `./mvnw package`.

Running with the latest [Elastic OpenTelemetry distribution](https://github.com/elastic/elastic-otel-java):
```
curl -o 'elastic-otel-javaagent.jar' -L 'https://oss.sonatype.org/service/local/artifact/maven/redirect?r=releases&g=co.elastic.otel&a=elastic-otel-javaagent&v=LATEST'
java -javaagent:./elastic-otel-javaagent.jar \
-Dotel.exporter.otlp.endpoint=<elastic-cloud-OTLP-endpoint> \
"-Dotel.exporter.otlp.headers=Authorization=Bearer XXXX" \
-Dotel.service.name=cpu-burner-otel \
-Delastic.otel.universal.profiling.integration.enabled=true \
-jar ./target/cpu-burner.jar
```

Running with the latest [classic Elastic APM Agent](https://github.com/elastic/apm-agent-java):
```
curl -o 'elastic-apm-agent.jar' -L 'https://oss.sonatype.org/service/local/artifact/maven/redirect?r=releases&g=co.elastic.apm&a=elastic-apm-agent&v=LATEST'
java -javaagent:elastic-apm-agent.jar \
-Delastic.apm.service_name=cpu-burner-elastic \
-Delastic.apm.secret_token=XXXXX \
-Delastic.apm.server_url=<elastic-apm-server-endpoint> \
-Delastic.apm.application_packages=co.elastic.demo \
-Delastic.apm.universal_profiling_integration_enabled=true \
-jar ./target/cpu-burner.jar
```

Note that at the time of writing, universal profiling is only supported on Linux.
On macOS, you can use [colima](https://github.com/abiosoft/colima) and docker containers to avoid having to setup a full Linux VM yourself.

Use the following commands to build and run the cpu-burner app in Docker with the OpenTelemetry agent:
```
docker build -t cpu-burner .
docker run --rm -e OTEL_EXPORTER_OTLP_ENDPOINT=<elastic-cloud-OTLP-endpoint> -e OTEL_EXPORTER_OTLP_HEADERS="Authorization=Bearer XXXX" cpu-burner
```

