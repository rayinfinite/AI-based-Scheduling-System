FROM ray2/jdk:latest

ENV NAME="./target/auth-0.0.1.jar"
WORKDIR /app
COPY $NAME /app/app.jar
EXPOSE ${SERVER_PORT}
ENTRYPOINT ["sh", "-c", "java -jar /app/app.jar ${JAVA_OPTS}"]