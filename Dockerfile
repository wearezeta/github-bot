FROM dejankovacevic/bots.runtime:2.10.0

COPY target/github.jar     /opt/github/github.jar
COPY github.yaml           /etc/github/github.yaml

WORKDIR /opt/github

