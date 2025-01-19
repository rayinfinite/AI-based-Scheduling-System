#!/bin/bash
#remember to chmod +x deploy.sh
#remember to replase the secret_key with the actual key

docker stop scheduler || true
docker rm scheduler || docker rm -f scheduler|| true

# Pull the latest image from Docker Hub
docker pull ray2/scheduler:latest
docker run --name scheduler -e AZURE_SECRET=secret_key -d -p 9000:9000 ray2/scheduler:latest

# Clean up dangling images
docker image prune -f