FROM ubuntu:xenial

ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update \
    && apt-get install openjdk-8-jre-headless -qqy --no-install-recommends

ENV JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64

COPY target/echo-jar-with-dependencies.jar /opt/echo/echo.jar
COPY echo.yaml                             /opt/echo/echo.yaml

WORKDIR /opt/echo

EXPOSE  8080

CMD ["sh", "-c", "/usr/bin/java -jar echo.jar"]