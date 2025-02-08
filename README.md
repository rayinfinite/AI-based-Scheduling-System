# microservice version of AI-based Scheduling System

```shell
docker-compose build
docker-compose push
```

## Deploy method:
send the **/deploy** folder to the server
```shell
scp -r ./deploy ubuntu@aicourse:~
```

prerequisite: docker, docker compose  
run the following command on the server  
```shell
cd ~/deploy && sudo sh ./deploy.sh
```

## Using K8s (Not recommended)
remember: those file should be in /home/ubuntu/deploy/k8s  
prerequisite: minikube, kubectl, docker  
```shell
chmod 755 ~/deploy
minikube start --mount --mount-string="/home/ubuntu/deploy:/home/ubuntu/deploy"
kubectl apply -f ~/deploy/k8s
```

```shell
kubectl delete -f ~/deploy/k8s
```