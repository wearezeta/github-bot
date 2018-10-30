#!/usr/bin/env bash
mvn package -DskipTests=true -Dmaven.javadoc.skip=true
docker build -t dejankovacevic/github-bot:latest .
docker push dejankovacevic/github-bot
kubectl delete pod -l name=github
kubectl get pods -l name=github
