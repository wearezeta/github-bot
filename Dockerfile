FROM wire/bots.runtime:latest

COPY target/github.jar      /opt/github/github.jar

WORKDIR /opt/github

