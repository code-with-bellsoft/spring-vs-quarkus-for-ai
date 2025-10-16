FROM bellsoft/liberica-runtime-container:jdk-25-musl as builder
WORKDIR /app
COPY . /app/backend
RUN cd backend && ./mvnw clean package

FROM bellsoft/liberica-runtime-container:jre-25-musl as optimizer
WORKDIR /app
COPY --from=builder /app/backend/target/backend-*.jar app.jar
RUN java -Djarmode=tools -jar /app/app.jar extract --layers --destination extracted

FROM bellsoft/liberica-runtime-container:jre-25-slim-musl
WORKDIR /app
COPY --from=optimizer /app/extracted/dependencies/ ./
COPY --from=optimizer /app/extracted/spring-boot-loader/ ./
COPY --from=optimizer /app/extracted/snapshot-dependencies/ ./
COPY --from=optimizer /app/extracted/application/ ./
ENTRYPOINT ["java", "-jar", "/app/app.jar"]