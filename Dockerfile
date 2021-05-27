# Package pfg-backend
FROM maven:3.5-jdk-8-alpine
COPY maven-settings.xml /root/.m2/settings.xml
COPY ./src /app/pfg-backend/src
COPY ./pom.xml /app/pfg-backend/pom.xml
RUN cd /app/pfg-backend && mvn package -DskipTests

# Run pfg-backend
FROM openjdk:8-jre AS pfg-backend
WORKDIR /app
COPY --from=0 /app/pfg-backend/target/pfg-web-service.jar /app/app.jar
ENTRYPOINT ["java","-jar","app.jar"]
EXPOSE 8080
