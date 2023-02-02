FROM openjdk:11-jre-slim

WORKDIR /app

COPY ./target/tdl-1.0.0.war .

EXPOSE 8080

ENTRYPOINT ["java","-jar","tdl-1.0.0.war"]