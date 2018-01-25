#!/bin/bash

NAME="github-prod"

AUTH_TOKEN=""

kubectl delete secret $NAME
kubectl create secret generic $NAME \
    --from-literal=token=$AUTH_TOKEN \
