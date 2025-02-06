# microservice version of AI-based Scheduling System

```shell
docker-compose build
docker-compose push
```

## deploy method:
send the **/deploy** folder to the server
```shell
scp -r ./deploy ubuntu@aicourse:~
```

run the following command on the server
```shell
cd ~/deploy && sudo sh ./deploy.sh
```