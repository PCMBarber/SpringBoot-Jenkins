FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

# Discard source code and only have war file
FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=build /home/app/target/tdl-1.0.0.war .
EXPOSE 8080
ENTRYPOINT ["java","-jar","tdl-1.0.0.war"]
