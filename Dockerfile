FROM dejankovacevic/bots.runtime:latest

COPY target/github.jar     /opt/github/github.jar
COPY github.yaml           /etc/github/github.yaml

WORKDIR /opt/github

