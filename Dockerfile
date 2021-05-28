# Package pfg-backend
FROM maven:3.5-jdk-8-alpine

ARG github_token
ENV GITHUB_TOKEN=$github_token

COPY config/maven /root/.m2
CMD ["/bin/sh", "-c", "test -s /root/.m2/settings.local.xml && cp /root/.m2/settings.local.xml /root/.m2/settings.xml"]
COPY ./src /app/pfg-backend/src
COPY ./pom.xml /app/pfg-backend/pom.xml
CMD ["/bin/sh", "-c", "envsubst < /root/.m2/settings.xml > /root/.m2/settings.xml"]
RUN cd /app/pfg-backend && mvn package -DskipTests

# Run pfg-backend
FROM openjdk:8-jre AS pfg-backend
WORKDIR /app
COPY --from=0 /app/pfg-backend/target/pfg-web-service.jar /app/app.jar
ENTRYPOINT ["java","-jar","app.jar"]
EXPOSE 8080
