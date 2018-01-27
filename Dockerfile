FROM wire/bots.runtime:latest

COPY target/github.jar     /opt/github/github.jar
COPY conf/github.yaml      /etc/github/github.yaml

WORKDIR /opt/github

