FROM dejankovacevic/bots.runtime:2.10.2

COPY target/github.jar     /opt/github/github.jar
COPY github.yaml           /etc/github/github.yaml

WORKDIR /opt/github

EXPOSE  8080 8081 8082
