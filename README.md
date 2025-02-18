# microservice version of AI-based Scheduling System
prerequisite: docker, docker compose, maven, jdk21
```shell
maven clean package
docker-compose build
docker-compose push
```

update docker base jre image
```shell
docker build --no-cache -t ray2/jdk .
docker push ray2/jdk
```

[Docker Hub](https://hub.docker.com/u/ray2)

## Deploy method:
send the **/deploy** folder to the server
```shell
scp -r ./deploy ubuntu@aicourse:~
```

prerequisite: docker, docker compose  
remember: add the user to the docker group
```shell
sudo usermod -aG docker $USER && newgrp docker
```

run the following command on the server  
```shell
cd ~/deploy && sudo sh ./deploy.sh
```

## Using K8s (Not recommended)
remember: those file should be in /home/ubuntu/deploy/k8s  
prerequisite: minikube, kubectl, docker  
```shell
chmod 755 ~/deploy
minikube addons enable ingress
minikube start --mount --mount-string="/home/ubuntu/deploy:/home/ubuntu/deploy"
kubectl apply -f ~/deploy/k8s
kubectl create secret tls www.aicourse-iss.online --key ~/deploy/domain/privkey.pem --cert ~/deploy/domain/fullchain.pem 
```
then config the `/etc/hosts` file

or using reverse proxy
```shell
kubectl port-forward service/nginx 80:80
```

```shell
kubectl delete -f ~/deploy/k8s
```