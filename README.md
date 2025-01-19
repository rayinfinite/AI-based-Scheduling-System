# AI-based Scheduling System

```shell
nohup java -jar -XX:+UseZGC -XX:+ZGenerational ./target/scheduler-0.0.1.jar &
```

```docker
docker run --name scheduler -e AZURE_SECRET=secret_key -d -p 9000:9000 ray2/scheduler:latest
```

## Prerequisites
- Java 21
- Node 20
- Familiarity with *Spring Framework* & *Shadcn Ui*
- [Azure OAuth](https://github.com/Azure-Samples/ms-identity-msal-java-sample)