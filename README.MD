This project is a CPU-bound demo application for showcasing the elastic profiling capabilities.

To build, run `./mvnw package`.

Running with the [Elastic OpenTelemetry distribution](https://github.com/elastic/elastic-otel-java):
```
java -javaagent:./elastic-otel-javaagent-<version>.jar \
-Dotel.exporter.otlp.endpoint=<elastic-cloud-OTLP-endpoint> \
"-Dotel.exporter.otlp.headers=Authorization=Bearer XXXX" \
-Dotel.service.name=cpu-burner-otel \
-Delastic.otel.universal.profiling.integration.enabled=true \
-jar ./target/cpu-burner.jar
```

Running with the [classic Elastic APM Agent](https://github.com/elastic/apm-agent-java):
```
java -javaagent:elastic-apm-agent-<version>.jar \
-Delastic.apm.service_name=cpu-burner-elastic \
-Delastic.apm.secret_token=XXXXX \
-Delastic.apm.server_url=<elastic-apm-server-enndpoint> \
-Delastic.apm.application_packages=co.elastic.demo \
-Delastic.apm.universal_profiling_integration_enabled=true \
-jar ./target/cpu-burner.jar
```