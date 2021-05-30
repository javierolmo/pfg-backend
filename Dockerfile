# Package pfg-backend
FROM maven:3.8.1-jdk-11

ARG github_token
ENV GITHUB_TOKEN=$github_token

COPY config/maven /root/.m2
RUN sed "s/SUPERSECRETTOKEN/$github_token/g" /root/.m2/settings.prod.xml > /root/.m2/settings.xml
RUN cp /root/.m2/settings.local.xml /root/.m2/settings.xml 2>&1; exit 0

COPY ./src /app/pfg-backend/src
COPY ./pom.xml /app/pfg-backend/pom.xml
RUN cd /app/pfg-backend && mvn package -DskipTests -P github

# Run pfg-backend
FROM openjdk:11-jre AS pfg-backend
WORKDIR /app
COPY --from=0 /app/pfg-backend/target/pfg-web-service.jar /app/app.jar
ENTRYPOINT ["java","-jar","app.jar"]
EXPOSE 8080
