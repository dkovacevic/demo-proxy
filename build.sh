#!/usr/bin/env bash
docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD
mvn package -DskipTests=true -Dmaven.javadoc.skip=true
docker build -t $DOCKER_USERNAME/echo-proxy:latest .
docker push $DOCKER_USERNAME/echo-proxy
kubectl delete pod -l name=demo
kubectl get pods -l name=demo
