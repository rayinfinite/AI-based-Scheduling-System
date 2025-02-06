#!/bin/bash

for id in $(docker ps -aq --filter "name=course");
  do docker rm -f "$id";
done
docker rm -f nginx || true
chmod -R 755 ./*
docker compose up -d --pull always
docker image prune -f