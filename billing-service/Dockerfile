FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY target/billing-service-1.0.0.jar app.jar

EXPOSE 8080

ENV JAVA_OPTS="-Xms512m -Xmx2g -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
