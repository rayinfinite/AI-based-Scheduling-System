FROM openjdk:21-jdk-slim-buster

ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-XX:+UseZGC -XX:+ZGenerational --spring.profiles.active=${SPRING_PROFILES_ACTIVE}"

ENV NAME="./target/scheduler-0.0.1.jar"
ENV SERVER_PORT=9000

WORKDIR /app
COPY $NAME /app/app.jar
EXPOSE $SERVER_PORT
ENTRYPOINT ["sh", "-c", "java -jar /app/app.jar ${JAVA_OPTS} --spring.cloud.azure.active-directory.credential.client-secret=${AZURE_SECRET}"]