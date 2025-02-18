FROM alpine:3.21

RUN apk update \
  && apk upgrade \
  && apk add --no-cache openjdk21-jre \
  && rm -rf /var/cache/apk/*

ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/${TZ} /etc/localtime && echo ${TZ} > /etc/timezone

ENV JAVA_OPTS="-XX:+UseZGC -XX:+ZGenerational --spring.profiles.active=prod"
