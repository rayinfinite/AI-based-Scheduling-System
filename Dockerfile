FROM openjdk:21-jdk-slim-buster

ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-XX:+UseZGC -XX:+ZGenerational"
ENV AZURE_OPTS="-Dspring.cloud.azure.active-directory.credential.client-secret=${AZURE_SECRET}"
ENV NAME="./target/scheduler-0.0.1.jar"
ENV SERVER_PORT=9000

WORKDIR /app
COPY $NAME /app/app.jar
EXPOSE $SERVER_PORT
ENTRYPOINT ["java","-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}","$JAVA_OPTS","$AZURE_OPTS","-jar","/app/app.jar"]