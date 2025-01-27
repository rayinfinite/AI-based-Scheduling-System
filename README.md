# microservice version of AI-based Scheduling System

```shell
 docker-compose build  
 docker-compose push
```

```shell
docker image prune -f 
```

## deploy method:
send the docker-compose file and .env file to the server
```shell
scp ./deploy.docker-compose.yml ubuntu@aicourse:~/docker-compose.yml
scp ./.env ubuntu@aicourse:~/.env
```

run the following command on the server
```shell
sudo docker compose up -d
```