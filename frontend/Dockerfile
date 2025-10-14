FROM bellsoft/liberica-runtime-container:jre-25-musl as optimizer
WORKDIR /app
COPY ./target/frontend-0.0.1-SNAPSHOT-spring-boot.jar app.jar
RUN java -Djarmode=tools -jar /app/app.jar extract --layers --destination extracted

FROM bellsoft/liberica-runtime-container:jre-25-slim-musl
WORKDIR /app
COPY --from=optimizer /app/extracted/dependencies/ ./
COPY --from=optimizer /app/extracted/spring-boot-loader/ ./
COPY --from=optimizer /app/extracted/snapshot-dependencies/ ./
COPY --from=optimizer /app/extracted/application/ ./
ENTRYPOINT ["java", "-jar", "/app/app.jar"]