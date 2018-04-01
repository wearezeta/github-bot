#!/usr/bin/env bash
mvn package
docker build -t dejankovacevic/github-bot:0.6.0 .
docker push dejankovacevic/github-bot
kubectl delete pod -l name=github
kubectl get pods -l name=github
