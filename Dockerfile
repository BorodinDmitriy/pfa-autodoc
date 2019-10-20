FROM java:8-jdk-alpine

COPY ./target/pfa-autodoc-0.0.1-SNAPSHOT.jar /usr/app/

WORKDIR /usr/app

RUN sh -c 'touch pfa-autodoc-0.0.1-SNAPSHOT.jar'

ENTRYPOINT ["java","-jar","pfa-autodoc-0.0.1-SNAPSHOT.jar"]