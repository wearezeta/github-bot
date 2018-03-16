#!/usr/bin/env bash
mvn package
docker build -t dejankovacevic/github-bot:latest .
docker push dejankovacevic/github-bot
kubectl delete pod -l name=github -n prod
kubectl get pods -l name=github -n prod
