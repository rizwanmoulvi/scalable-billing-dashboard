FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/analytics-service-1.0.0.jar app.jar
ENV JAVA_OPTS="-Xms256m -Xmx1g"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
