#!/usr/bin/env bash
docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD
docker build -t $DOCKER_USERNAME/echo-proxy:latest .
docker push $DOCKER_USERNAME/echo-proxy
kubectl delete pod -l name=echo-roman
kubectl get pods -l name=echo-roman
